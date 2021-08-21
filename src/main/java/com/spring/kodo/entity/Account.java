package com.spring.kodo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table
public class Account
{
    @Id
    @SequenceGenerator(name = "account_sequence", sequenceName = "account_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_sequence")
    private Long accountId;

    @Column(nullable = false, unique = true, length = 32)
    @NotBlank(message = "Username cannot be blank")
    @Size(max = 32)
    private String username;

    @JsonProperty(access = Access.WRITE_ONLY) // Will not be read on serialization (GET request)
    @Column(nullable = false, length = 32)
    @NotBlank(message = "Password cannot be blank")
    @Size(max = 32)
    private String password;

    @Column(nullable = false, length = 64)
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 64)
    private String name;

    @Column(length = 512)
    @NotNull
    @Size(min = 0, max = 512)
    private String bio;

    @Column(nullable = false, unique = true, length = 64)
    @Email(message = "Email is not valid", regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    @NotEmpty(message = "Email cannot be empty")
    @Size(max = 64)
    private String email;

    @Column(nullable = true, unique = true, length = 512)
    @URL // Maybe can have a regex to help check if the URL is valid? Unsure of what constraints though
    @NotNull
    @Size(min = 0, max = 512)
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
