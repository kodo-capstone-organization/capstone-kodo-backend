package com.spring.kodo.util.enumeration;

public enum MultimediaType
{
    PDF("PDF"),
    IMAGE("IMAGE"),
    VIDEO("VIDEO");

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
