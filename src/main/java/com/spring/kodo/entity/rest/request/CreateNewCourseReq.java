package com.spring.kodo.entity.rest.request;

import java.math.BigDecimal;
import java.util.List;

public class CreateNewCourseReq
{
    private String name;
    private String description;
    private BigDecimal price;
    private Long tutorId;
    private List<String> tagTitles;

    public CreateNewCourseReq()
    {
    }

    public CreateNewCourseReq(String name, String description, BigDecimal price, Long tutorId, List<String> tagTitles)
    {
        this.name = name;
        this.description = description;
        this.price = price;
        this.tutorId = tutorId;
        this.tagTitles = tagTitles;
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

    public Long getTutorId()
    {
        return tutorId;
    }

    public void setTutorId(Long tutorId)
    {
        this.tutorId = tutorId;
    }

    public List<String> getTagTitles()
    {
        return tagTitles;
    }

    public void setTagTitles(List<String> tagTitles)
    {
        this.tagTitles = tagTitles;
    }
}
