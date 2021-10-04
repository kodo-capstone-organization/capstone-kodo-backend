package com.spring.kodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity(name="quiz_question_option")
@Table(name="quiz_question_option")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class QuizQuestionOption
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quizQuestionOptionId;

    @Column(nullable = false, length = 512)
    @NotBlank(message = "Left Content cannot be blank")
    @Size(max = 512)
    private String leftContent;

    @Column(length = 512)
    @Size(max = 512)
    private String rightContent;

    @Column(nullable = false)
    @NotNull
    private Boolean correct;

    public QuizQuestionOption()
    {
    }

    public QuizQuestionOption(String leftContent, String rightContent, Boolean correct)
    {
        this.leftContent = leftContent;
        this.rightContent = rightContent;
        this.correct = correct;
    }

    public Long getQuizQuestionOptionId()
    {
        return quizQuestionOptionId;
    }

    public void setQuizQuestionOptionId(Long quizQuestionOptionId)
    {
        this.quizQuestionOptionId = quizQuestionOptionId;
    }

    public String getLeftContent()
    {
        return leftContent;
    }

    public void setLeftContent(String leftContent)
    {
        this.leftContent = leftContent;
    }

    public String getRightContent()
    {
        return rightContent;
    }

    public void setRightContent(String rightContent)
    {
        this.rightContent = rightContent;
    }

    public Boolean getCorrect()
    {
        return correct;
    }

    public void setCorrect(Boolean correct)
    {
        this.correct = correct;
    }

    @Override
    public String toString()
    {
        return "QuizQuestionOption{" +
                "quizQuestionOptionId=" + quizQuestionOptionId +
                ", leftContent='" + leftContent + '\'' +
                ", rightContent='" + rightContent + '\'' +
                ", correct=" + correct +
                '}';
    }
}
