package com.jqbian.lucene.cookbook.chapter07;

import java.io.IOException;



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
import org.apache.lucene.search.similarities.AfterEffectL;
import org.apache.lucene.search.similarities.BasicModelIF;
import org.apache.lucene.search.similarities.BasicModelIn;
import org.apache.lucene.search.similarities.DFRSimilarity;
import org.apache.lucene.search.similarities.DistributionSPL;
import org.apache.lucene.search.similarities.IBSimilarity;
import org.apache.lucene.search.similarities.LambdaDF;
import org.apache.lucene.search.similarities.Normalization;
import org.apache.lucene.search.similarities.NormalizationH1;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

public class DFRSimilarityTest {
	
	@Test
	public void testDFRSimilarity() throws IOException, ParseException{
		StandardAnalyzer analyzer = new StandardAnalyzer();
		Directory directory = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		DFRSimilarity similarity = new DFRSimilarity(
		new BasicModelIn(),
		new AfterEffectL(),
		new NormalizationH1());
		config.setSimilarity(similarity);
		IndexWriter indexWriter = new IndexWriter(directory, config);
		Document doc = new Document();
		TextField textField = new TextField("content", "",Field.Store.YES);
		String[] contents = {"Humpty Dumpty sat on a wall,",
				"Humpty Dumpty had a great fall.",
				"All the king's horses and all the king's men",
				"Couldn't put Humpty together again."};		
		for (String content : contents) {
			textField.setStringValue(content);
			doc.removeField("content");
			doc.add(textField);
			indexWriter.addDocument(doc);
		}
		indexWriter.commit();
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		indexSearcher.setSimilarity(similarity);
		QueryParser queryParser = new QueryParser("content", analyzer);
		Query query = queryParser.parse("humpty dumpty");
		TopDocs topDocs = indexSearcher.search(query, 100);
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			doc = indexReader.document(scoreDoc.doc);
			System.out.println(scoreDoc.score + ": " +
			doc.getField("content").stringValue());
		}
	}
	
	@Test
	public void testDFRSimilarity2() throws IOException, ParseException{
		StandardAnalyzer analyzer = new StandardAnalyzer();
		Directory directory = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		DFRSimilarity similarity = new DFRSimilarity(
		new BasicModelIF(),
		new AfterEffectL(),
		new Normalization.NoNormalization());
		config.setSimilarity(similarity);
		IndexWriter indexWriter = new IndexWriter(directory, config);
		Document doc = new Document();
		TextField textField = new TextField("content", "",Field.Store.YES);
		String[] contents = {"Humpty Dumpty sat on a wall,",
				"Humpty Dumpty had a great fall.",
				"All the king's horses and all the king's men",
				"Couldn't put Humpty together again."};		
		for (String content : contents) {
			textField.setStringValue(content);
			doc.removeField("content");
			doc.add(textField);
			indexWriter.addDocument(doc);
		}
		indexWriter.commit();
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		indexSearcher.setSimilarity(similarity);
		QueryParser queryParser = new QueryParser("content", analyzer);
		Query query = queryParser.parse("humpty dumpty");
		TopDocs topDocs = indexSearcher.search(query, 100);
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			doc = indexReader.document(scoreDoc.doc);
			System.out.println(scoreDoc.score + ": " +
			doc.getField("content").stringValue());
		}
	}
	
	@Test
	public void testLambdaDF() throws IOException, ParseException{
		StandardAnalyzer analyzer = new StandardAnalyzer();
		Directory directory = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IBSimilarity similarity = new IBSimilarity(
		new DistributionSPL(),
		new LambdaDF(),
		new NormalizationH1());
		config.setSimilarity(similarity);
		IndexWriter indexWriter = new IndexWriter(directory, config);
		Document doc = new Document();
		TextField textField = new TextField("content", "",
		Field.Store.YES);
		String[] contents = {"Humpty Dumpty sat on a wall,",
			"Humpty Dumpty had a great fall.",
			"All the king's horses and all the king's men",
			"Couldn't put Humpty together again."};
		for (String content : contents) {
		textField.setStringValue(content);
		doc.removeField("content");
		doc.add(textField);
		indexWriter.addDocument(doc);
		
		}
		indexWriter.commit();
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		indexSearcher.setSimilarity(similarity);
		QueryParser queryParser = new QueryParser("content", analyzer);
		Query query = queryParser.parse("humpty dumpty");
		TopDocs topDocs = indexSearcher.search(query, 100);
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			doc = indexReader.document(scoreDoc.doc);
			System.out.println(scoreDoc.score + ": " +	doc.getField("content").stringValue());
		}
	}
	
	@Test
	public void testNormalization() throws IOException, ParseException{
		StandardAnalyzer analyzer = new StandardAnalyzer();
		Directory directory = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IBSimilarity similarity = new IBSimilarity(
		new DistributionSPL(),
		new LambdaDF(),
		new Normalization.NoNormalization());
		config.setSimilarity(similarity);
		IndexWriter indexWriter = new IndexWriter(directory, config);
		Document doc = new Document();
		TextField textField = new TextField("content", "",
		Field.Store.YES);
		String[] contents = {"Humpty Dumpty sat on a wall,",
			"Humpty Dumpty had a great fall.",
			"All the king's horses and all the king's men",
			"Couldn't put Humpty together again."};
		for (String content : contents) {
			textField.setStringValue(content);
			doc.removeField("content");
			doc.add(textField);
			indexWriter.addDocument(doc);		
		}
		indexWriter.commit();
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		indexSearcher.setSimilarity(similarity);
		QueryParser queryParser = new QueryParser("content", analyzer);
		Query query = queryParser.parse("humpty dumpty");
		TopDocs topDocs = indexSearcher.search(query, 100);
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			doc = indexReader.document(scoreDoc.doc);
			System.out.println(scoreDoc.score + ": " +	doc.getField("content").stringValue());
		}
	}
}
