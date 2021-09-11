package com.spring.kodo.restentity.request;

public class GetNumberOfStudentAttemptsLeftReq {
    Long accountId;
    Long quizId;

    public GetNumberOfStudentAttemptsLeftReq() {
    }

    public GetNumberOfStudentAttemptsLeftReq(Long accountId, Long quizId) {
        this.accountId = accountId;
        this.quizId = quizId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }
}
