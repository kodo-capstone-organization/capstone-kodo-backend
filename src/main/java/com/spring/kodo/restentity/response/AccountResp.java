package com.spring.kodo.restentity.response;

public class AccountResp
{
    private Long accountId;

    private String name;

    private String username;

    private String email;

    private Boolean isAdmin;

    private String displayPictureUrl;

    public AccountResp() {
    }

    public AccountResp(Long accountId, String name, String username, String email, Boolean isAdmin, String displayPictureUrl) {
        this.accountId = accountId;
        this.name = name;
        this.username = username;
        this.email = email;
        this.isAdmin = isAdmin;
        this.displayPictureUrl = displayPictureUrl;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getDisplayPictureUrl() {
        return displayPictureUrl;
    }

    public void setDisplayPictureUrl(String displayPictureUrl) {
        this.displayPictureUrl = displayPictureUrl;
    }

    @Override
    public String toString() {
        return "AccountResp{" +
                "accountId=" + accountId +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }
}
