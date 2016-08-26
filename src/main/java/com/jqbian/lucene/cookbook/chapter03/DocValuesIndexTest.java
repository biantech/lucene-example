package com.jqbian.lucene.cookbook.chapter03;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.FloatPoint;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.DocValues;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.LeafReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.SortedDocValues;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.junit.Test;

public class DocValuesIndexTest {
	
	
	@Test
	public void testDocValues() throws IOException{
		//document);
		Analyzer analyzer = new StandardAnalyzer();
		Directory directory = new RAMDirectory();
		IndexWriterConfig config = new	IndexWriterConfig(analyzer);
		IndexWriter indexWriter = new IndexWriter(directory, config);
		Document document = new Document();
		document = new Document();
		document.add(new SortedDocValuesField("sorted_string", new	BytesRef("world")));
		indexWriter.addDocument(document);
		indexWriter.commit();
		indexWriter.close();
		IndexReader reader = DirectoryReader.open(directory);
		document = reader.document(0);
		System.out.println("doc 0: " + document.toString());
		document = reader.document(1);
		System.out.println("doc 1: " + document.toString());
		for (LeafReaderContext context : reader.leaves()) {
		LeafReader atomicReader = context.reader();
		SortedDocValues sortedDocValues =
		DocValues.getSorted(atomicReader, "sorted_string");
		System.out.println("Value count: " +
		sortedDocValues.getValueCount());
		System.out.println("doc 0 sorted_string: " +
		sortedDocValues.get(0).utf8ToString());
		System.out.println("doc 1 sorted_string: " +
		sortedDocValues.get(1).utf8ToString());		 
		}
		reader.close();
	}
	
	
	public void testField(){
		
		IntPoint intField = new IntPoint("int_value", 100);
		LongPoint longField = new LongPoint("long_value", 100L);
		FloatPoint floatField = new FloatPoint("float_value", 100.0F);
		DoublePoint doubleField = new DoublePoint("double_value", 100.0D);
		//StoredField intfields = new StoredField("int_value",StoredField.TYPE.stored())
		/*
		FieldType sortedIntField = new FieldType();
		sortedIntField.setNumericType(FieldType.);
		sortedIntField.setNumericPrecisionStep(Integer.MAX_VALUE);
		sortedIntField.setStored(false);
		sortedIntField.setIndexed(true);
		IntField intFieldSorted = new IntField("int_value_sort", 100,
		sortedIntField);
		*/
		Document document = new Document();
		document.add(intField);
		document.add(longField);
		document.add(floatField);
		document.add(doubleField);
		//document.add(intFieldSorted);
	}
}
