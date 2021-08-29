package com.spring.kodo.config;

import com.spring.kodo.entity.*;
import com.spring.kodo.service.*;
import com.spring.kodo.util.enumeration.MultimediaType;
import com.spring.kodo.util.enumeration.QuestionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Configuration
public class DatabaseConfig
{
    @Autowired
    private MultimediaService multimediaService;

    @Autowired
    private QuizQuestionOptionService quizQuestionOptionService;

    @Autowired
    private QuizQuestionService quizQuestionService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private LessonService lessonService;

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
            "TypeScript",
            "Java",
            "C",
            "C#",
            "C++",
            "Go",
            "R",
            "Swift"
    );

    private static final Integer LESSON_COUNT = 5;
    private static final Integer QUIZ_QUESTION_COUNT = 5;
    private static final Integer QUIZ_QUESTION_OPTION_COUNT = 4;

    @EventListener(ApplicationReadyEvent.class)
    public void loadDataOnStartup() throws Exception
    {
        System.out.println("\n===== Application started on port: " + env.getProperty("local.server.port") + " =====");
        System.out.println("\n===== 1. Loading Init Data to Database =====");

        // Populate data lists
        List<Account> accounts = addAccounts();
        List<Tag> tags = addTags();
        List<Course> courses = addCourses();
        List<Lesson> lessons = addLessons();
        List<Quiz> quizzes = addQuizzes();
        List<QuizQuestion> quizQuestions = addQuizQuestions();
        List<QuizQuestionOption> quizQuestionOptions = addQuizQuestionOptions();
        List<Multimedia> multimedias = addMultimedias();

        // Create data set to Database
        // Create Accounts w Tags
        for (Account account : accounts)
        {
            accountService.createNewAccount(account, Arrays.asList(tags.get(getRandomNumber(0, tags.size())).getTitle()));
        }

        // Create Courses and lessons
        int courseIndex = 0;
        int tagIndex = 0;
        int lessonIndex = 0;
        int quizIndex = 0;
        int quizQuestionIndex = 0;
        int quizQuestionOptionIndex = 0;
        int multimediaIndex = 0;

        Course course;
        Tag tag;
        Lesson lesson;
        Quiz quiz;
        QuizQuestion quizQuestion;
        QuizQuestionOption quizQuestionOption;
        Multimedia multimedia;

        while (courseIndex < courses.size()
                && tagIndex < tags.size()
                && lessonIndex < lessons.size()
                && quizIndex < quizzes.size()
                && quizQuestionIndex < quizQuestions.size()
                && quizQuestionOptionIndex < quizQuestionOptions.size()
                && multimediaIndex < multimedias.size())
        {
            course = courses.get(courseIndex);
            tag = tags.get(tagIndex);

            courseService.createNewCourse(
                    course,
                    accounts.get(getRandomNumber(0, accounts.size())).getAccountId(),
                    Arrays.asList(tag.getTitle())
            );

            for (int i = 0; i < LESSON_COUNT; i++, lessonIndex++, quizIndex++, multimediaIndex++)
            {
                lesson = lessons.get(lessonIndex);
                quiz = quizzes.get(quizIndex);
                multimedia = multimedias.get(multimediaIndex);

                courseService.addLessonToCourse(course, lesson);
                lessonService.addContentToLesson(lesson, quiz);

                for (int j = 0; j < QUIZ_QUESTION_COUNT; j++, quizQuestionIndex++, quizQuestionOptionIndex += QUIZ_QUESTION_OPTION_COUNT)
                {
                    quizService.addQuizQuestionToQuiz(
                            quiz,
                            quizQuestions.get(quizQuestionIndex),
                            quizQuestionOptions.subList(quizQuestionOptionIndex, quizQuestionOptionIndex + QUIZ_QUESTION_OPTION_COUNT)
                    );
                }

                lessonService.addContentToLesson(lesson, multimedia);
            }
        }
//        for (int i = 0; i < courses.size(); i++)
//        {
//            Course course = courses.get(i);
//            Tag tag = tags.get(i);
//            courseService.createNewCourse(
//                    course,
//                    accounts.get(getRandomNumber(0, accounts.size())).getAccountId(),
//                    Arrays.asList(tag.getTitle())
//            );
//
//            for (int j = LESSON_COUNT * i; j < LESSON_COUNT * (i + 1); j++)
//            {
//                Lesson lesson = lessons.get(j);
//                Quiz quiz = quizzes.get(j);
//                Multimedia multimedia = multimedias.get(j);
//                courseService.addLessonToCourse(course, lesson);
//                lessonService.addContentToLesson(lesson, quiz);
//
//                for (int k = QUIZ_QUESTION_COUNT * j; k < QUIZ_QUESTION_COUNT * (j + 1); k++)
//                {
//                    quizService.addQuizQuestionToQuiz(quiz, quizQuestions.get(k), quizQuestionOptions.subList());
//                }
//
//                lessonService.addContentToLesson(lesson, multimedia);
//            }
//        }

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

        List<Long> lessonIds = lessonService.getAllLessons().stream().map(Lesson::getLessonId).collect(Collectors.toList());
        System.out.println(">> Added Lessons with lessonIds: " + lessonIds);

        List<Long> quizIds = quizService.getAllQuizzes().stream().map(Quiz::getContentId).collect(Collectors.toList());
        System.out.println(">> Added Lessons with quizIds: " + quizIds);

        List<Long> quizQuestionIds = quizQuestionService.getAllQuizQuestions().stream().map(QuizQuestion::getQuizQuestionId).collect(Collectors.toList());
        System.out.println(">> Added Lessons with quizQuestionIds: " + quizQuestionIds);

        List<Long> quizQuestionOptionIds = quizQuestionOptionService.getAllQuizQuestionOptions().stream().map(QuizQuestionOption::getQuizQuestionOptionId).collect(Collectors.toList());
        System.out.println(">> Added Lessons with quizQuestionOptionIds: " + quizQuestionOptionIds);

        List<Long> multimediaIds = multimediaService.getAllMultimedias().stream().map(Multimedia::getContentId).collect(Collectors.toList());
        System.out.println(">> Added Lessons with multimediaIds: " + multimediaIds);
    }

    private List<Account> addAccounts()
    {
        return Arrays.asList(
                new Account("admin", "password", "Admin Adam", "I am Admin", "admin@gmail.com", "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/1131f24e-b080-4420-a897-88bcee2b2787.gif?generation=1630265308844077&alt=media", true),
                new Account("student1", "password", "Student Samuel", "I am Student 1", "student1@gmail.com", "https://student1URL.com", false),
                new Account("student2", "password", "Student Sunny", "I am Student 2", "student2@gmail.com", "https://student2URL.com", false),
                new Account("tutor1", "password", "Tutor Trisha", "I am Tutor 1", "tutor1@gmail.com", "https://tutor1URL.com", false)
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

    private List<Lesson> addLessons()
    {
        List<Lesson> lessons = new ArrayList<>();

        for (String language : PROGRAMMING_LANGUAGES)
        {
            for (int i = 1; i <= LESSON_COUNT; i++)
            {
                lessons.add(new Lesson(language + " Lesson " + i, "A very interesting " + ordinal(i) + " lesson on " + language, i));
            }
        }

        return lessons;
    }

    private List<Quiz> addQuizzes()
    {
        List<Quiz> quizzes = new ArrayList<>();

        for (String language : PROGRAMMING_LANGUAGES)
        {
            for (int i = 1; i <= LESSON_COUNT; i++)
            {
                quizzes.add(new Quiz(language + " Quiz #" + i, "A very interesting " + ordinal(i) + " quiz on " + language, LocalTime.of(0, 30), 10));
            }
        }

        return quizzes;
    }

    private List<QuizQuestion> addQuizQuestions()
    {
        List<QuizQuestion> quizQuestions = new ArrayList<>();

        for (String language : PROGRAMMING_LANGUAGES)
        {
            for (int i = 1; i <= LESSON_COUNT; i++)
            {
                for (int j = 1; j <= QUIZ_QUESTION_COUNT; j++)
                {
                    quizQuestions.add(new QuizQuestion(ordinal(j) + " question of quiz for lesson " + j + " of " + language + " course", QuestionType.MCQ, 1));
                }
            }
        }

        return quizQuestions;
    }

    private List<QuizQuestionOption> addQuizQuestionOptions()
    {
        List<QuizQuestionOption> quizQuestionOptions = new ArrayList<>();

        for (String language : PROGRAMMING_LANGUAGES)
        {
            for (int i = 1; i <= LESSON_COUNT; i++)
            {
                for (int j = 1; j <= QUIZ_QUESTION_COUNT; j++)
                {
                    for (int k = 1; k <= QUIZ_QUESTION_OPTION_COUNT; k++)
                    {
                        quizQuestionOptions.add(new QuizQuestionOption("Option " + k, null, k == 1));
                    }
                }
            }
        }

        return quizQuestionOptions;
    }

    private List<Multimedia> addMultimedias()
    {
        List<Multimedia> multimedias = new ArrayList<>();

        for (String language : PROGRAMMING_LANGUAGES)
        {
            for (int i = 1; i <= LESSON_COUNT; i++)
            {
                multimedias.add(new Multimedia(language + " Multimedia #" + i, "A very interesting " + ordinal(i) + " multimedia on " + language, "https://" + language + "multimedia" + i, MultimediaType.PDF));
            }
        }

        return multimedias;
    }


    private int getRandomNumber(int min, int max)
    {
        return (int) ((Math.random() * (max - min)) + min);
    }

    private String ordinal(int i)
    {
        String[] suffixes = new String[]{"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"};
        switch (i % 100)
        {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + suffixes[i % 10];
        }
    }
}

