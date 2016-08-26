package com.jqbian.lucene.cookbook.chapter02;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

public class MyStopWordFilter  extends TokenFilter{

    private CharTermAttribute charTermAtt;
    private PositionIncrementAttribute posIncrAtt;
	protected MyStopWordFilter(TokenStream input) {
		super(input);
        charTermAtt = addAttribute(CharTermAttribute.class);
        posIncrAtt = addAttribute(PositionIncrementAttribute.class);
	}

	@Override
	public boolean incrementToken() throws IOException {
	    int extraIncrement = 0;
        boolean returnValue = false;
        while (input.incrementToken()) {
            if (StopAnalyzer.ENGLISH_STOP_WORDS_SET.contains(charTermAtt.toString())) {
                extraIncrement++;// filter this word
                continue;
            }
            returnValue = true;
            break;
        }
        if(extraIncrement>0){
            posIncrAtt.setPositionIncrement(posIncrAtt.getPositionIncrement()+extraIncrement);
        }
        return returnValue;
		//return false;
	}
	
	
	
}
