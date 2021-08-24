package com.spring.kodo.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table
public class Lesson
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lessonId;

    @Column(nullable = false, length = 128)
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 128)
    private String name;

    @Column(nullable = false, length = 512)
    @NotBlank(message = "Description cannot be blank")
    @Size(max = 512)
    private String description;

    @Column(nullable = false)
    @NotNull
    @Size(min = 1)
    private int sequence;

    public Lesson()
    {
    }

}
