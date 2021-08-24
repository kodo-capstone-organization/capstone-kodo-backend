package com.spring.kodo.entity;

import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
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

    @Column(unique = true, length = 512)
    @URL
    @Size(min = 0, max = 512)
    private String bannerUrl;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Account tutor;

    @OneToMany(targetEntity = Tag.class, fetch = FetchType.LAZY)
    private List<Tag> courseTags;

    public Course()
    {
        this.courseTags = new ArrayList<>();
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

    public Account getTutor()
    {
        return tutor;
    }

    public void setTutor(Account tutor)
    {
        this.tutor = tutor;
    }

    public List<Tag> getCourseTags()
    {
        return courseTags;
    }

    public void setCourseTags(List<Tag> courseTags)
    {
        this.courseTags = courseTags;
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
                '}';
    }
}
