package com.spring.kodo.config;

import com.spring.kodo.entity.Account;
import com.spring.kodo.entity.Course;
import com.spring.kodo.entity.Tag;
import com.spring.kodo.service.FileService;
import com.spring.kodo.service.AccountService;
import com.spring.kodo.service.CourseService;
import com.spring.kodo.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class DatabaseConfig
{
    @Autowired
    private CourseService courseService;

    @Autowired
    private TagService tagService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private FileService fileService;

    @Autowired
    private Environment env;

    @EventListener(ApplicationReadyEvent.class)
    public void loadDataOnStartup() throws Exception
    {
        System.out.println("\n===== Application started on port: " + env.getProperty("local.server.port") + " =====");
        System.out.println("\n===== 1. Loading Init Data to Database =====");

        // Populate data lists
        List<Account> accounts = addAccounts();
        List<Tag> tags = addTags();
        List<Course> courses = addCourses();

        // Create data set to Database
        // Create Accounts w Tags
        for (Account account : accounts)
        {
            accountService.createNewAccount(account, tags.stream().map(tag -> tag.getTitle()).collect(Collectors.toList()));
        }

        // Print Ids of saved data list
        printIds();

        System.out.println("===== Init Data Fully Loaded to Database =====");
    }

    private void printIds()
    {
        List<Long> accountIds = accountService.getAllAccounts().stream().map(Account::getAccountId).collect(Collectors.toList());
        System.out.println(">> Added Accounts with accountIds: " + accountIds);

        List<Long> tagIds = tagService.getAllTags().stream().map(Tag::getTagId).collect(Collectors.toList());
        System.out.println(">> Added Tags with tagIds: " + tagIds);

        List<Long> courseIds = courseService.getAllCourses().stream().map(Course::getCourseId).collect(Collectors.toList());
        System.out.println(">> Added Courses with courseIds: " + courseIds);
    }

    private List<Account> addAccounts()
    {
        return Arrays.asList(
                new Account("admin", "password", "Admin", "I am Admin", "admin@gmail.com", "https://adminURL.com", true),
                new Account("student1", "password", "Student 1", "I am Student 1", "student1@gmail.com", "https://student1URL.com", false),
                new Account("student2", "password", "Student 2", "I am Student 2", "student2@gmail.com", "https://student2URL.com", false),
                new Account("tutor1", "password", "Tutor 1", "I am Tutor 1", "tutor1@gmail.com", "https://tutor1URL.com", false)
        );
    }

    private List<Tag> addTags()
    {
        return Arrays.asList(
                new Tag("Python"),
                new Tag("JavaScript"),
                new Tag("Java"),
                new Tag("C#"),
                new Tag("C"),
                new Tag("C++"),
                new Tag("Go"),
                new Tag("R"),
                new Tag("Swift")
        );
    }

    private List<Course> addCourses()
    {
        return Arrays.asList(
                new Course("Python Course", "A beginner course in Python language.", BigDecimal.valueOf(19.99), "https://pythoncoursebanner.com"),
                new Course("JavaScript Course", "A beginner course in JavaScript language.", BigDecimal.valueOf(19.99), "https://javascriptbanner.com"),
                new Course("Java Course", "A beginner course in Java language.", BigDecimal.valueOf(19.99), "https://javabanner.com"),
                new Course("C# Course", "A beginner course in C# language.", BigDecimal.valueOf(19.99), "https://c#banner.com")
        );
    }
}

