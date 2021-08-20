package com.spring.kodo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import javax.persistence.*;

@Entity
@Table
public class Account
{
    @Id
    @SequenceGenerator(name = "account_sequence", sequenceName = "account_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_sequence")
    private Long accountId;
    private String username;
    @JsonProperty(access = Access.WRITE_ONLY) // Will not be read on serialization (GET request)
    private String password;
    private String name;
    private String bio;
    private String email;
    private String displayPictureUrl;
    private boolean isAdmin;

    public Account()
    {
    }

    public Account(Long accountId, String username, String password, String name, String bio, String email, String displayPictureUrl, boolean isAdmin)
    {
        this.accountId = accountId;
        this.username = username;
        this.password = password;
        this.name = name;
        this.bio = bio;
        this.email = email;
        this.displayPictureUrl = displayPictureUrl;
        this.isAdmin = isAdmin;
    }

    public Account(String username, String password, String name, String bio, String email, String displayPictureUrl, boolean isAdmin)
    {
        this.username = username;
        this.password = password;
        this.name = name;
        this.bio = bio;
        this.email = email;
        this.displayPictureUrl = displayPictureUrl;
        this.isAdmin = isAdmin;
    }

    public Long getAccountId()
    {
        return accountId;
    }

    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getBio()
    {
        return bio;
    }

    public void setBio(String bio)
    {
        this.bio = bio;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getDisplayPictureUrl()
    {
        return displayPictureUrl;
    }

    public void setDisplayPictureUrl(String displayPictureUrl)
    {
        this.displayPictureUrl = displayPictureUrl;
    }

    public boolean getIsAdmin()
    {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin)
    {
        isAdmin = isAdmin;
    }

    @Override
    public String toString()
    {
        return "Account{" +
                "accountId=" + accountId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", bio='" + bio + '\'' +
                ", email='" + email + '\'' +
                ", displayPictureUrl='" + displayPictureUrl + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }
}
