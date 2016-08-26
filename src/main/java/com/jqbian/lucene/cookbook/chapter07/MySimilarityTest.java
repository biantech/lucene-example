package com.jqbian.lucene.cookbook.chapter07;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.payloads.PayloadHelper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.payloads.AveragePayloadFunction;
import org.apache.lucene.queries.payloads.PayloadScoreQuery;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

public class MySimilarityTest {
	
	public void prepareData(Analyzer analyzer,Directory directory,ClassicSimilarity similarity) throws IOException{
		IndexWriterConfig config = new IndexWriterConfig(analyzer);		
		config.setSimilarity(similarity);
		IndexWriter indexWriter = new IndexWriter(directory, config);
		Document doc = new Document();
		TextField textField = new TextField("content", "", Field.Store.YES);
		String[] contents = {"Humpty Dumpty sat on a wall,","Humpty Dumpty had a great fall.",
							 "All the king's horses and all the king's men","Couldn't put Humpty together again."};
		for (String content : contents) {
			textField.setStringValue(content);
			doc.removeField("content");
			doc.add(textField);
			indexWriter.addDocument(doc);
		}
		indexWriter.commit();

	}
	
	@Test
	public void testMySimilarity() throws IOException, ParseException{
		Analyzer analyzer = new StandardAnalyzer();
		Directory directory = new RAMDirectory();
		MySimilarity similarity = new MySimilarity();
		prepareData(analyzer,directory,similarity);		
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		indexSearcher.setSimilarity(similarity);	
		QueryParser queryParser = new QueryParser("content", analyzer);
		Query query = queryParser.parse("humpty dumpty");
		TopDocs topDocs = indexSearcher.search(query, 100);
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc = indexReader.document(scoreDoc.doc);
			System.out.println(scoreDoc.score + ": " +	doc.getField("content").stringValue());
		}		
	}
	
	@Test
	public void testPayload() throws IOException{
		Analyzer analyzer = new StandardAnalyzer();
		Directory directory = new RAMDirectory();
		MySimilarity similarity = new MySimilarity();
		prepareData(analyzer,directory,similarity);		
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		indexSearcher.setSimilarity(similarity);	
		//BooleanQuery query = new BooleanQuery.Builder().build();		
		SpanTermQuery spanQuery =new SpanTermQuery(new Term("content", "humpty"));
		AveragePayloadFunction payLoadFunction = new AveragePayloadFunction(){
			@Override
			public float currentScore(int docId, String field, int start, int end, int numPayloadsSeen, float currentScore, float currentPayloadScore) {
			    return currentPayloadScore + currentScore;
				/*float val = PayloadHelper.decodeFloat(payload.bytes);
				if (start == 0 || start == 1) {
					return currentPayloadScore * 0.1f;
				}
				return currentPayloadScore * 100f;
				*/
			}
			@Override
			public float docScore(int docId, String field, int numPayloadsSeen, float payloadScore) {
			    //return numPayloadsSeen > 0 ? (payloadScore / numPayloadsSeen) : 1;
				//System.out.println("field="+field);
				return 10;
			}
		};
		PayloadScoreQuery palyLoadQuery=new PayloadScoreQuery(spanQuery,payLoadFunction, true);
		BooleanQuery query =new BooleanQuery.Builder().add(palyLoadQuery, BooleanClause.Occur.SHOULD).build();
		//query.add(palyLoad,BooleanClause.Occur.SHOULD);
		TopDocs topDocs = indexSearcher.search(query, 100);
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc = indexReader.document(scoreDoc.doc);
			System.out.println(scoreDoc.score + ": " +	doc.getField("content").stringValue());
		}	
	}

	@Test
	public void testQueryParser() throws IOException, ParseException{
		Analyzer analyzer = new StandardAnalyzer();
		Directory directory = new RAMDirectory();
		MySimilarity similarity = new MySimilarity();
		prepareData(analyzer,directory,similarity);		
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		indexSearcher.setSimilarity(similarity);	
		//PhraseQuery phraseQuery = new PhraseQuery.Builder().add(new Term("content", "humpty")).add(new Term("content", "together")).build();
		//QueryParser queryParser = new QueryParser(phraseQuery);
		QueryParser queryParser = new QueryParser("content", analyzer);
		queryParser.setPhraseSlop(1);
		Query query = queryParser.parse("\"humpty dumpty\"");
		TopDocs topDocs = indexSearcher.search(query, 100);
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc = indexReader.document(scoreDoc.doc);
			System.out.println(scoreDoc.score + ": " +	doc.getField("content").stringValue());
		}	
		
	}
}
