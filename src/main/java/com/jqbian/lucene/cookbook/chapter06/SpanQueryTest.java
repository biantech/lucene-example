package com.jqbian.lucene.cookbook.chapter06;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.spans.FieldMaskingSpanQuery;
import org.apache.lucene.search.spans.SpanFirstQuery;
import org.apache.lucene.search.spans.SpanMultiTermQueryWrapper;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanNotQuery;
import org.apache.lucene.search.spans.SpanOrQuery;
import org.apache.lucene.search.spans.SpanPositionRangeQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.QueryBuilder;
import org.junit.Test;

public class SpanQueryTest extends BaseQueryTerm{
	@Test
	public void testSpanNearQuery() throws IOException{
		Analyzer analyzer = new StandardAnalyzer();		
		Directory directory = new RAMDirectory();
		this.prepareData(directory);
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		//Query query = new TermQuery(new Term("content", "humpty"));
		//BooleanQuery bquery= new BooleanQuery();		
		TermRangeQuery termRange = TermRangeQuery.newStringRange("name", "A", "G", true, true);		
		Query query = new BooleanQuery.Builder().add(new QueryBuilder(analyzer).createBooleanQuery("content", "humpty"),BooleanClause.Occur.MUST).add(termRange, BooleanClause.Occur.MUST).build();
		//new TermQuery(new Term("content", "humpty")).
		//TermRangeFilter termRangeFilter =			TermRangeFilter.newStringRange("name", "A", "G", true, true);
		SpanNearQuery query1 = new SpanNearQuery(new SpanQuery[] {
				new SpanTermQuery(new Term("content", "wall")),
				new SpanTermQuery(new Term("content", "sat2")),
				new SpanTermQuery(new Term("content", "humpty")),
				},
				4,
				false);
		TopDocs topDocs = indexSearcher.search(query1, 100);
		//indexSearcher.search
		//System.out.println("Searching 'humpty'");
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc = indexReader.document(scoreDoc.doc);
			if(doc!=null){
				System.out.println("name: " +	doc.getField("name").stringValue() +" - content: " +doc.getField("content").stringValue() +	" - num: " +doc.getField("num").stringValue());
			}
		}
		indexReader.close();
	}
	
	@Test
	public void testSpanFirstQuery() throws IOException{
		Analyzer analyzer = new StandardAnalyzer();		
		Directory directory = new RAMDirectory();
		this.prepareData(directory);
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		//Query query = new TermQuery(new Term("content", "humpty"));
		//BooleanQuery bquery= new BooleanQuery();		
		TermRangeQuery termRange = TermRangeQuery.newStringRange("name", "A", "G", true, true);		
		Query query = new BooleanQuery.Builder().add(new QueryBuilder(analyzer).createBooleanQuery("content", "humpty"),BooleanClause.Occur.MUST).add(termRange, BooleanClause.Occur.MUST).build();
		//new TermQuery(new Term("content", "humpty")).
		//TermRangeFilter termRangeFilter =			TermRangeFilter.newStringRange("name", "A", "G", true, true);
		SpanFirstQuery query2 = new SpanFirstQuery(new SpanTermQuery(new Term("content", "all")),3);
		TopDocs topDocs = indexSearcher.search(query2, 100);
		//indexSearcher.search
		//System.out.println("Searching 'humpty'");
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc = indexReader.document(scoreDoc.doc);
			if(doc!=null){
				System.out.println("name: " +	doc.getField("name").stringValue() +" - content: " +doc.getField("content").stringValue() +	" - num: " +doc.getField("num").stringValue());
			}
		}
		indexReader.close();
	}

	@Test
	public void testSpanNotQuery() throws IOException{
		Analyzer analyzer = new StandardAnalyzer();		
		Directory directory = new RAMDirectory();
		this.prepareData(directory);
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		//Query query = new TermQuery(new Term("content", "humpty"));
		//BooleanQuery bquery= new BooleanQuery();		
		TermRangeQuery termRange = TermRangeQuery.newStringRange("name", "A", "G", true, true);		
		Query query = new BooleanQuery.Builder().add(new QueryBuilder(analyzer).createBooleanQuery("content", "humpty"),BooleanClause.Occur.MUST).add(termRange, BooleanClause.Occur.MUST).build();
		//new TermQuery(new Term("content", "humpty")).
		//TermRangeFilter termRangeFilter =			TermRangeFilter.newStringRange("name", "A", "G", true, true);
		//SpanNotQuery query2 = new SpanNotQuery(new SpanTermQuery(new Term("content", "all")),3);
		SpanNearQuery query1 = new SpanNearQuery(
				new SpanQuery[] {
				new SpanTermQuery(new Term("content", "wall")),
				new SpanTermQuery(new Term("content", "humpty")),
				},
				4,
				false);
		SpanNotQuery query3 = new SpanNotQuery(query1,
				new SpanTermQuery(new Term("content", "sat"))
				);
		TopDocs topDocs = indexSearcher.search(query3, 100);
		//indexSearcher.search
		//System.out.println("Searching 'humpty'");
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc = indexReader.document(scoreDoc.doc);
			if(doc!=null){
				System.out.println("name: " +	doc.getField("name").stringValue() +" - content: " +doc.getField("content").stringValue() +	" - num: " +doc.getField("num").stringValue());
			}
		}
		indexReader.close();
	}

	
	@Test
	public void testSpanOrQuery() throws IOException{
		Analyzer analyzer = new StandardAnalyzer();		
		Directory directory = new RAMDirectory();
		this.prepareData(directory);
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		//Query query = new TermQuery(new Term("content", "humpty"));
		//BooleanQuery bquery= new BooleanQuery();		
		TermRangeQuery termRange = TermRangeQuery.newStringRange("name", "A", "G", true, true);		
		Query query = new BooleanQuery.Builder().add(new QueryBuilder(analyzer).createBooleanQuery("content", "humpty"),BooleanClause.Occur.MUST).add(termRange, BooleanClause.Occur.MUST).build();
		//new TermQuery(new Term("content", "humpty")).
		//TermRangeFilter termRangeFilter =			TermRangeFilter.newStringRange("name", "A", "G", true, true);
		//SpanNotQuery query2 = new SpanNotQuery(new SpanTermQuery(new Term("content", "all")),3);
		SpanNearQuery query1 = new SpanNearQuery(
				new SpanQuery[] {
				new SpanTermQuery(new Term("content", "wall")),
				new SpanTermQuery(new Term("content", "humpty")),
				},
				4,
				false);
		SpanOrQuery query4 = new SpanOrQuery(
				query1,
				new SpanTermQuery(new Term("content", "together"))
				);
		TopDocs topDocs = indexSearcher.search(query4, 100);
		//indexSearcher.search
		//System.out.println("Searching 'humpty'");
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc = indexReader.document(scoreDoc.doc);
			if(doc!=null){
				System.out.println("name: " +	doc.getField("name").stringValue() +" - content: " +doc.getField("content").stringValue() +	" - num: " +doc.getField("num").stringValue());
			}
		}
		indexReader.close();
	}
	
	@Test
	public void testWildcardQuery() throws IOException{
		Analyzer analyzer = new StandardAnalyzer();		
		Directory directory = new RAMDirectory();
		this.prepareData(directory);
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		WildcardQuery wildcard = new WildcardQuery(new Term("content","hum*"));
		SpanQuery query5 = new SpanMultiTermQueryWrapper<WildcardQuery>(wildcard);
		TopDocs topDocs = indexSearcher.search(query5, 100);
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc = indexReader.document(scoreDoc.doc);
			if(doc!=null){
				System.out.println("name: " +	doc.getField("name").stringValue() +" - content: " +doc.getField("content").stringValue() +	" - num: " +doc.getField("num").stringValue());
			}
		}
		indexReader.close();
	}

	@Test
	public void testMaskedQuery() throws IOException{
		Analyzer analyzer = new StandardAnalyzer();		
		Directory directory = new RAMDirectory();
		this.prepareData(directory);
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		SpanQuery q1 = new SpanTermQuery(new Term("content", "dumpty"));
		SpanQuery q2 = new SpanTermQuery(new Term("content2", "humpty"));
		SpanQuery maskedQuery = new FieldMaskingSpanQuery(q2, "content");
		Query query6 = new SpanNearQuery(new SpanQuery[]{q1, maskedQuery}, 4,false);		
		TopDocs topDocs = indexSearcher.search(query6, 100);
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc = indexReader.document(scoreDoc.doc);
			if(doc!=null){
				System.out.println("name: " +	doc.getField("name").stringValue() +" - content: " +doc.getField("content").stringValue() +	" - num: " +doc.getField("num").stringValue());
			}
		}
		indexReader.close();
	}

	@Test
	public void testSpanPositionRangeQuery() throws IOException{
		Analyzer analyzer = new StandardAnalyzer();		
		Directory directory = new RAMDirectory();
		this.prepareData(directory);
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		SpanPositionRangeQuery query7 = new SpanPositionRangeQuery(new SpanTermQuery(new Term("content", "wall")), 5, 6);	
		TopDocs topDocs = indexSearcher.search(query7, 100);
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc = indexReader.document(scoreDoc.doc);
			if(doc!=null){
				System.out.println("name: " +	doc.getField("name").stringValue() +" - content: " +doc.getField("content").stringValue() +	" - num: " +doc.getField("num").stringValue());
			}
		}
		indexReader.close();
	}
	
}
