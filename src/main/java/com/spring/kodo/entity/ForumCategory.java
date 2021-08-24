package com.spring.kodo.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class ForumCategory
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long forumCategoryId;

    @Column(nullable = false, length = 128)
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 128)
    private String name;

    @Column(nullable = false, length = 256)
    @NotBlank(message = "Description cannot be blank")
    @Size(max = 256)
    private String description;

    @OneToMany(targetEntity = ForumThread.class, fetch = FetchType.LAZY)
    private List<ForumThread> forumThreads;

    public ForumCategory()
    {
        this.forumThreads = new ArrayList<>();
    }
}
