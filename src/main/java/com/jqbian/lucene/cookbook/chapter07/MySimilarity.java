package com.jqbian.lucene.cookbook.chapter07;

import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.util.BytesRef;

public class MySimilarity extends ClassicSimilarity{

	@Override
	public float coord(int overlap, int maxOverlap) {
		if (overlap > 1) {
			return overlap / maxOverlap;
		} else {
			return 10;
		}
		//return super.coord(overlap, maxOverlap);
	}
	@Override
	public float idf(long docFreq, long numDocs) {
		//return super.idf(docFreq, numDocs);
		if (docFreq > 2) {
		   return super.idf(docFreq, numDocs);
		} else {
		   return super.idf(docFreq * 100, numDocs);
		}
	}
	
	@Override
	public float lengthNorm(FieldInvertState state) {
		//return super.lengthNorm(state);
		if(state.getLength() % 2 == 1){
			return super.lengthNorm(state) * 100;
		}
		return super.lengthNorm(state);
	}
	@Override
	public float queryNorm(float sumOfSquaredWeights) {
		//return super.queryNorm(sumOfSquaredWeights);
		if(Math.round(sumOfSquaredWeights * 100f) % 2 == 0) {
			return super.queryNorm(sumOfSquaredWeights) * 100;
		}
		return super.queryNorm(sumOfSquaredWeights);
	}
	@Override
	public float scorePayload(int doc, int start, int end,BytesRef payload) {
		return super.scorePayload(doc, start, end, payload);
	}
	@Override
	public float sloppyFreq(int distance) {
		return super.sloppyFreq(distance);
	}
	@Override
	public float tf(float freq) {
		return super.tf(freq);
	}
}
