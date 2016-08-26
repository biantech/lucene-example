package com.jqbian.lucene.cookbook.chapter06;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;

public class BaseQueryTerm {
	protected void prepareData(Directory directory) throws IOException{
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig config = new	IndexWriterConfig(analyzer);
		IndexWriter indexWriter = new IndexWriter(directory, config);
		Document doc = new Document();
		StringField nameField = new StringField("name", "",Field.Store.YES);
		TextField textField = new TextField("content", "",	Field.Store.YES);
		StoredField intField = new StoredField("num",0);
		doc.removeField("name");
		doc.removeField("content");
		doc.removeField("num");
		nameField.setStringValue("First");
		textField.setStringValue("Humpty Dumpty sat on a wall,");
		intField.setIntValue(100);
		//intField.setLongValue(300);
		doc.add(nameField);
		doc.add(textField);
		doc.add(intField);
		//doc.add(new IntPoint("num1",100));
		//doc.add(new NumericDocValuesField("num2",100));
		indexWriter.addDocument(doc);
		doc.removeField("name"); 
		doc.removeField("content");
		doc.removeField("num");
		nameField.setStringValue("Second");
		textField.setStringValue("Humpty Dumpty had a great fall.");
		intField.setIntValue(200);
		//intField.setLongValue(300);
		doc.add(nameField); 
		doc.add(textField);
		doc.add(intField);
		//doc.add(new IntPoint("num1",200));
		//doc.add(new NumericDocValuesField("num2",200));
		indexWriter.addDocument(doc);
		doc.removeField("name"); 
		doc.removeField("content");
		doc.removeField("num");
		nameField.setStringValue("Third");
		textField.setStringValue("All the king's horses and all the king's	men");
		intField.setIntValue(300);
		//intField.setLongValue(300);
		
		doc.add(nameField); 
		doc.add(textField);
		doc.add(intField);
		//doc.add(new IntPoint("num1",300));
		//doc.add(new NumericDocValuesField("num2",300));
		indexWriter.addDocument(doc);
		doc.removeField("name"); 
		doc.removeField("content");
		doc.removeField("num");
		nameField.setStringValue("Fourth");
		textField.setStringValue("Couldn't put Humpty together	again.");
		intField.setIntValue(400);
		//intField.setLongValue(300);
		doc.add(nameField);
		doc.add(textField);
		doc.add(intField);
		//doc.add(intField);
		//doc.add(new IntPoint("num1",400));
		//doc.add(new NumericDocValuesField("num2",400));
		indexWriter.addDocument(doc);
		indexWriter.commit();
		indexWriter.close();
		//IndexReader indexReader = DirectoryReader.open(directory);
		//IndexSearcher indexSearcher = new IndexSearcher(indexReader);	
	}
}
