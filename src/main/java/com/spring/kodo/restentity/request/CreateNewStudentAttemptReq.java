package com.spring.kodo.restentity.request;

import java.util.List;

public class CreateNewStudentAttemptReq
{
    private Long enrolledContentId;
    private List<List<Integer[]>> quizQuestionOptionIdLists;

    public CreateNewStudentAttemptReq()
    {
    }

    public CreateNewStudentAttemptReq(Long enrolledContentId, List<List<Integer[]>> quizQuestionOptionIdLists)
    {
        this.enrolledContentId = enrolledContentId;
        this.quizQuestionOptionIdLists = quizQuestionOptionIdLists;
    }

    public Long getEnrolledContentId()
    {
        return enrolledContentId;
    }

    public void setEnrolledContentId(Long enrolledContentId)
    {
        this.enrolledContentId = enrolledContentId;
    }

    public List<List<Integer[]>> getQuizQuestionOptionIdLists()
    {
        return quizQuestionOptionIdLists;
    }

    public void setQuizQuestionOptionIdLists(List<List<Integer[]>> quizQuestionOptionIdLists)
    {
        this.quizQuestionOptionIdLists = quizQuestionOptionIdLists;
    }
}
