package com.jqbian.lucene.example.backward;

import java.io.IOException;
import java.util.Set;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.Weight;
import org.apache.lucene.util.Bits;

// Query that assigns a lot of points for a term occuring backwards,
// but some points for a term occuring forwards
public class BackwardsTermQuery extends Query {
	
	TermQuery backwardsQuery;
	TermQuery forwardsQuery;

	public BackwardsTermQuery(String field, String term) {
		// A wrapped TermQuery for the reverse string
		Term backwardsTerm = new Term(field, new StringBuilder(term).reverse().toString());
		backwardsQuery = new TermQuery(backwardsTerm);
		// A wrapped TermQuery for the Forward
		Term forwardsTerm = new Term(field, term);
		forwardsQuery = new TermQuery(forwardsTerm);
	}
	
	/*
	public Weight createWeight(IndexSearcher searcher) throws IOException {
		return new BackwardsWeight(searcher);
	}
	*/
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		/*
		if (!super.equals(other)) {
			return false;
		}
		*/
		if (getClass() != other .getClass()) {
			return false;
		}
		BackwardsTermQuery otherQ = (BackwardsTermQuery)(other);		
		/*
		if (otherQ.getBoost() != getBoost()) {
			return false;
		}
		*/
		return otherQ.backwardsQuery.equals(backwardsQuery) && otherQ.forwardsQuery.equals(forwardsQuery);
	}
	
	@Override
	public int hashCode() {		
		//return super.hashCode() + backwardsQuery.hashCode() + forwardsQuery.hashCode();
		return backwardsQuery.hashCode() + forwardsQuery.hashCode();
	}
	
	public class BackwardsWeight extends Weight {
		
		protected BackwardsWeight(Query query) {
			super(query);
			// TODO Auto-generated constructor stub
		}

		Weight backwardsWeight = null;
		Weight forwardsWeight = null;
		/*
		public BackwardsWeight(IndexSearcher searcher) throws IOException {
			//super(searcher);
			backwardsWeight = backwardsQuery.createWeight(searcher);
			forwardsWeight = forwardsQuery.createWeight(searcher);
		}
		*/
		@Override
		public Explanation explain(LeafReaderContext context, int doc) throws IOException {
			return null;
		}
	
		//@Override
		/*
		public Query getQuery() {
			return BackwardsTermQuery.this ;
		}
	    */
		@Override
		public float getValueForNormalization() throws IOException {
			return backwardsWeight.getValueForNormalization() + 
					forwardsWeight.getValueForNormalization();
		}
	
		@Override
		public void normalize(float norm, float topLevelBoost) {
			backwardsWeight.normalize(norm, topLevelBoost);
			forwardsWeight.normalize(norm, topLevelBoost);
		}
	
		//@Override
		public Scorer scorer(LeafReaderContext context, boolean scoreDocsInOrder,boolean topScorer, Bits acceptDocs) throws IOException {
			// TODO Auto-generated method stub
			//Scorer backwardsScorer = backwardsWeight.scorer(context, scoreDocsInOrder, topScorer, acceptDocs);
			//Scorer forwardsScorer = forwardsWeight.scorer(context, scoreDocsInOrder, topScorer, acceptDocs);
			Scorer backwardsScorer = backwardsWeight.scorer(context);
			Scorer forwardsScorer = forwardsWeight.scorer(context);
			return new BackwardsScorer(this, context, backwardsScorer, forwardsScorer);
		}

		@Override
		public void extractTerms(Set<Term> arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Scorer scorer(LeafReaderContext arg0) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	public class BackwardsScorer extends Scorer {

		final float BACKWARDS_SCORE = 5.0f;
		final float FORWARDS_SCORE = 1.0f;
		float currScore = 0.0f;
		
		Scorer backwardsScorer = null;
		Scorer forwardsScorer = null;
		
		protected BackwardsScorer(Weight weight, LeafReaderContext context, Scorer _backwardsScorer, Scorer _forwardsScorer) throws IOException {
			super(weight);
			backwardsScorer = _backwardsScorer;
			forwardsScorer = _forwardsScorer;
		}

		@Override
		public float score() throws IOException {
			return currScore;
		}

		@Override
		public int freq() throws IOException {
			return 1;
		}

		@Override
		public int docID() {
			int backwordsDocId = backwardsScorer.docID();
			int forwardsDocId = forwardsScorer.docID();
			
			if (backwordsDocId <= forwardsDocId) {
				currScore = BACKWARDS_SCORE;
				return backwordsDocId;
			} else if (backwordsDocId>forwardsDocId ) {
				currScore = FORWARDS_SCORE;
				return forwardsDocId;
			}
			return backwordsDocId;
			//return NO_MORE_DOCS;
		}	


		public int nextDoc() throws IOException {
			int currDocId = docID();
			// increment one or both
			if (currDocId == backwardsScorer.docID()) {
				//backwardsScorer.nextDoc();
				//backwardsScorer.
			}
			if (currDocId == forwardsScorer.docID()) {
				//forwardsScorer.nextDoc();
			}
			return docID();
		}

		
		public int advance(int target) throws IOException {			
			//backwardsScorer.advance(target);
			//forwardsScorer.advance(target);
			return docID();
		}

		//@Override
		public long cost() {
			return 1;
		}

		@Override
		public DocIdSetIterator iterator() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

	@Override
	public String toString(String field) {
		return "BackwardsQuery: " + backwardsQuery.toString();
	}

}
