package com.jqbian.lucene.example.backward;

import java.io.IOException;

import org.apache.lucene.index.*;
import org.apache.lucene.index.Terms;
import org.apache.lucene.queries.CustomScoreProvider;

public class BackwardsScoreProvider extends CustomScoreProvider {
	
	  public BackwardsScoreProvider(LeafReaderContext context) {
		super(context);
	}

	public float customScore(int doc, float subQueryScore, float valSrcScore) throws IOException {
		// consult context 
		Terms tv = context.reader().getTermVector(doc, "tag");
		return 0.0f;
	}

}
