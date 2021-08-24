package com.spring.kodo.entity;

import com.spring.kodo.util.enumeration.QuestionType;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table
public class Question
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private QuestionType type;

    @Column(nullable = false, length = 512)
    @NotBlank(message = "Question cannot be blank")
    @Size(max = 512)
    private String question;

    @Column(nullable = false)
    @NotNull
    @Min(0)
    private int mark;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Quiz quiz;

    @OneToOne(targetEntity = QuestionOptions.class, optional = false)
    @JoinColumn
    private QuestionOptions questionOptions;

    public Question()
    {
    }

    public Long getQuestionId()
    {
        return questionId;
    }

    public void setQuestionId(Long questionId)
    {
        this.questionId = questionId;
    }

    public QuestionType getType()
    {
        return type;
    }

    public void setType(QuestionType type)
    {
        this.type = type;
    }

    public String getQuestion()
    {
        return question;
    }

    public void setQuestion(String question)
    {
        this.question = question;
    }

    public int getMark()
    {
        return mark;
    }

    public void setMark(int mark)
    {
        this.mark = mark;
    }

    public Quiz getQuiz()
    {
        return quiz;
    }

    public void setQuiz(Quiz quiz)
    {
        this.quiz = quiz;
    }

    public QuestionOptions getQuestionOptions()
    {
        return questionOptions;
    }

    public void setQuestionOptions(QuestionOptions questionOptions)
    {
        this.questionOptions = questionOptions;
    }

    @Override
    public String toString()
    {
        return "Question{" +
                "questionId=" + questionId +
                ", type=" + type +
                ", question='" + question + '\'' +
                ", mark=" + mark +
                ", quiz=" + quiz +
                ", questionOptions=" + questionOptions +
                '}';
    }
}
