package com.jqbian.lucene.cookbook.chapter02;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LetterTokenizer;

public class CourtesyTitleAnalyzer extends Analyzer{

	/*
	@Override
	protected TokenStreamComponents createComponents(String arg0, Reader reader) {
        Tokenizer letterTokenizer = new LetterTokenizer(reader);
        TokenStream filter = new CourtesyTitleFilter(letterTokenizer);
        return new TokenStreamComponents(letterTokenizer, filter);
	}
    */
	@Override
	protected TokenStreamComponents createComponents(String arg0) {
        Tokenizer letterTokenizer = new LetterTokenizer();
        TokenStream filter = new CourtesyTitleFilter(letterTokenizer);
        return new TokenStreamComponents(letterTokenizer, filter);
	}

}
