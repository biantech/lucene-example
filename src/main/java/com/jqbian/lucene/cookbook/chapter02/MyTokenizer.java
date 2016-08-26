package com.jqbian.lucene.cookbook.chapter02;

import java.io.Reader;

import org.apache.lucene.analysis.util.CharTokenizer;
import org.apache.lucene.util.AttributeFactory;

public class MyTokenizer extends CharTokenizer{
	 public MyTokenizer(Reader input) {
	        //super(input);
	    }

	    public MyTokenizer(AttributeFactory factory, Reader input) {
	        super(factory);
	    }

	    @Override
	    protected boolean isTokenChar(int c) {
	        return !Character.isSpaceChar(c);
	    }
}
