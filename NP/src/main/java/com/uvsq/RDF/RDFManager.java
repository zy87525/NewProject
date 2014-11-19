package com.uvsq.RDF;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;

public class RDFManager {
	private RDF rdf;
	private ArrayList<Statement> triplets;
	
	public ArrayList<Statement> getTriplets() {
		return triplets;
	}
	public void setTriplets(ArrayList<Statement> triplets) {
		this.triplets = triplets;
	}

	public RDFManager(RDF rdf)
	{
		this.rdf = rdf;
		triplets = new ArrayList<Statement>();
	}
	
	//Fonction permettant de charger un fichier RDF
	private InputStream loadFileRDF()
	{
		try
		{
			File f = new File(this.rdf.getFileName());
			
			if(f.exists())
			{
				InputStream input = FileManager.get().open(this.rdf.getFileName());
				return input;	
			}
			else 
				{
					System.out.println("Erreur: Fichier: " + this.rdf.getFileName() + "  Introuvable");
					return null;
				}
		}
		catch(IllegalArgumentException   io)
		{
			System.out.println("Error:"+io.getMessage());
			return null;
		}
	}
	
	//Fonction permettant de lire un fichier rdf
	public int readFileRDF()
	{
		Model model = ModelFactory.createDefaultModel();
		
		InputStream input = loadFileRDF();
		
		if(input!=null)
		{
			model.read(input,null);
			
			StmtIterator iter = model.listStatements();
			System.out.println("y");
			while (iter.hasNext()) {
				Statement stmt      = iter.nextStatement(); // get next statement
			
                triplets.add(stmt);	
                 
        }
			return 1;
		}
		else return 0;
	}
	
	
	public final RDF getRdf() {
		return rdf;
	}
	public final void setRdf(RDF rdf) {
		this.rdf = rdf;
	}
}
