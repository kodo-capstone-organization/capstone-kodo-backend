package com.spring.kodo.entity.rest.request;

import com.spring.kodo.entity.Quiz;
import com.spring.kodo.entity.QuizQuestion;
import com.spring.kodo.entity.QuizQuestionOption;

import java.util.List;

public class UpdateQuizReq
{
    private Quiz quiz;
    private List<QuizQuestion> quizQuestions;
    private List<List<QuizQuestionOption>> quizQuestionOptionLists;

    public UpdateQuizReq()
    {
    }

    public UpdateQuizReq(Quiz quiz, List<QuizQuestion> quizQuestions, List<List<QuizQuestionOption>> quizQuestionOptionLists)
    {
        this.quiz = quiz;
        this.quizQuestions = quizQuestions;
        this.quizQuestionOptionLists = quizQuestionOptionLists;
    }

    public Quiz getQuiz()
    {
        return quiz;
    }

    public void setQuiz(Quiz quiz)
    {
        this.quiz = quiz;
    }

    public List<QuizQuestion> getQuizQuestions()
    {
        return quizQuestions;
    }

    public void setQuizQuestions(List<QuizQuestion> quizQuestions)
    {
        this.quizQuestions = quizQuestions;
    }

    public List<List<QuizQuestionOption>> getQuizQuestionOptionLists()
    {
        return quizQuestionOptionLists;
    }

    public void setQuizQuestionOptionLists(List<List<QuizQuestionOption>> quizQuestionOptionLists)
    {
        this.quizQuestionOptionLists = quizQuestionOptionLists;
    }
}
