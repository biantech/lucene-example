package com.jqbian.lucene.cookbook.chapter06;

import java.util.HashSet;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.CustomScoreProvider;
import org.apache.lucene.queries.CustomScoreQuery;
import org.apache.lucene.queries.function.FunctionQuery;
import org.apache.lucene.search.Query;

public class AnagramQuery extends CustomScoreQuery{
	private String field=null;
	private HashSet<String> terms = new HashSet<String>();
	
	public AnagramQuery(Query subQuery, FunctionQuery scoringQuery) {
		super(subQuery, scoringQuery);		
		
	}

	public AnagramQuery(Query subQuery) {
		super(subQuery);		
	}

	public AnagramQuery(Query subquery, String field) {
		super(subquery);	
		this.field = field;
		HashSet<Term> termSet = new HashSet<Term>();
		/*
		subquery.extractTerms(termSet);		
		for (Term term : termSet) {
			terms.add(term.text());
		}
		*/
	}
	
	@Override
	protected CustomScoreProvider getCustomScoreProvider(LeafReaderContext context) {
		return new AnagramQueryScoreProvider(context, field,terms);
	}
	
}
