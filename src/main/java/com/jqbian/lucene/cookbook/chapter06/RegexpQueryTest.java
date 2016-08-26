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
import org.apache.lucene.search.RegexpQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

public class RegexpQueryTest extends BaseQueryTerm{
	
	@Test
	public void testRegexpQuery() throws IOException{
		Analyzer analyzer = new StandardAnalyzer();		
		Directory directory = new RAMDirectory();
		this.prepareData(directory);
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		//Query query = new TermQuery(new Term("content", "humpty"));
		//BooleanQuery bquery= new BooleanQuery();
		RegexpQuery query = new RegexpQuery(new Term("content", ".together.*"));
		//TermRangeQuery termRange = TermRangeQuery.newStringRange("name", "A", "G", true, true);		
		//Query query = new BooleanQuery.Builder().add(new QueryBuilder(analyzer).createBooleanQuery("content", "humpty"),BooleanClause.Occur.MUST).add(termRange, BooleanClause.Occur.MUST).build();
		//new TermQuery(new Term("content", "humpty")).
		//TermRangeFilter termRangeFilter =			TermRangeFilter.newStringRange("name", "A", "G", true, true);
		TopDocs topDocs = indexSearcher.search(query, 100);
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
}
