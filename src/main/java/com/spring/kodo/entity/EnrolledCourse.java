package com.spring.kodo.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table
public class EnrolledCourse
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enrolledCourseId;

    @Size(min = 0, max = 5)
    private int courseRating;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Course parentCourse;

    @OneToMany(targetEntity = CompletedLesson.class, fetch = FetchType.LAZY)
    private List<CompletedLesson> completedLessons;

    public EnrolledCourse()
    {
    }
}
