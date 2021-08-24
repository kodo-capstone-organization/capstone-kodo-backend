package com.spring.kodo.entity;

import javax.persistence.*;
import java.util.Map;

@Entity
@Table
public class QuestionOptions
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionOptionsId;

    @ElementCollection
    private Map<String, String> options;

    @OneToOne(targetEntity = Question.class, optional = false)
    private Question question;

    public QuestionOptions()
    {
    }

    public QuestionOptions(Map<String, String> options, Question question)
    {
        this();

        this.options = options;
        this.question = question;
    }
}
