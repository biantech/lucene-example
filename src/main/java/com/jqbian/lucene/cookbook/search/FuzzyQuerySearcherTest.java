package com.jqbian.lucene.cookbook.search;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.junit.Test;

public class FuzzyQuerySearcherTest {
	private String path = "d:\\temp\\Lucene\\index";
	private FuzzyQuery fuzzyQuery;
	
	public void createIndex(Directory directory ) throws IOException{
		   //IndexWriter writer;
		   //Path filePaht = new Path(path)
		   SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
		   IndexWriterConfig config = new IndexWriterConfig(analyzer);
		   IndexWriter indexWriter = new IndexWriter(directory, config); // 使用ThesaurusAnalyzer 中文分析器
		    //writer = new IndexWriter(path,new StandardAnalyzer(),true);
		    Field fieldA = new TextField("contents","文件夹",Field.Store.YES); 
		    Document docA = new Document(); 
		    docA.add(fieldA);
		   
		    Field fieldB = new TextField("contents","文件名",Field.Store.YES);
		    Document docB = new Document(); 
		    docB.add(fieldB);
		   
		    Field fieldC = new TextField("contents","文件精神",Field.Store.YES);
		    Document docC = new Document(); 
		    docC.add(fieldC);
		   
		    Field fieldD = new TextField("contents","文人",Field.Store.YES); 
		    Document docD = new Document(); 
		    docD.add(fieldD);
		   
		    Field fieldE = new TextField("contents","整饬",Field.Store.YES);
		    Document docE = new Document(); 
		    docE.add(fieldE);

		    indexWriter.addDocument(docA);
		    indexWriter.addDocument(docB);
		    indexWriter.addDocument(docC);
		    indexWriter.addDocument(docD);
		    indexWriter.addDocument(docE);
		   
		    Field fieldEnglish = new TextField("contents","come",Field.Store.YES); 
		    Document docEnglish = new Document(); 
		    docEnglish.add(fieldEnglish);
		    indexWriter.addDocument(docEnglish);
		    
		    fieldEnglish = new TextField("contents","cope",Field.Store.YES);
		    docEnglish = new Document(); 
		    docEnglish.add(fieldEnglish);
		    indexWriter.addDocument(docEnglish);
		   
		    fieldEnglish = new TextField("contents","compleat",Field.Store.YES);
		    docEnglish = new Document(); 
		    docEnglish.add(fieldEnglish);
		    indexWriter.addDocument(docEnglish);
		   
		    fieldEnglish = new TextField("contents","complete",Field.Store.YES); 
		    docEnglish = new Document(); 
		    docEnglish.add(fieldEnglish);
		    indexWriter.addDocument(docEnglish);
		   
		    fieldEnglish = new TextField("contents","compile",Field.Store.YES);
		    docEnglish = new Document(); 
		    docEnglish.add(fieldEnglish);
		    indexWriter.addDocument(docEnglish);
		   
		    fieldEnglish = new TextField("contents","compiler",Field.Store.YES);
		    docEnglish = new Document(); 
		    docEnglish.add(fieldEnglish);
		    indexWriter.addDocument(docEnglish);
		    /*
		    writer.addDocument(docA);
		    writer.addDocument(docB);
		    writer.addDocument(docC);
		    writer.addDocument(docD);
		    writer.addDocument(docE);
		    writer.addDocument(docF);*/
		    indexWriter.commit();
		    indexWriter.close();		  
		}
	
	@Test
	public void testFuzzyQuery()throws IOException{
		   File file = new File(path);   
		   Directory directory = new SimpleFSDirectory(file.toPath());
		   //createIndex(directory);
		   Term term = new Term("contents","文件夹");
		   //Term term = new Term("contents","compiler");
		   FuzzyQuery query = new FuzzyQuery(term,2,51);		  
		   Date startTime = new Date();
		   IndexReader indexReader = DirectoryReader.open(directory);
		   IndexSearcher searcher = new IndexSearcher(indexReader);
		   TopDocs topDocs = searcher.search(query,10);
		   System.out.println("********************************************************************");
		   
		   for(ScoreDoc scoreDoc : topDocs.scoreDocs){
		     Document doc = indexReader.document(scoreDoc.doc);
		     System.out.println("Document的内部编号为 ： "+scoreDoc.doc);
		     System.out.println("Document内容为 ： "+doc.getField("contents").stringValue());
		     System.out.println("Document的得分为 ： "+ scoreDoc.score);
		    }
		    System.out.println("********************************************************************");
		    System.out.println("共检索出符合条件的Document "+topDocs.totalHits+" 个。");
		    Date finishTime = new Date();
		    long timeOfSearch = finishTime.getTime() - startTime.getTime();
		    System.out.println("本次搜索所用的时间为 "+timeOfSearch+" ms");

	}	
}
