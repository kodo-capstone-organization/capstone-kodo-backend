package com.spring.kodo.entity.rest.response;

import com.spring.kodo.entity.EnrolledLesson;

public class EnrolledLessonWithStudentNameResp
{
    private EnrolledLesson enrolledLesson;
    private String studentName;

    public EnrolledLessonWithStudentNameResp()
    {
    }

    public EnrolledLessonWithStudentNameResp(EnrolledLesson enrolledLesson, String studentName)
    {
        this.enrolledLesson = enrolledLesson;
        this.studentName = studentName;
    }

    public EnrolledLesson getEnrolledLesson()
    {
        return enrolledLesson;
    }

    public void setEnrolledLesson(EnrolledLesson enrolledLesson)
    {
        this.enrolledLesson = enrolledLesson;
    }

    public String getStudentName()
    {
        return studentName;
    }

    public void setStudentName(String studentName)
    {
        this.studentName = studentName;
    }
}
