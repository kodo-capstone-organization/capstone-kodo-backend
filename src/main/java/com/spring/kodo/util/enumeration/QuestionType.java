package com.spring.kodo.util.enumeration;

public enum QuestionType
{
    MCQ("MCQ"),
    TF("TF"),
    MATCHING("MATCHING");

    private final String questionType;

    QuestionType(String questionType)
    {
        this.questionType = questionType;
    }

    @Override
    public String toString()
    {
        return this.questionType;
    }
}
