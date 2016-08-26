package com.jqbian.lucene.cookbook.chapter01;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

public class LuceneTest {
	private int maxNumberOfResults=10;
	 public static void main(String[] args) throws IOException, ParseException {
	        Analyzer analyzer = new StandardAnalyzer();
	        
	        Directory directory = new RAMDirectory();

	        IndexWriterConfig config = new IndexWriterConfig(analyzer);
	        IndexWriter indexWriter = new IndexWriter(directory, config);

	        Document doc = new Document();
	        String text = "Lucene is an Information Retrieval library written in Java";

	        doc.add(new TextField("Content", text, Field.Store.YES));

	        indexWriter.addDocument(doc);
	        indexWriter.close();
	        
	        IndexReader indexReader = DirectoryReader.open(directory);
	        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
	        
	        QueryParser parser = new QueryParser("Content", analyzer);
	        Query query = parser.parse("Lucene");

	        int hitsPerPage = 10;
	        TopDocs docs = indexSearcher.search(query, hitsPerPage);
	        ScoreDoc[] hits = docs.scoreDocs;
	        //int end = Math.min(hits.totalHits, hitsPerPage);
	        int end = Math.min(hits.length, hitsPerPage);	        
	        System.out.print("Total Hits: " + hits.length);
	        System.out.print("Results: ");
	        for (int i = 0; i < end; i++) {
	            Document d = indexSearcher.doc(hits[i].doc);
	            System.out.println("Content: " + d.get("Content"));
	        }	        
	    }
	 
	 	
	 	public List<Document> getPage(IndexSearcher searcher,QueryParser parser,String searchTerm,int from , int size) throws ParseException, IOException{
		    List<Document> documents = new ArrayList<Document>();
		    Query query = parser.parse(searchTerm);
		    TopDocs hits = searcher.search(query, maxNumberOfResults);
		    int end = Math.min(hits.totalHits, size);
		    for (int i = from; i < end; i++) {
		        int docId = hits.scoreDocs[i].doc;
		        //load the document
		        Document doc = searcher.doc(docId);
		        documents.add(doc);
		    }
		    return documents;
		}
}
