/**
 * 
 */
package com.uvsq.Jung;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JFrame;

import net.rootdev.jenajung.JenaJungGraph;
import net.rootdev.jenajung.Transformers;

import org.apache.commons.collections15.Transformer;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.VCARD;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

/**
 * @author Bruce GONG
 *
 */
public class JUNG {
	static String vcardUri="http://www.w3.org/2001/vcard-rdf/3.0#";
	
	public static Resource createRes(Model model, TupleRDF t)
	{
		Resource res = model.createResource(t.getSujet().toString());
		if(t.isHasSubNode())
		{
			Resource res1 = createRes(model, (TupleRDF)t.getLiteral());
			res.addProperty(model.createProperty(vcardUri, t.getPredicat().toString()), res1);
		}else{
			res.addProperty(model.createProperty(vcardUri, t.getPredicat().toString()), t.getLiteral().toString());
		}
		return res;
	}
	
	//pris en charge de transformer Object[][] en List<TupleRDF> bien organise
	public static Model convertirModel(Object[][] result)
	{
		Model model = ModelFactory.createDefaultModel();
		Hashtable htSub = new Hashtable();
		Hashtable htObj = new Hashtable();
		ArrayList<TupleRDF> ts = new ArrayList<TupleRDF>();
		for(Object[] ligne : result){
//			String sujet = ligne[0].toString();
//			String predicat = ligne[1].toString();
//			String objet = ligne[2].toString();
			//Statement stat = ResourceFactory.createStatement(sujet, predicat, objet);
//			Resource res = model.createResource(sujet)
//					.addProperty(model.createProperty(vcardUri, predicat), objet);
			TupleRDF t = new TupleRDF(ligne[0], ligne[1], ligne[2]);
			htSub.put(t.getSujet(), t);
			htObj.put(t.getLiteral(), t);
			ts.add(t);
		}
		ArrayList<TupleRDF> tsClone = new ArrayList<TupleRDF>();
		tsClone = (ArrayList<TupleRDF>) ts.clone();
		for(TupleRDF t : tsClone)
		{
			if(htSub.containsKey(t.getLiteral())){
				System.out.println("got it");
				TupleRDF t1 = (TupleRDF) htSub.get(t.getLiteral());
				t.setLiteral(t1);
				t.setHasSubNode(true);
				ts.remove(t1);
			}
		}
		for(TupleRDF t : ts)
		{
			createRes(model, t);
		}
		return model;
	}
	
	public static void creatTree(Object[][] result){
		if(result.length == 0)
		{
			return;
		}
		Model model = convertirModel(result);
		
		int width = 1350;
        int height = 700;
        
        //Model model = null;		//FileManager.get().loadModel(resource);
        Graph<RDFNode, Statement> g = new JenaJungGraph(model);

        Layout<RDFNode, Statement> layout = new FRLayout(g);
        layout.setSize(new Dimension(width, height));
        VisualizationViewer<RDFNode, Statement> viz =
                new VisualizationViewer<RDFNode, Statement>(layout);
        viz.setPreferredSize(new Dimension(width, height));
        
        Transformer<RDFNode,Paint> vertexPaint = new Transformer<RDFNode,Paint>() {
        	public Paint transform(RDFNode i) {
        	return Color.GREEN;
        	}
        };  
        
        RenderContext context = viz.getRenderContext();
        context.setEdgeLabelTransformer(Transformers.EDGE); // property label
        context.setVertexLabelTransformer(Transformers.NODE); // node label
        //set colors of Vertex
        context.setVertexFillPaintTransformer(vertexPaint);
        //set the position of vertex's label.
        viz.getRenderer().getVertexLabelRenderer().setPosition(Position.S);
     // Create a graph mouse and add it to the visualization component
        DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
        //gm.setMode(Mode.TRANSFORMING);
        gm.setMode(Mode.PICKING);
        viz.setGraphMouse(gm);
        
        JFrame app = new JFrame("Nad est le plus CON");
        app.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        app.getContentPane().add(viz);
        app.pack();
        app.setVisible(true);
	}
}
