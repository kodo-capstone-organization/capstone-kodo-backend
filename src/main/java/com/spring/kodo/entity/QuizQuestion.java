package com.spring.kodo.entity;

import com.spring.kodo.util.enumeration.QuestionType;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
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
    private QuestionType type;

    @Column(nullable = false)
    @NotNull
    @Min(0)
    private Integer marks;

    @ManyToOne(targetEntity = Quiz.class, fetch = FetchType.LAZY)
    private Quiz quiz;

    @OneToMany(targetEntity = QuizQuestionOption.class, fetch = FetchType.LAZY)
    private List<QuizQuestionOption> quizQuestionOptions;

    public QuizQuestion()
    {
        this.quizQuestionOptions = new ArrayList<>();
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

    public QuestionType getType()
    {
        return type;
    }

    public void setType(QuestionType type)
    {
        this.type = type;
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
                ", type=" + type +
                ", marks=" + marks +
                ", quiz=" + quiz +
                ", quizQuestionOptions=" + quizQuestionOptions +
                '}';
    }
}
