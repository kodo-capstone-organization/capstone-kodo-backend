package com.spring.kodo.restentity;

import java.util.List;

public class CreateNewAccountReq
{
    private String username;
    private String password;
    private String name;
    private String bio;
    private String email;
    private Boolean isAdmin;
    private List<String> tagTitles;

    public CreateNewAccountReq(){}

    public CreateNewAccountReq(String username, String password, String name, String bio, String email, Boolean isAdmin, List<String> tagTitles)
    {
        this.username = username;
        this.password = password;
        this.name = name;
        this.bio = bio;
        this.email = email;
        this.isAdmin = isAdmin;
        this.tagTitles = tagTitles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public List<String> getTagTitles() {
        return tagTitles;
    }

    public void setTagTitles(List<String> tagTitles) {
        this.tagTitles = tagTitles;
    }
}
