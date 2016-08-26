package com.jqbian.lucene.cookbook.chapter02;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.junit.Test;

public class PerFieldAnalyzerWrapperTest {
	
	
	@Test
	public void TestMethod(){
	    Map<String,Analyzer> analyzerPerField = new HashMap<String,Analyzer>();
	    analyzerPerField.put("myfield", new WhitespaceAnalyzer());
	    PerFieldAnalyzerWrapper defanalyzer = new PerFieldAnalyzerWrapper(new StandardAnalyzer(), analyzerPerField);
	    TokenStream ts = null;
	    OffsetAttribute offsetAtt = null;
	    CharTermAttribute charAtt = null;
	    try {
	        ts = defanalyzer.tokenStream("myfield", new StringReader("lucene.apache.org AB-978"));
	        offsetAtt = ts.addAttribute(OffsetAttribute.class);
	        charAtt = ts.addAttribute(CharTermAttribute.class);
	        ts.reset();
	        System.out.println("== Processing field 'myfield' using WhitespaceAnalyzer (per field) ==");
	        while (ts.incrementToken()) {
	        System.out.println(charAtt.toString());
	        System.out.println("token start offset: " + offsetAtt.startOffset());
	        System.out.println("  token end offset: " + offsetAtt.endOffset());
	        }
	        ts.end();
	        ts = defanalyzer.tokenStream("content", new StringReader("lucene.apache.org AB-978"));
	        offsetAtt = ts.addAttribute(OffsetAttribute.class);
	        charAtt = ts.addAttribute(CharTermAttribute.class);
	        ts.reset();
	        System.out.println("== Processing field 'content' using StandardAnalyzer ==");
	        while (ts.incrementToken()) {
	        System.out.println(charAtt.toString());
	        System.out.println("token start offset: " + offsetAtt.startOffset());
	        System.out.println("  token end offset: " + offsetAtt.endOffset());
	        }
	        ts.end();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    finally {
	        try {
				ts.close();
			} catch (IOException e) {				
				e.printStackTrace();
			}
	    }		
	}
}
