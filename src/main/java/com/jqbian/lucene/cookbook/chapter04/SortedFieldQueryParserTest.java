package com.jqbian.lucene.cookbook.chapter04;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field;
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
import org.junit.Test;

public class SortedFieldQueryParserTest {

	@Test
	public void testSortedFieldQueryParser() throws IOException, ParseException{
		StandardAnalyzer analyzer = new StandardAnalyzer();
		Directory directory = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter indexWriter = new IndexWriter(directory, config);
		Document doc = new Document();
		TextField textField = new TextField("content", "", Field.Store.YES);
		String[] contents = {"Humpty Dumpty sat on a wall,","Humpty Dumpty had a great fall.",
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
		QueryParser queryParser = new QueryParser("content", analyzer);
		Query query = queryParser.parse("humpty dumpty");
		TopDocs topDocs = indexSearcher.search(query, 2);
		System.out.println("Total hits: " + topDocs.totalHits);
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document doc1 = indexReader.document(scoreDoc.doc);
			System.out.println(scoreDoc.score + ": " + doc1.getField("content").stringValue());
		}
		
	}
}
