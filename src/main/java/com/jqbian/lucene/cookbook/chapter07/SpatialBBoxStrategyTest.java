package com.jqbian.lucene.cookbook.chapter07;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.spatial.bbox.BBoxStrategy;
import org.apache.lucene.spatial.query.SpatialArgs;
import org.apache.lucene.spatial.query.SpatialOperation;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Rectangle;

public class SpatialBBoxStrategyTest {

	@Test
	public void testBBoxStrategy() throws IOException {
		StandardAnalyzer analyzer = new StandardAnalyzer();
		Directory directory = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter indexWriter = new IndexWriter(directory, config);
		Document doc = new Document();
		SpatialContext spatialContext = SpatialContext.GEO;
		FieldType fType = new FieldType();
		fType.setStored(true);
		BBoxStrategy bBoxStrategy = new BBoxStrategy(spatialContext, "rectangle", fType);
		Rectangle rectangle = spatialContext.makeRectangle(1.0d, 5.0d, 1.0d, 5.0d);
		Field[] fields = bBoxStrategy.createIndexableFields(rectangle);
		for (Field field : fields) {
			doc.add(field);
		}
		doc.add(new StringField("name", "Rectangle 1", Field.Store.YES));
		indexWriter.addDocument(doc);
		indexWriter.commit();		
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		Rectangle rectangle2 = spatialContext.makeRectangle(2.0d, 4.0d, 0.0d, 4.0d);
		SpatialArgs spatialArgs = new SpatialArgs(SpatialOperation.Intersects, rectangle2);
		Query query = bBoxStrategy.makeQuery(spatialArgs);
		TopDocs topDocs = indexSearcher.search(query, 10);
		System.out.println("Total hits: " + topDocs.totalHits);
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			doc = indexReader.document(scoreDoc.doc);
			System.out.println(scoreDoc.score + ": " + doc.getField("name").stringValue());
		}
		indexWriter.close();
	}
}
