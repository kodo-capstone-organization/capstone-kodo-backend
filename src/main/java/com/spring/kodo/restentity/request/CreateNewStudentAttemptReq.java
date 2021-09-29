package com.spring.kodo.restentity.request;

import java.util.List;

public class CreateNewStudentAttemptReq
{
    private Long enrolledContentId;
    private List<List<Long[]>> quizQuestionOptionIdLists;

    public CreateNewStudentAttemptReq()
    {
    }

    public CreateNewStudentAttemptReq(Long enrolledContentId, List<List<Long[]>> quizQuestionOptionIdLists)
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

    public List<List<Long[]>> getQuizQuestionOptionIdLists()
    {
        return quizQuestionOptionIdLists;
    }

    public void setQuizQuestionOptionIdLists(List<List<Long[]>> quizQuestionOptionIdLists)
    {
        this.quizQuestionOptionIdLists = quizQuestionOptionIdLists;
    }
}
