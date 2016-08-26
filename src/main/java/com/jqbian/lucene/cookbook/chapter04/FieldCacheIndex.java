package com.jqbian.lucene.cookbook.chapter04;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.BinaryDocValues;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.LeafReader;
import org.apache.lucene.index.SortedDocValues;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.junit.Test;


public class FieldCacheIndex {
	
	@Test
	public void testFieldCache() throws IOException{
		StandardAnalyzer analyzer = new StandardAnalyzer();
		Directory directory = new RAMDirectory();
		IndexWriterConfig config =new IndexWriterConfig(analyzer);
		IndexWriter indexWriter = new IndexWriter(directory, config);
		Document doc = new Document();
		StringField stringField = new StringField("name", "", Field.Store.YES);
		String[] contents = {"alpha", "bravo", "charlie","delta", "echo", "foxtrot"};
		for (String content : contents) {
			stringField.setStringValue(content);
			doc.removeField("name");
			doc.add(stringField);
			indexWriter.addDocument(doc);
		}
		indexWriter.commit();
		IndexReader indexReader = DirectoryReader.open(directory);
		//LeafReader atomicReader=indexReader.leaves().get(0).reader();
		//BinaryDocValues cache = FieldCache.DEFAULT.getTerms(SlowCompositeReaderWrapper.wrap(indexReader), "name", true);
		//BinaryDocValues cache = atomicReader.getBinaryDocValues("name");
		for(int i = 0; i < indexReader.maxDoc(); i++) {
			LeafReader atomicReader=indexReader.leaves().get(i).reader();
			BinaryDocValues cache = atomicReader.getBinaryDocValues("name");
			SortedDocValues sortedDocValues=atomicReader.getSortedDocValues("name");
			//BytesRef bytesRef = cache.get(i);
			//System.out.println(i + ": " + bytesRef.utf8ToString());
			System.out.println("sortedDocValues"+sortedDocValues);
		}
	}
	
}
