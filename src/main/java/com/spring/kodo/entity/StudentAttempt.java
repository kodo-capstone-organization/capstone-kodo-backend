package com.spring.kodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name="student_attempt")
@Table(name="student_attempt")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class StudentAttempt
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentAttemptId;

    @Column(nullable = false)
    @NotNull
    private LocalDateTime dateTimeOfAttempt;

    @ManyToOne(optional = false, targetEntity = Quiz.class, fetch = FetchType.LAZY)
    private Quiz quiz;

    @OneToMany(targetEntity = StudentAttemptQuestion.class, fetch = FetchType.LAZY)
    @JoinTable(
            joinColumns = @JoinColumn(name = "student_attempt_id", referencedColumnName="studentAttemptId"),
            inverseJoinColumns = @JoinColumn(name = "student_attempt_question_id", referencedColumnName = "studentAttemptQuestionId"),
            uniqueConstraints = @UniqueConstraint(columnNames = { "student_attempt_id", "student_attempt_question_id" })
    )
    private List<StudentAttemptQuestion> studentAttemptQuestions;

    public StudentAttempt()
    {
        this.dateTimeOfAttempt = LocalDateTime.now();
        this.studentAttemptQuestions = new ArrayList<>();
    }

    public Long getStudentAttemptId()
    {
        return studentAttemptId;
    }

    public void setStudentAttemptId(Long studentAttemptId)
    {
        this.studentAttemptId = studentAttemptId;
    }

    public LocalDateTime getDateTimeOfAttempt()
    {
        return dateTimeOfAttempt;
    }

    public void setDateTimeOfAttempt(LocalDateTime dateTimeOfAttempt)
    {
        this.dateTimeOfAttempt = dateTimeOfAttempt;
    }

    public Quiz getQuiz()
    {
        return quiz;
    }

    public void setQuiz(Quiz quiz)
    {
        this.quiz = quiz;
    }

    public List<StudentAttemptQuestion> getStudentAttemptQuestions()
    {
        return studentAttemptQuestions;
    }

    public void setStudentAttemptQuestions(List<StudentAttemptQuestion> studentAttemptQuestions)
    {
        this.studentAttemptQuestions = studentAttemptQuestions;
    }

    @Override
    public String toString()
    {
        return "StudentAttempt{" +
                "studentAttemptId=" + studentAttemptId +
                ", dateTimeOfAttempt=" + dateTimeOfAttempt +
                ", quiz=" + quiz +
                ", studentAttemptQuestions=" + studentAttemptQuestions +
                '}';
    }
}
