package com.spring.kodo.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table
public class ForumThread
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long forumThreadId;

    @Column(nullable = false, length = 256)
    @NotBlank(message = "Description cannot be blank")
    @Size(max = 256)
    private String description;

    @Column(nullable = false)
    @NotNull
    private LocalDateTime timeStamp;


}
