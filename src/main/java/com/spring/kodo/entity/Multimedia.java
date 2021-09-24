package com.spring.kodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.spring.kodo.util.FormatterUtil;
import com.spring.kodo.util.enumeration.MultimediaType;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonTypeName("multimedia")
public class Multimedia extends Content
{
    @Column(length = 512)
    @URL // Maybe can have a regex to help check if the URL is valid? Unsure of what constraints though
    @NotNull
    @Size(min = 0, max = 512)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private MultimediaType multimediaType;

    // Used only for updateCourse to match file to incoming MultipartFile
    @Transient
    private String newFilename;

    public Multimedia()
    {
    }

    public Multimedia(String name, String description, String url, MultimediaType mutlimediaType)
    {
        super(name, description);
        this.url = url;
        this.multimediaType = mutlimediaType;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getUrlFilename()
    {
        return FormatterUtil.getGCSObjectNameFromMediaLink(this.url);
    }

    public String getNewFilename() {
        return newFilename;
    }

    public void setNewFilename(String newFilename) {
        this.newFilename = newFilename;
    }

    public MultimediaType getMultimediaType()
    {
        return multimediaType;
    }

    public void setMultimediaType(MultimediaType type)
    {
        this.multimediaType = type;
    }

    @Override
    public String toString()
    {
        return "Multimedia{" +
                "url='" + url + '\'' +
                ", type=" + multimediaType +
                '}';
    }
}
