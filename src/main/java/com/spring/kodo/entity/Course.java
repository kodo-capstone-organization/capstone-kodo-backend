package com.spring.kodo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "enrollment"})
public class Course
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    @Column(nullable = false, length = 128)
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 128)
    private String name;

    @Column(length = 256)
    @NotNull
    @Size(max = 256)
    private String description;

    @Column(nullable = false, precision = 11, scale = 2)
    @DecimalMin("0.00")
    @NotNull
    @Digits(integer = 9, fraction = 2)
    private BigDecimal price;

    @Column(length = 512)
    @URL
    @Size(min = 0, max = 512)
    private String bannerUrl;

    @OneToMany(targetEntity = EnrolledCourse.class, mappedBy = "parentCourse", fetch = FetchType.LAZY)
    private List<EnrolledCourse> enrollment;

    @OneToMany(targetEntity = Lesson.class, fetch = FetchType.LAZY)
    @JoinTable(
            joinColumns = @JoinColumn(name = "course_id", referencedColumnName="courseId"),
            inverseJoinColumns = @JoinColumn(name = "lesson_id", referencedColumnName = "lessonId"),
            uniqueConstraints = @UniqueConstraint(columnNames = { "course_id", "lesson_id" })
    )
    private List<Lesson> lessons;

    @ManyToMany(targetEntity = Tag.class, fetch = FetchType.LAZY)
    @JoinTable(
            joinColumns = @JoinColumn(name = "course_id", referencedColumnName="courseId"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "tagId"),
            uniqueConstraints = @UniqueConstraint(columnNames = { "course_id", "tag_id" })
    )
    private List<Tag> courseTags;

    @OneToMany(targetEntity = ForumCategory.class, fetch = FetchType.LAZY)
    @JoinColumn
    private List<ForumCategory> forumCategories;

    public Course()
    {
        this.enrollment = new ArrayList<>();
        this.lessons = new ArrayList<>();
        this.courseTags = new ArrayList<>();
        this.forumCategories = new ArrayList<>();
    }

    public Course(Long courseId, String name, String description, BigDecimal price, String bannerUrl)
    {
        this();

        this.courseId = courseId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.bannerUrl = bannerUrl;
    }

    public Course(String name, String description, BigDecimal price, String bannerUrl)
    {
        this();

        this.name = name;
        this.description = description;
        this.price = price;
        this.bannerUrl = bannerUrl;
    }

    public Long getCourseId()
    {
        return courseId;
    }

    public void setCourseId(Long courseId)
    {
        this.courseId = courseId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public BigDecimal getPrice()
    {
        return price;
    }

    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }

    public String getBannerUrl()
    {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl)
    {
        this.bannerUrl = bannerUrl;
    }

    public List<EnrolledCourse> getEnrollment()
    {
        return enrollment;
    }

    public void setEnrollment(List<EnrolledCourse> enrollment)
    {
        this.enrollment = enrollment;
    }

    public List<Lesson> getLessons()
    {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons)
    {
        this.lessons = lessons;
    }

    public List<Tag> getCourseTags()
    {
        return courseTags;
    }

    public void setCourseTags(List<Tag> courseTags)
    {
        this.courseTags = courseTags;
    }

    public List<ForumCategory> getForumCategories()
    {
        return forumCategories;
    }

    public void setForumCategories(List<ForumCategory> forumCategories)
    {
        this.forumCategories = forumCategories;
    }

    @Override
    public String toString()
    {
        return "Course{" +
                "courseId=" + courseId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", bannerUrl='" + bannerUrl + '\'' +
                ", enrollment=" + enrollment +
                ", lessons=" + lessons +
                ", courseTags=" + courseTags +
                ", forumCategories=" + forumCategories +
                '}';
    }
}
