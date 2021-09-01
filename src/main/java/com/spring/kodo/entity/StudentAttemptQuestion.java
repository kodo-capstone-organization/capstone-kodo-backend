package com.spring.kodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class StudentAttemptQuestion
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentAttemptQuestionId;

    @ManyToOne(optional = false, targetEntity = QuizQuestion.class, fetch = FetchType.LAZY)
    private QuizQuestion quizQuestion;

    @OneToMany(targetEntity = StudentAttemptAnswer.class, fetch = FetchType.LAZY)
    @JoinTable(
            joinColumns = @JoinColumn(name = "student_attempt_question_id", referencedColumnName="studentAttemptQuestionId"),
            inverseJoinColumns = @JoinColumn(name = "student_attempt_answer_id", referencedColumnName = "studentAttemptAnswerId"),
            uniqueConstraints = @UniqueConstraint(columnNames = { "student_attempt_question_id", "student_attempt_answer_id" })
    )
    private List<StudentAttemptAnswer> studentAttemptAnswers;

    public StudentAttemptQuestion()
    {
        this.studentAttemptAnswers = new ArrayList<>();
    }

    public Long getStudentAttemptQuestionId()
    {
        return studentAttemptQuestionId;
    }

    public void setStudentAttemptQuestionId(Long studentAttemptQuestionId)
    {
        this.studentAttemptQuestionId = studentAttemptQuestionId;
    }

    public QuizQuestion getQuizQuestion()
    {
        return quizQuestion;
    }

    public void setQuizQuestion(QuizQuestion quizQuestion)
    {
        this.quizQuestion = quizQuestion;
    }

    public List<StudentAttemptAnswer> getStudentAttemptAnswers()
    {
        return studentAttemptAnswers;
    }

    public void setStudentAttemptAnswers(List<StudentAttemptAnswer> studentAttemptAnswers)
    {
        this.studentAttemptAnswers = studentAttemptAnswers;
    }

    @Override
    public String toString()
    {
        return "StudentAttemptQuestion{" +
                "studentAttemptQuestionId=" + studentAttemptQuestionId +
                ", quizQuestion=" + quizQuestion +
                ", studentAttemptAnswers=" + studentAttemptAnswers +
                '}';
    }
}
