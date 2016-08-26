package com.jqbian.lucene.cookbook.chapter02.impl;

import org.apache.lucene.util.AttributeImpl;
import org.apache.lucene.util.AttributeReflector;

import com.jqbian.lucene.cookbook.chapter02.GenderAttribute;

public class GenderAttributeImpl extends AttributeImpl implements GenderAttribute{
	
	private Gender gender = Gender.Undefined;

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Gender getGender() {
        return gender;
    };

    @Override
    public void clear() {
        gender = Gender.Undefined;
    }

    @Override
    public void copyTo(AttributeImpl target) {
        ((GenderAttribute) target).setGender(gender);
    }

	@Override
	public void reflectWith(AttributeReflector AttrReflector) {
		//AttrReflector.reflect(arg0, arg1, arg2);
	}
}
