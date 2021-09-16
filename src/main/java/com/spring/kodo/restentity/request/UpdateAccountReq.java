package com.spring.kodo.restentity.request;

import com.spring.kodo.entity.Account;
import com.spring.kodo.util.exception.InputDataValidationException;

import java.util.List;

public class UpdateAccountReq
{
    private Account account;
    private String password;
    private List<String> tagTitles; // leave as null if not updating
    private List<Long> enrolledCourseIds; // leave as null if not updating
    private List<Long> courseIds; // leave as null if not updating
    private List<Long> forumThreadIds; // leave as null if not updating
    private List<Long> forumPostIds; // leave as null if not updating
    private List<Long> studentAttemptIds; // leave as null if not updating

    public UpdateAccountReq(Account account, String password, List<String> tagTitles, List<Long> enrolledCourseIds, List<Long> courseIds, List<Long> forumThreadIds, List<Long> forumPostIds, List<Long> studentAttemptIds) throws InputDataValidationException
    {
        this.account = account;
        this.password = password;
        this.tagTitles = tagTitles;
        this.enrolledCourseIds = enrolledCourseIds;
        this.courseIds = courseIds;
        this.forumThreadIds = forumThreadIds;
        this.forumPostIds = forumPostIds;
        this.studentAttemptIds = studentAttemptIds;
    }

    public Account getAccount()
    {
        return account;
    }

    public void setAccount(Account account)
    {
        this.account = account;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public List<String> getTagTitles()
    {
        return tagTitles;
    }

    public void setTagTitles(List<String> tagTitles)
    {
        this.tagTitles = tagTitles;
    }

    public List<Long> getEnrolledCourseIds()
    {
        return enrolledCourseIds;
    }

    public void setEnrolledCourseIds(List<Long> enrolledCourseIds)
    {
        this.enrolledCourseIds = enrolledCourseIds;
    }

    public List<Long> getCourseIds()
    {
        return courseIds;
    }

    public void setCourseIds(List<Long> courseIds)
    {
        this.courseIds = courseIds;
    }

    public List<Long> getForumThreadIds()
    {
        return forumThreadIds;
    }

    public void setForumThreadIds(List<Long> forumThreadIds)
    {
        this.forumThreadIds = forumThreadIds;
    }

    public List<Long> getForumPostIds()
    {
        return forumPostIds;
    }

    public void setForumPostIds(List<Long> forumPostIds)
    {
        this.forumPostIds = forumPostIds;
    }

    public List<Long> getStudentAttemptIds()
    {
        return studentAttemptIds;
    }

    public void setStudentAttemptIds(List<Long> studentAttemptIds)
    {
        this.studentAttemptIds = studentAttemptIds;
    }
}
