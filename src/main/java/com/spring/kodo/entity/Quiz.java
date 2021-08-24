package com.spring.kodo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Entity
@Table
public class Quiz extends Content
{
    @Column(nullable = false)
    @NotNull
    private LocalTime timeLimit;

    public Quiz()
    {
    }

    public Quiz(String name, String description, LocalTime timeLimit)
    {
        super(name, description);
        this.timeLimit = timeLimit;
    }
}
