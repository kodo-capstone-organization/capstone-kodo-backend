package com.spring.kodo.util.enumeration;

public enum MultimediaType
{
    IMAGE("IMAGE"),
    VIDEO("VIDEO"),
    ZIP("ZIP"),
    DOCUMENT("DOCUMENT"),
    EMPTY("EMPTY");

    private final String multimediaType;

    MultimediaType(String multimediaType)
    {
        this.multimediaType = multimediaType;
    }

    @Override
    public String toString()
    {
        return this.multimediaType;
    }
}
