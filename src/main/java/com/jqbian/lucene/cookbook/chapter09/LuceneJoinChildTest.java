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
import org.apache.lucene.index.DocValuesType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.grouping.GroupDocs;
import org.apache.lucene.search.grouping.TopGroups;
import org.apache.lucene.search.join.JoinUtil;
import org.apache.lucene.search.join.QueryBitSetProducer;
import org.apache.lucene.search.join.ScoreMode;
import org.apache.lucene.search.join.ToParentBlockJoinCollector;
import org.apache.lucene.search.join.ToParentBlockJoinQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

public class LuceneJoinChildTest {
	
	@Test
	public void testJoinChildIndexTime() throws IOException{
		StandardAnalyzer analyzer = new StandardAnalyzer();
		Directory directory = new RAMDirectory();
		IndexWriterConfig config = new	IndexWriterConfig(analyzer);
		IndexWriter indexWriter = new IndexWriter(directory,config);
		List<Document> documentList = new ArrayList<Document>();
		Document childDoc1 = new Document();
		childDoc1.add(new StringField("name", "Child doc 1",Field.Store.YES));
		childDoc1.add(new StringField("type", "child",Field.Store.YES));
		childDoc1.add(new StoredField("points", 10));
		Document childDoc2 = new Document();
		childDoc2.add(new StringField("name", "Child doc 2",Field.Store.YES));
		childDoc2.add(new StringField("type", "child",Field.Store.YES));
		childDoc2.add(new StoredField("points", 100));
		Document parentDoc = new Document();
		parentDoc.add(new StringField("name", "Parent doc 1",Field.Store.YES));
		parentDoc.add(new StringField("type", "parent",	Field.Store.YES));
		parentDoc.add(new StoredField("points", 1000));
		documentList.add(childDoc1);
		documentList.add(childDoc2);
		documentList.add(parentDoc);
		indexWriter.addDocuments(documentList);
		indexWriter.commit();
		IndexReader indexReader=DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		Query childQuery = new TermQuery(new Term("type","child"));
		TermQuery parentQuery =new TermQuery(new Term("type","parent"));
		QueryBitSetProducer bitQuery = new QueryBitSetProducer(parentQuery);
		//Filter parentFilter = new	FixedBitSetCachingWrapperFilter(new QueryWrapperFilter(new TermQuery(new Term("type","parent"))));
		ToParentBlockJoinQuery toParentBlockJoinQuery = new	ToParentBlockJoinQuery(childQuery, bitQuery,ScoreMode.Max);
		ToParentBlockJoinCollector toParentBlockJoinCollector =	new ToParentBlockJoinCollector(Sort.RELEVANCE, 10,true, true);
		indexSearcher.search(toParentBlockJoinQuery,toParentBlockJoinCollector);
		TopGroups topGroups =toParentBlockJoinCollector.getTopGroupsWithAllChildDocs(toParentBlockJoinQuery, Sort.RELEVANCE, 0, 0, true);
		System.out.println("Total group count: " +	topGroups.totalGroupCount);
		System.out.println("Total hits: " +
		topGroups.totalGroupedHitCount);
		Document doc = null;
		for (GroupDocs groupDocs : topGroups.groups) {
			doc = indexSearcher.doc((Integer)groupDocs.groupValue);
			System.out.println("parent: " +	doc.getField("name").stringValue()+" type:"+doc.getField("type").stringValue()+" points:"+doc.getField("points").stringValue());
			for (ScoreDoc scoreDoc : groupDocs.scoreDocs) {
				doc = indexSearcher.doc(scoreDoc.doc);
				System.out.println(scoreDoc.score + ": " + doc.getField("name").stringValue()+" type:"+doc.getField("type").stringValue()+" points:"+doc.getField("points").stringValue());
			}
		}
	}
	
	@Test
	public void testChildJoinQueryTime() throws IOException{
		StandardAnalyzer analyzer = new StandardAnalyzer();
		Directory directory = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter indexWriter = new IndexWriter(directory, config);
		Document doc = new Document();
		doc.add(new StringField("name", "A Book", Field.Store.YES));
		doc.add(new StringField("type", "book", Field.Store.YES));
		//doc.add(new StoredField("bookAuthorId",1));
		FieldType fType= new FieldType();
		fType.setDocValuesType(DocValuesType.SORTED);
		//fType.setStored(true);
		fType.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
		doc.add(new StoredField("bookAuthorId","1",fType));
		//doc.add(new StoredField("bookAuthorId",1));
		//NumericDocValuesField field=new NumericDocValuesField("bookAuthorId",1);
		//doc.add(new NumericDocValuesField("bookAuthorId",1));
		doc.add(new StoredField("bookId", 1));
		indexWriter.addDocument(doc);
		doc = new Document();
		doc.add(new StringField("name", "An Author", Field.Store.YES));
		doc.add(new StringField("type", "author", Field.Store.YES));
		doc.add(new StoredField("authorId", 1));
		indexWriter.addDocument(doc);
		indexWriter.commit();
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		String fromField = "bookAuthorId";
		boolean multipleValuesPerDocument = false;
		String toField = "authorId";
		ScoreMode scoreMode = ScoreMode.Max;
		Query fromQuery = new TermQuery(new Term("type", "book"));
		Query joinQuery = JoinUtil.createJoinQuery(fromField,multipleValuesPerDocument,toField,fromQuery,indexSearcher,scoreMode);
		TopDocs topDocs = indexSearcher.search(joinQuery, 10);
		System.out.println("Total hits: " + topDocs.totalHits);
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			doc = indexReader.document(scoreDoc.doc);
			System.out.println(scoreDoc.score + ": " +	doc.getField("name").stringValue());
		}
	}
}
