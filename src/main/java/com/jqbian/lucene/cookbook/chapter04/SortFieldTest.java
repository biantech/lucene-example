package com.jqbian.lucene.cookbook.chapter04;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.junit.Test;

public class SortFieldTest{
	
	
	@Test
	public void testSortedField() throws IOException{
		StandardAnalyzer analyzer = new StandardAnalyzer();
		Directory directory = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter indexWriter = new IndexWriter(directory, config);
		Document doc = new Document();
		SortedDocValuesField docValues=new SortedDocValuesField("name", new BytesRef());
		StringField stringField = new StringField("name", "", Field.Store.YES);		
		String[] contents = {"foxtrot", "echo", "delta", "charlie", "bravo","alpha"};
		for (String content : contents) {
			//Document doc = new Document();
			//StringField stringField = new StringField("name", content, Field.Store.YES);
			doc.clear();
			stringField.setStringValue(content);
			doc.removeField("name");			
			doc.add(stringField);			
			docValues.setBytesValue(new BytesRef(content));
			doc.add(docValues);			
			indexWriter.addDocument(doc);
		}
		//doc.add(new SortedDocValuesField("name", new BytesRef("foxtrot")));
		indexWriter.commit();
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		WildcardQuery query = new WildcardQuery(new Term("name","*"));
		SortField sortField = new SortField("name", SortField.Type.STRING);
		Sort sort = new Sort(sortField);		
		TopDocs topDocs = indexSearcher.search(query, 100,sort);
		//TopDocs topDocs = indexSearcher.search(query, 100);
		for(ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc1 = indexReader.document(scoreDoc.doc);
			System.out.println(scoreDoc.score + " : " + doc1.getField("name").stringValue()+" scoreDoc.doc:"+scoreDoc.doc);
		}
	}
	
}
