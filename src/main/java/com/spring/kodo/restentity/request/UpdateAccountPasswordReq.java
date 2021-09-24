package com.spring.kodo.restentity.request;

public class UpdateAccountPasswordReq
{
    private Long accountId;
    private String username;
    private String oldPassword;
    private String newPassword;

    public UpdateAccountPasswordReq()
    {
    }

    public UpdateAccountPasswordReq(Long accountId, String username, String oldPassword, String newPassword)
    {
        this.accountId = accountId;
        this.username = username;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
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

    public String getOldPassword()
    {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword)
    {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword()
    {
        return newPassword;
    }

    public void setNewPassword(String newPassword)
    {
        this.newPassword = newPassword;
    }
}
