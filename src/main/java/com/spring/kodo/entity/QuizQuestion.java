package com.spring.kodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.spring.kodo.util.enumeration.QuestionType;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity(name="quiz_question")
@Table(name="quiz_question")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class QuizQuestion
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quizQuestionId;

    @Column(nullable = false, length = 2048)
    @NotBlank(message = "Content cannot be blank")
    @Size(max = 2048)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private QuestionType questionType;

    @Column(nullable = false)
    @NotNull
    @Min(0)
    private Integer marks;

    @ManyToOne(optional = false, targetEntity = Quiz.class, fetch = FetchType.LAZY)
    private Quiz quiz;

    @OneToMany(targetEntity = QuizQuestionOption.class, fetch = FetchType.LAZY)
    @JoinColumn
    private List<QuizQuestionOption> quizQuestionOptions;

    public QuizQuestion()
    {
        this.quizQuestionOptions = new ArrayList<>();
    }

    public QuizQuestion(String content, QuestionType questionType, Integer marks)
    {
        this();

        this.content = content;
        this.questionType = questionType;
        this.marks = marks;
    }

    public Long getQuizQuestionId()
    {
        return quizQuestionId;
    }

    public void setQuizQuestionId(Long quizQuestionId)
    {
        this.quizQuestionId = quizQuestionId;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public QuestionType getQuestionType()
    {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType)
    {
        this.questionType = questionType;
    }

    public Integer getMarks()
    {
        return marks;
    }

    public void setMarks(Integer marks)
    {
        this.marks = marks;
    }

    public Quiz getQuiz()
    {
        return quiz;
    }

    public void setQuiz(Quiz quiz)
    {
        this.quiz = quiz;
    }

    public List<QuizQuestionOption> getQuizQuestionOptions()
    {
        return quizQuestionOptions;
    }

    public void setQuizQuestionOptions(List<QuizQuestionOption> quizQuestionOptions)
    {
        this.quizQuestionOptions = quizQuestionOptions;
    }

    @Override
    public String toString()
    {
        return "QuizQuestion{" +
                "quizQuestionId=" + quizQuestionId +
                ", content='" + content + '\'' +
                ", questionType=" + questionType +
                ", marks=" + marks +
                ", quiz=" + quiz +
                ", quizQuestionOptions=" + quizQuestionOptions +
                '}';
    }
}
