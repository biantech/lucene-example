package com.jqbian.lucene.cookbook.chapter06;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

public class PhraseQueryTest extends BaseQueryTerm{

	@Test
	public void testPhraseQuery() throws IOException{
		Analyzer analyzer = new StandardAnalyzer();		
		Directory directory = new RAMDirectory();
		this.prepareData(directory);
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		
		PhraseQuery phraseQuery = new PhraseQuery.Builder().add(new Term("content", "humpty")).add(new Term("content", "together")).build();
		
		//phraseQuery.add();
		//phraseQuery.add(new Term("content", "together"));
		
		//Query query = new TermQuery(new Term("content", "humpty"));
		//BooleanQuery bquery= new BooleanQuery();		
		//TermRangeQuery termRange = TermRangeQuery.newStringRange("name", "A", "G", true, true);		
		//Query query = new BooleanQuery.Builder().add(new QueryBuilder(analyzer).createBooleanQuery("content", "humpty"),BooleanClause.Occur.MUST).add(termRange, BooleanClause.Occur.MUST).build();
		//new TermQuery(new Term("content", "humpty")).
		//TermRangeFilter termRangeFilter =			TermRangeFilter.newStringRange("name", "A", "G", true, true);
		TopDocs topDocs = indexSearcher.search(phraseQuery, 100);
		//indexSearcher.search
		//System.out.println("Searching 'humpty'");
		
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc = indexReader.document(scoreDoc.doc);
			if(doc!=null){
				System.out.println("name: " +	doc.getField("name") +" - content: " +doc.getField("content") +	" - num: " +doc.getField("num"));
			}
		}
		indexReader.close();
	}
}
