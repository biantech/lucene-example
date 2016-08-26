package com.jqbian.lucene.cookbook.chapter02;

public interface GenderAttribute {
    public static enum Gender {Male, Female, Undefined};
    public void setGender(Gender gender);
    public Gender getGender();
}
