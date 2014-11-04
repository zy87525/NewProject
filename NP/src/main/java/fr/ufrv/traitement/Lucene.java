package fr.ufrv.traitement;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.hp.hpl.jena.rdf.model.Statement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Lucene {
	private static Version VERSION = Version.LUCENE_4_10_1;
	private StandardAnalyzer analyzer;
	private static String indexPath = "C:/java_project/fr.uvsq.javaproject/index_files/index";
	private Directory index ;
	@SuppressWarnings("deprecation")
	public Lucene()
	{
		analyzer = new StandardAnalyzer(VERSION);
	}

	public void index(ArrayList<Statement> _triplets) throws IOException, ParseException {

		index = FSDirectory.open(new File(indexPath));   
		IndexWriterConfig config = new IndexWriterConfig(VERSION, analyzer);
		config.setOpenMode(OpenMode.CREATE);

		IndexWriter w = new IndexWriter(index, config);
		for(Statement tr : _triplets)
			addDoc(w,tr.getSubject().toString(),tr.getPredicate().getLocalName().toString(), tr.getObject().toString());

		w.close();

	}

	public Object[][]  Search(String querystr) throws ParseException, IOException
	{
		// the "title" arg specifies the default field to use
		// when no field is explicitly specified in the query.
		@SuppressWarnings("deprecation")
		Query q = new QueryParser(Version.LUCENE_40, "objet", analyzer).parse(querystr);

		int hitsPerPage = 10;
		IndexReader reader = DirectoryReader.open(index);
		IndexSearcher searcher = new IndexSearcher(reader);
		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
		searcher.search(q, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;

		Object[][] tArray = new Object[hits.length][3];
		if(tArray.length !=0)System.out.println("plein");
		System.out.println("Found " + hits.length + " hits.");
	
		for(int i=0;i<hits.length;++i) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			tArray[i][0]= d.get("ressource");
			tArray[i][1]= d.get("predicate");
			tArray[i][2]= d.get("objet");
			System.out.println((i + 1) + ". " + d.get("ressource") + "\t"+d.get("predicate")+"\t" + d.get("objet"));
		}


		// reader can only be closed when there
		// is no need to access the documents any more.
		reader.close();
		return tArray;
	}

	private static void addDoc(IndexWriter w, String _ressource,String _predicate, String _objet) throws IOException {
		Document doc = new Document();
		doc.add(new TextField("ressource", _ressource, Field.Store.YES));
		doc.add(new StringField("predicate", _predicate, Field.Store.YES));
		doc.add(new TextField("objet", _objet, Field.Store.YES));
		w.addDocument(doc);
	}
}