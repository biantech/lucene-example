package com.jqbian.lucene.example.person.test;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import com.jqbian.lucene.example.person.domain.Article;

public class HelloWord {

	Path path = FileSystems.getDefault().getPath("d:/temp/testindex");
	Analyzer analyzer = new StandardAnalyzer();

	@Test
	public void createIndex() throws Exception {
		// 模拟一条刚保存到数据库中的数据
		Article article = new Article();
		article.setId("1");
		article.setTitle("Lucene是全文检索的框架");
		article.setContent("如果信息检索系统在用户发出了检索请求后再去互联网上找答案，根本无法在有限的时间内返回结果。");

		// 转换对象
		Document doc = new Document();
		doc.add(new Field("id", article.getId(), TextField.TYPE_STORED));
		doc.add(new Field("title", article.getTitle(), TextField.TYPE_STORED));
		doc.add(new Field("content", article.getContent(), TextField.TYPE_STORED));

		// >> 2 创建索引
		Directory directory = FSDirectory.open(path);
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter iwriter = new IndexWriter(directory, config);

		iwriter.addDocument(doc);
		iwriter.close();
		directory.close();
	}

	// 搜索
	@Test
	public void search() throws Exception {
		// 搜索条件
		//String queryString = "Lucene";
		String queryString = "asdf Lucene";
		
		// 进行搜索
		Directory directory = FSDirectory.open(path);// 索引库文件所在的目录

		// >> 1把查询字符转换成queryparse对象
		QueryParser parser = new QueryParser("title", analyzer);
		Query query = parser.parse(queryString);

		// 2，查询，得到中间结果
		IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(directory));
		TopDocs topDocs = searcher.search(query, 100); // 按指定条件条询，只返回前n条结束

		System.out.println("总结果数: " + topDocs.totalHits);
		ScoreDoc[] scoreDocs = topDocs.scoreDocs; // 前n条结果的信息
		// 3，处理结果

		List<Article> list = new ArrayList<>();
		for (int i = 0; i < scoreDocs.length; i++) {
			ScoreDoc scoreDoc = scoreDocs[i]; // 得到具体一条查询信息
			// scoreDoc.score public 相关度得分
			// Document数据库的内部编号（是唯一的，由Lucene自动生成的）// A hit document's number
			int docID = scoreDoc.doc;
			Document document = searcher.doc(docID);
			
			//转换对象
			Article article = new Article();
			article.setId(document.get("id"));
			article.setTitle(document.get("title"));
			article.setContent(document.get("content"));
			
			list.add(article);
		}
		// 显示结果

		directory.close();
		
		System.out.println("总结果数量是: " + list.size());

		for (Article article : list) {
			//System.out.println("id: " + article.getId());
			//System.out.println("Title: " + article.getTitle());
			//System.out.println("Content: " + article.getContent());
			System.out.println(article);
			//System.out.println("<--------------------->");
		}

	}
}
