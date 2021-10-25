package com.spring.kodo.entity.rest.request;

import com.spring.kodo.entity.Lesson;
import com.spring.kodo.entity.Quiz;

import java.util.List;

public class UpdateLessonReq
{
    private Lesson lesson;
    // Decomposing Content
    private List<Quiz> quizzes;
    private List<MultimediaReq> multimediaReqs;

    public UpdateLessonReq(Lesson lesson, List<Quiz> quizzes, List<MultimediaReq> multimediaReqs) {
        this.lesson = lesson;
        this.quizzes = quizzes;
        this.multimediaReqs = multimediaReqs;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }

    public List<MultimediaReq> getMultimediaReqs() {
        return multimediaReqs;
    }

    public void setMultimediaReqs(List<MultimediaReq> multimediaReqs) {
        this.multimediaReqs = multimediaReqs;
    }
}
