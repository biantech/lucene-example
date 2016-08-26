package com.jqbian.lucene.cookbook.chapter07;

import java.io.Reader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.util.StopwordAnalyzerBase;



public class PayloadAnalyzer extends StopwordAnalyzerBase{

	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		
		return null;
	}
	protected TokenStreamComponents createComponents(final String fieldName, final Reader reader) {
		
		//final StandardTokenizer source = new StandardTokenizer(reader);
		final StandardTokenizer source = new StandardTokenizer();
		TokenStream filter = new StandardFilter(source);
		filter = new LowerCaseFilter(filter);
		filter = new StopFilter(filter, stopwords);
		filter = new PayloadFilter(filter);
		return new TokenStreamComponents(source, filter);
	}
}
