package com.jqbian.lucene.cookbook.chapter09;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.spell.Dictionary;
import org.apache.lucene.search.spell.LuceneDictionary;
import org.apache.lucene.search.suggest.Lookup;
import org.apache.lucene.search.suggest.analyzing.AnalyzingInfixSuggester;
import org.apache.lucene.search.suggest.analyzing.AnalyzingSuggester;
import org.apache.lucene.search.suggest.analyzing.FreeTextSuggester;
import org.apache.lucene.search.suggest.analyzing.FuzzySuggester;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

public class SuggestTest {
	
	public void prepareData(StandardAnalyzer analyzer,Directory directory) throws IOException{
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter indexWriter = new IndexWriter(directory, config);
		Document doc = new Document();
		doc.add(new StringField("content", "Humpty Dumpty sat on a wall",Field.Store.YES));
		indexWriter.addDocument(doc);
		doc = new Document();
		doc.add(new StringField("content", "Humpty Dumpty had a great fall", Field.Store.YES));
		indexWriter.addDocument(doc);
		doc = new Document();
		doc.add(new StringField("content", "All the king's horses and all the king's men", Field.Store.YES));
		indexWriter.addDocument(doc);
		doc = new Document();
		doc.add(new StringField("content", "Couldn't put Humpty together again", Field.Store.YES));
		indexWriter.addDocument(doc);
		doc = new Document();
		doc.add(new StringField("content", "Humpty Dumpty Humpty Dumpty sat on a wall2#2 ",Field.Store.YES));
		indexWriter.addDocument(doc);
		indexWriter.commit();
		indexWriter.close();
	}
	
	@Test
	public void testSuggest() throws IOException{
		StandardAnalyzer analyzer = new StandardAnalyzer();
		Directory directory = new RAMDirectory();
		prepareData(analyzer,directory);
		IndexReader indexReader = DirectoryReader.open(directory);
		Dictionary dictionary = new LuceneDictionary(indexReader,"content");
		AnalyzingSuggester analyzingSuggester = new AnalyzingSuggester(directory,"",new StandardAnalyzer());
		analyzingSuggester.build(dictionary);
		List<Lookup.LookupResult> lookupResultList =analyzingSuggester.lookup("humpty dum", false, 10);
		for (Lookup.LookupResult lookupResult : lookupResultList) {
			System.out.println(lookupResult.key + ": " +lookupResult.value);
		}
		//analyzingSuggester.
	}
	
	@Test
	public void testInfixSuggester()  throws IOException{
		StandardAnalyzer analyzer = new StandardAnalyzer();
		Directory directory = new RAMDirectory();
		prepareData(analyzer,directory);
		IndexReader indexReader = DirectoryReader.open(directory);
		Dictionary dictionary = new LuceneDictionary(indexReader,"content");
		AnalyzingInfixSuggester analyzingInfixSuggester = new	AnalyzingInfixSuggester(directory, analyzer);
		analyzingInfixSuggester.build(dictionary);
		List<Lookup.LookupResult> lookupResultList = analyzingInfixSuggester.lookup("put h", false, 10);
		for (Lookup.LookupResult lookupResult : lookupResultList) {
			System.out.println(lookupResult.key + ": " +	lookupResult.value);
		}
		analyzingInfixSuggester.close();
	}
	
	@Test
	public void testFreeTextSuggester() throws IOException{
		StandardAnalyzer analyzer = new StandardAnalyzer();
		Directory directory = new RAMDirectory();
		prepareData(analyzer,directory);
		IndexReader indexReader = DirectoryReader.open(directory);
		Dictionary dictionary = new LuceneDictionary(indexReader,"content");	
		FreeTextSuggester freeTextSuggester = new FreeTextSuggester(analyzer,analyzer, 4);
		freeTextSuggester.build(dictionary);
		List<Lookup.LookupResult> lookupResultList = freeTextSuggester.lookup("h", false, 10);
		for (Lookup.LookupResult lookupResult : lookupResultList) {
			System.out.println(lookupResult.key + ": " + lookupResult.value);
		}
	}
	
	@Test
	public void testFuzzySuggester() throws IOException{
		StandardAnalyzer analyzer = new StandardAnalyzer();
		Directory directory = new RAMDirectory();
		prepareData(analyzer,directory);
		IndexReader indexReader = DirectoryReader.open(directory);
		Dictionary dictionary = new LuceneDictionary(indexReader,"content");	
		FuzzySuggester fuzzySuggester = new FuzzySuggester(directory,"",analyzer);
		fuzzySuggester.build(dictionary);
		List<Lookup.LookupResult> lookupResultList =fuzzySuggester.lookup("hampty", false, 10);
		for (Lookup.LookupResult lookupResult : lookupResultList) {
			System.out.println(lookupResult.key + ": " +lookupResult.value);
		}		
	}
}
