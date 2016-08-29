package com.jqbian.lucene.cookbook.chapter09;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.grouping.BlockGroupingCollector;
import org.apache.lucene.search.grouping.GroupDocs;
import org.apache.lucene.search.grouping.GroupingSearch;
import org.apache.lucene.search.grouping.TopGroups;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;
import org.junit.Test;

public class GroupingTest {

	@Test
	public void testBlockGroupingCollector() throws IOException{
		StandardAnalyzer analyzer = new StandardAnalyzer();
		Directory directory = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter indexWriter = new IndexWriter(directory, config);
		FieldType groupEndFieldType = new FieldType();
		groupEndFieldType.setStored(false);
		groupEndFieldType.setTokenized(false);
		groupEndFieldType.setIndexOptions(IndexOptions.DOCS);
		//groupEndFieldType.setIndexOptions(FieldInfo.);
		groupEndFieldType.setOmitNorms(true);
		Field groupEndField = new Field("groupEnd", "x",groupEndFieldType);
		List<Document> documentList = new ArrayList<Document>();
		Document doc = new Document();
		doc.add(new StringField("BookId", "B1", Field.Store.YES));
		doc.add(new StringField("Category", "Cat 1", Field.Store.YES));
		documentList.add(doc);
		doc = new Document();
		doc.add(new StringField("BookId", "B2", Field.Store.YES));
		doc.add(new StringField("Category", "Cat 1", Field.Store.YES));
		documentList.add(doc);
		doc.add(groupEndField);
		indexWriter.addDocuments(documentList);
		documentList = new ArrayList<Document>();
		doc = new Document();
		doc.add(new StringField("BookId", "B3", Field.Store.YES));
		doc.add(new StringField("Category", "Cat 2", Field.Store.YES));
		documentList.add(doc);
		doc.add(groupEndField);
		indexWriter.addDocuments(documentList);
		indexWriter.commit();
		TermQuery termQuery=new TermQuery(new Term("groupEnd", "x"));
		
		//Filter groupEndDocs = new CachingWrapperFilter(new	QueryWrapperFilter(new TermQuery(new Term("groupEnd", "x"))));
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		BlockGroupingCollector blockGroupingCollector = new	BlockGroupingCollector(Sort.RELEVANCE, 10, true, termQuery.createWeight(indexSearcher, true));
		indexSearcher.search(new MatchAllDocsQuery(), blockGroupingCollector);
		//indexSearcher.search		
		TopGroups topGroups = blockGroupingCollector.getTopGroups(Sort.RELEVANCE, 0, 0, 10,	true);
		System.out.println("Total group count: " +	topGroups.totalGroupCount);
		System.out.println("Total group hit count: " +	topGroups.totalGroupedHitCount);
		for (GroupDocs groupDocs : topGroups.groups) {
			System.out.println("Group: " + groupDocs.groupValue + " maxScore:"+ groupDocs.maxScore);
			for (ScoreDoc scoreDoc : groupDocs.scoreDocs) {
				doc = indexSearcher.doc(scoreDoc.doc);
				System.out.println("Category: " +doc.getField("Category").stringValue() + ", BookId: " +doc.getField("BookId").stringValue());
			}
		}
	}
	
	@Test
	public void testGroupingSearch() throws IOException{
		StandardAnalyzer analyzer = new StandardAnalyzer();
		Directory directory = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter indexWriter = new IndexWriter(directory, config);
		Document doc = new Document();
		doc.add(new StringField("BookId", "B1", Field.Store.YES));
		doc.add(new StringField("Category", "Cat 1", Field.Store.YES));
		doc.add(new StoredField("Category", "Cat 1"));
		//doc.add(new NumericDocValuesField("Category", new LongToDate().transferDateTolong(filebean.getModified())));
		
		indexWriter.addDocument(doc);
		doc = new Document();
		doc.add(new StringField("BookId", "B2", Field.Store.YES));
		doc.add(new StringField("Category", "Cat 1", Field.Store.YES));
		indexWriter.addDocument(doc);
		doc = new Document();
		doc.add(new StringField("BookId", "B3", Field.Store.YES));
		doc.add(new StringField("Category", "Cat 2", Field.Store.YES));
		indexWriter.addDocument(doc);
		indexWriter.commit();
		GroupingSearch groupingSearch = new GroupingSearch("Category");
		groupingSearch.setAllGroups(true);
		groupingSearch.setGroupDocsLimit(10);
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		TopGroups topGroups = groupingSearch.search(indexSearcher, new	MatchAllDocsQuery(), 0, 10);
		System.out.println("Total group count: " +	topGroups.totalGroupCount);
		System.out.println("Total group hit count: " +	topGroups.totalGroupedHitCount);
		for (GroupDocs groupDocs : topGroups.groups) {
			//System.out.println("Group: " +((BytesRef)groupDocs.groupValue).utf8ToString());
			System.out.println("Group: " +((BytesRef)groupDocs.groupValue).utf8ToString());
			for (ScoreDoc scoreDoc : groupDocs.scoreDocs) {
				doc = indexSearcher.doc(scoreDoc.doc);
				System.out.println("Category: " +	doc.getField("Category").stringValue() + ", BookId: " +	doc.getField("BookId").stringValue());
		}
		}
	}
}
