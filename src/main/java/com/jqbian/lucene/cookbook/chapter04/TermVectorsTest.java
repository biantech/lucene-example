package com.jqbian.lucene.cookbook.chapter04;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.junit.Test;

public class TermVectorsTest {

	@Test
	public void testTermVectors() throws IOException {
		StandardAnalyzer analyzer = new StandardAnalyzer();
		Directory directory = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter indexWriter = new IndexWriter(directory, config);
		FieldType textFieldType = new FieldType();
		IndexOptions options = IndexOptions.DOCS_AND_FREQS;
		// textFieldType.setIndexed(options);
		textFieldType.setIndexOptions(options);
		textFieldType.setTokenized(true);
		textFieldType.setStored(true);
		textFieldType.setStoreTermVectors(true);
		textFieldType.setStoreTermVectorPositions(true);
		textFieldType.setStoreTermVectorOffsets(true);
		Document doc = new Document();
		Field textField = new Field("content", "", textFieldType);
		String[] contents = { "Humpty Dumpty sat on a wall,", "Humpty Dumpty had a great fall.",
				"All the king's horses and all the king's men", "Couldn't put Humpty together again." };

		for (String content : contents) {
			textField.setStringValue(content);
			doc.removeField("content");
			doc.add(textField);
			indexWriter.addDocument(doc);
		}
		indexWriter.commit();
		IndexReader indexReader = DirectoryReader.open(directory);
		//DocsAndPositionsEnum docsAndPositionsEnum = null;
		PostingsEnum docsAndPositionsEnum = null;
		Terms termsVector = null;
		TermsEnum termsEnum = null;
		BytesRef term = null;
		String val = null;

		for (int i = 0; i < indexReader.maxDoc(); i++) {
			termsVector = indexReader.getTermVector(i, "content");
			termsEnum = termsVector.iterator();
			//termsEnum = termsVector.iterator(termsEnum);
			//termsEnum.postings(reuse)
			while ((term = termsEnum.next()) != null) {
				val = term.utf8ToString();
				System.out.println("DocId: " + i);
				System.out.println(" term: " + val);
				System.out.println(" length: " + term.length);				
				//docsAndPositionsEnum = termsEnum.docsAndPositions(null, docsAndPositionsEnum);
				docsAndPositionsEnum = termsEnum.postings(docsAndPositionsEnum);
				if (docsAndPositionsEnum.nextDoc() >= 0) {
					int freq = docsAndPositionsEnum.freq();
					System.out.println(" freq: " + docsAndPositionsEnum.freq());
					for (int j = 0; j < freq; j++) {
						System.out.println(" [");
						System.out.println(" position: " + docsAndPositionsEnum.nextPosition());
						System.out.println(" offset start: " + docsAndPositionsEnum.startOffset());
						System.out.println(" offset end: " + docsAndPositionsEnum.endOffset());
						System.out.println(" ]");
					}
				}
			}
		}
	}
}
