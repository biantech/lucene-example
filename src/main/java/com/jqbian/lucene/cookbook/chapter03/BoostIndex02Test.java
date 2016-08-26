package com.jqbian.lucene.cookbook.chapter03;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

public class BoostIndex02Test {
	
	@Test
	public void testBoost() throws IOException{
		Analyzer analyzer = new StandardAnalyzer();
		Directory directory = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		MySimilarity similarity = new MySimilarity(new ClassicSimilarity());
		config.setSimilarity(similarity);
		IndexWriter indexWriter = new IndexWriter(directory, config);
		Document doc = new Document();
		TextField textField = new TextField("name", "", Field.Store.YES);
		NumericDocValuesField docValuesField = new NumericDocValuesField("ranking", 1);
		long ranking = 1l;
		String[] names = {"John R Smith", "Mary Smith", "Peter Smith"};
		for (String name : names) {
		ranking *= 2;
		textField.setStringValue(name);
		docValuesField.setLongValue(ranking);
		doc.removeField("name");
		doc.removeField("ranking");
		doc.add(textField);
		doc.add(docValuesField);
		indexWriter.addDocument(doc);
		}
		indexWriter.commit();
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		indexSearcher.setSimilarity(similarity);
		Query query = new TermQuery(new Term("name", "smith"));
		TopDocs topDocs = indexSearcher.search(query, 100);
		System.out.println("Searching 'smith'");
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
		doc = indexReader.document(scoreDoc.doc);
		System.out.println(doc.getField("name").stringValue());
		}
		indexWriter.close();
	}
}
