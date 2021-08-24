package com.spring.kodo.entity;

import com.spring.kodo.util.enumeration.MultimediaType;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Multimedia extends Content
{
    @Column(unique = true, length = 512)
    @URL // Maybe can have a regex to help check if the URL is valid? Unsure of what constraints though
    @NotNull
    @Size(min = 0, max = 512)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private MultimediaType type;

    public Multimedia()
    {
    }

    public Multimedia(String name, String description, String url, MultimediaType type)
    {
        super(name, description);
        this.url = url;
        this.type = type;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public MultimediaType getType()
    {
        return type;
    }

    public void setType(MultimediaType type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return "Multimedia{" +
                "url='" + url + '\'' +
                ", type=" + type +
                '}';
    }
}
