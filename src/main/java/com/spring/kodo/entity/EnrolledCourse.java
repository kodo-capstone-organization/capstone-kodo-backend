package com.spring.kodo.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table
public class EnrolledCourse
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enrolledCourseId;

    @Size(min = 0, max = 5)
    private int courseRating;

    public EnrolledCourse()
    {
    }
}
