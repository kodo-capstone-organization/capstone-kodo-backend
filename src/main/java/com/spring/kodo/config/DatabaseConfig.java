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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
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

    private static final List<String> PROGRAMMING_LANGUAGES = Arrays.asList(
            "Python",
            "JavaScript",
            "Java",
            "C#",
            "C",
            "C++",
            "Go",
            "R",
            "Swift"
    );

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
            accountService.createNewAccount(account, Arrays.asList(tags.get(getRandomNumber(0, tags.size())).getTitle()));
        }

        // Create Courses
        for (Course course : courses)
        {
            courseService.createNewCourse(
                    course,
                    accounts.get(getRandomNumber(0, accounts.size())),
                    Arrays.asList(tags.get(courses.indexOf(course)).getTitle())
            );
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
        List<Tag> tags = new ArrayList<>();

        for (String language : PROGRAMMING_LANGUAGES)
        {
            tags.add(new Tag(language));
        }

        return tags;
    }

    private List<Course> addCourses()
    {
        List<Course> courses = new ArrayList<>();

        for (String language : PROGRAMMING_LANGUAGES)
        {
            courses.add(new Course(language + " Course", "A beginner course in " + language + " language.", BigDecimal.valueOf(19.99), "https://" + language.toLowerCase() + "coursebanner.com"));
        }

        return courses;
    }

    private int getRandomNumber(int min, int max)
    {
        return (int) ((Math.random() * (max - min)) + min);
    }
}

