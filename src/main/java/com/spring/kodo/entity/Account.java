package com.spring.kodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.spring.kodo.util.FormatterUtil;
import com.spring.kodo.util.cryptography.CryptographicHelper;
import com.spring.kodo.util.exception.InputDataValidationException;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Entity(name="account")
@Table(name="account")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "forumPosts"})
public class Account
{
    // At least 1 of 0-9 = (?=.*[0-9])
    // At least 1 of a-z = (?=.*[a-z])
    // At least 1 of A-Z = (?=.*[A-Z])
    // At least 1 of specials = (?=.*[@#$%^&+=])
    // No white spaces inbetween = (?=\S+$)
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,64}$");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(nullable = false, unique = true, length = 32)
    @NotBlank(message = "Username cannot be blank")
    @Size(max = 32)
    private String username;

    @JsonProperty(access = Access.WRITE_ONLY) // Will not be read on serialization (GET request)
    @Column(nullable = false, unique = true, length = 64)
    @NotBlank(message = "Salt cannot be blank")
    @Size(max = 64)
    private String salt;

    @JsonProperty(access = Access.WRITE_ONLY) // Will not be read on serialization (GET request)
    @Column(nullable = false, length = 64)
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 64)
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
    @NotBlank(message = "Email cannot be empty")
    @Size(max = 64)
    private String email;

    @Column(length = 512)
    @URL // Maybe can have a regex to help check if the URL is valid? Unsure of what constraints though
    @Size(min = 0, max = 512)
    private String displayPictureUrl;

    @Column(nullable = false)
    @NotNull
    private Boolean isAdmin;

    @Column(nullable = false)
    @NotNull
    private Boolean isActive;

    @Column(nullable = true, unique = true)
    private String stripeAccountId;

    @ManyToMany(targetEntity = Tag.class, fetch = FetchType.LAZY)
    @JoinTable(
            joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "accountId"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "tagId"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"account_id", "tag_id"})
    )
    private List<Tag> interests;

    @OneToMany(targetEntity = EnrolledCourse.class, fetch = FetchType.LAZY)
    @JoinTable(
            joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "accountId"),
            inverseJoinColumns = @JoinColumn(name = "enrolled_course_id", referencedColumnName = "enrolledCourseId"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"account_id", "enrolled_course_id"})
    )
    private List<EnrolledCourse> enrolledCourses;

    @OneToMany(targetEntity = Course.class, fetch = FetchType.LAZY)
    @JoinTable(
            joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "accountId"),
            inverseJoinColumns = @JoinColumn(name = "course_id", referencedColumnName = "courseId"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"account_id", "course_id"})
    )
    private List<Course> courses;

    public Account()
    {
        this.salt = CryptographicHelper.generateRandomString(64);
        this.enrolledCourses = new ArrayList<>();
        this.courses = new ArrayList<>();
        this.interests = new ArrayList<>();
        this.isActive = true;
    }

    public Account(String username, String password, String name, String bio, String email, boolean isAdmin) throws InputDataValidationException
    {
        this();

        this.username = username;
        setPassword(password);
        this.name = name;
        this.bio = bio;
        this.email = email;
        this.isAdmin = isAdmin;
    }

    public Account(String username, String password, String name, String bio, String email, String displayPictureUrl, boolean isAdmin) throws InputDataValidationException
    {
        this(username, password, name, bio, email, isAdmin);

        this.displayPictureUrl = displayPictureUrl;
    }

    public Account(Long accountId, String username, String password, String name, String bio, String email, String displayPictureUrl, boolean isAdmin) throws InputDataValidationException
    {
        this(username, password, name, bio, email, displayPictureUrl, isAdmin);

        this.accountId = accountId;
    }

    // Should only be used for Database Config
    public Account(String username, String password, String name, String bio, String email, String displayPictureUrl, String stripeAccountId, boolean isAdmin) throws InputDataValidationException
    {
        this(username, password, name, bio, email, displayPictureUrl, isAdmin);

        this.stripeAccountId = stripeAccountId;
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

    public String getSalt()
    {
        return salt;
    }

    public void setSalt(String salt)
    {
        this.salt = salt;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password) throws InputDataValidationException
    {
        if (password != null)
        {
            if (PASSWORD_PATTERN.matcher(password).matches())
            {
                this.password = CryptographicHelper.getSHA256Digest(password, this.salt);
            }
            else
            {
                throw new InputDataValidationException("Password has to be at least 8 characters long with at least 1 uppercase letter and 1 number");
            }
        }
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

    public String getDisplayPictureFilename()
    {
        return FormatterUtil.getGCSObjectNameFromMediaLink(getDisplayPictureUrl());
    }

    public String getDisplayPictureUrl()
    {
        return displayPictureUrl;
    }

    public void setDisplayPictureUrl(String displayPictureUrl)
    {
        this.displayPictureUrl = displayPictureUrl;
    }

    public Boolean getIsAdmin()
    {
        return isAdmin;
    }

    public void setIsAdmin(Boolean admin)
    {
        isAdmin = admin;
    }

    public Boolean getIsActive()
    {
        return isActive;
    }

    public void setIsActive(Boolean active)
    {
        isActive = active;
    }

    public List<Tag> getInterests()
    {
        return interests;
    }

    public void setInterests(List<Tag> interests)
    {
        this.interests = interests;
    }

    public List<EnrolledCourse> getEnrolledCourses()
    {
        return enrolledCourses;
    }

    public void setEnrolledCourses(List<EnrolledCourse> enrolledCourses)
    {
        this.enrolledCourses = enrolledCourses;
    }

    public List<Course> getCourses()
    {
        return courses;
    }

    public void setCourses(List<Course> courses)
    {
        this.courses = courses;
    }

    public String getStripeAccountId()
    {
        return stripeAccountId;
    }

    public void setStripeAccountId(String stripeAccountId)
    {
        this.stripeAccountId = stripeAccountId;
    }

    @Override
    public String toString()
    {
        return "Account{" +
                "accountId=" + accountId +
                ", username='" + username + '\'' +
                ", salt='" + salt + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", bio='" + bio + '\'' +
                ", email='" + email + '\'' +
                ", displayPictureUrl='" + displayPictureUrl + '\'' +
                ", isAdmin=" + isAdmin +
                ", isActive=" + isActive +
                ", enrolledCourses=" + enrolledCourses +
                ", courses=" + courses +
                ", interests=" + interests +
                '}';
    }
}
