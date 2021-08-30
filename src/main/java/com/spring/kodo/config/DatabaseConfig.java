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
    private StudentAttemptService studentAttemptService;

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
            "C",
            "C#",
            "C++",
            "Go",
            "Java",
            "JavaScript",
            "Perl",
            "Python",
            "R",
            "Rust",
            "SQL",
            "Scala",
            "Swift",
            "TypeScript"
    );

    private static final Integer PREFIXED_ADMIN_COUNT = 1;
    private static final Integer TUTOR_COUNT = 5;
    private static final Integer PREFIXED_TUTOR_COUNT = 1;
    private static final Integer STUDENT_COUNT = 50;
    private static final Integer PREFIXED_STUDENT_COUNT = 2;

    private static final Integer ADMIN_FIRST_INDEX = 0; // 0
    private static final Integer ADMIN_LAST_INDEX = ADMIN_FIRST_INDEX + PREFIXED_ADMIN_COUNT; // 1
    private static final Integer STUDENT_FIRST_INDEX = ADMIN_LAST_INDEX + 1; // 2
    private static final Integer STUDENT_LAST_INDEX = STUDENT_FIRST_INDEX + STUDENT_COUNT - 1; // 51
    private static final Integer TUTOR_FIRST_INDEX = STUDENT_LAST_INDEX + 1; // 52
    private static final Integer TUTOR_LAST_INDEX = TUTOR_FIRST_INDEX + TUTOR_COUNT - 1; // 56

    private static final Integer LESSON_COUNT = 5;
    private static final Integer QUIZ_QUESTION_COUNT = 5;
    private static final Integer QUIZ_QUESTION_OPTION_COUNT = 4;
    private static final Integer STUDENT_ATTEMPT_COUNT = 3;

    @EventListener(ApplicationReadyEvent.class)
    public void loadDataOnStartup() throws Exception
    {
        System.out.println("\n===== Application started on port: " + env.getProperty("local.server.port") + " =====");
        System.out.println("\n===== 1. Loading Init Data to Database =====");

        // Populate data lists
        System.out.println("\n===== 1.1. Populating Data Lists =====");
        List<Account> accounts = addAccounts();
        List<Tag> tags = addTags();
        List<Course> courses = addCourses();
        List<Lesson> lessons = addLessons();
        List<Quiz> quizzes = addQuizzes();
        List<QuizQuestion> quizQuestions = addQuizQuestions();
        List<QuizQuestionOption> quizQuestionOptions = addQuizQuestionOptions();
        List<Multimedia> multimedias = addMultimedias();

        // Create data set to Database
        System.out.println("\n===== 1.2. Creating Data Lists to Database =====");
        create(accounts,
                tags,
                courses,
                lessons,
                quizzes,
                quizQuestions,
                quizQuestionOptions,
                multimedias
        );

        // Print Ids of saved data list
        System.out.println("\n===== 1.3. Print IDs =====");
        printIds();

        System.out.println("\n===== Init Data Fully Loaded to Database =====");
    }

    private void create(
            List<Account> accounts,
            List<Tag> tags,
            List<Course> courses,
            List<Lesson> lessons,
            List<Quiz> quizzes,
            List<QuizQuestion> quizQuestions,
            List<QuizQuestionOption> quizQuestionOptions,
            List<Multimedia> multimedias
    ) throws Exception
    {
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
            course = courses.get(courseIndex++);
            tag = tags.get(tagIndex++);

            // Course Creation
            courseService.createNewCourse(
                    course,
                    accounts.get(getRandomNumber(TUTOR_FIRST_INDEX, TUTOR_LAST_INDEX)).getAccountId(),
                    Arrays.asList(tag.getTitle())
            );

            for (int i = 0; i < LESSON_COUNT; i++, lessonIndex++, quizIndex++, multimediaIndex++)
            {
                lesson = lessons.get(lessonIndex);
                quiz = quizzes.get(quizIndex);
                multimedia = multimedias.get(multimediaIndex);

                // Lesson Creation
                courseService.addLessonToCourse(course, lesson);

                // Quiz Creation
                lessonService.addContentToLesson(lesson, quiz);

                // QuizQuestion Creation
                for (int j = 0; j < QUIZ_QUESTION_COUNT; j++, quizQuestionIndex++, quizQuestionOptionIndex += QUIZ_QUESTION_OPTION_COUNT)
                {
                    quizService.addQuizQuestionToQuiz(
                            quiz,
                            quizQuestions.get(quizQuestionIndex),
                            quizQuestionOptions.subList(quizQuestionOptionIndex, quizQuestionOptionIndex + QUIZ_QUESTION_OPTION_COUNT)
                    );
                }

                // StudentAttempt Creation
                for (int j = 0; j < STUDENT_ATTEMPT_COUNT; j++)
                {
                    studentAttemptService.createNewStudentAttempt(
                            quiz.getContentId(),
                            accounts.get(getRandomNumber(STUDENT_FIRST_INDEX, STUDENT_LAST_INDEX)).getAccountId()
                    );
                }

                // Multimedia Creation
                lessonService.addContentToLesson(lesson, multimedia);
            }
        }
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

        List<Long> studentAttemptIds = studentAttemptService.getAllStudentAttempts().stream().map(StudentAttempt::getStudentAttemptId).collect(Collectors.toList());
        System.out.println(">> Added Lessons with studentAttemptIds: " + studentAttemptIds);
    }

    private List<Account> addAccounts()
    {
        List<Account> accounts = new ArrayList<>();

        accounts.add(new Account("admin", "password", "Admin Adam", "I am Admin", "admin@gmail.com", "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/1131f24e-b080-4420-a897-88bcee2b2787.gif?generation=1630265308844077&alt=media", true));

        accounts.add(new Account("student1", "password", "Student Samuel", "I am Student Samuel", "studentsamuel@gmail.com", "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/cba20ec5-5739-4853-b425-ba39647cd8cc.gif?generation=1630266661221190&alt=media", false));
        accounts.add(new Account("student2", "password", "Student Sunny", "I am Student Sunny", "studentsunny@gmail.com", "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/46a24305-9b12-4445-b779-5ee1d56b94d7.gif?generation=1630266556687403&alt=media", false));
        for (int i = PREFIXED_STUDENT_COUNT + 1; i <= STUDENT_COUNT; i++)
        {
            accounts.add(new Account("student" + i, "password", "Student " + i, "I am Student " + i, "student" + i + "@gmail.com", "https://student" + i + ".com", false));
        }

        accounts.add(new Account("tutor1", "password", "Tutor Trisha", "I am Tutor 1", "tutor1@gmail.com", "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/18700b5a-4890-430f-9bab-1d312862c030.gif?generation=1630266710675423&alt=media", false));
        for (int i = PREFIXED_TUTOR_COUNT + 1; i <= TUTOR_COUNT; i++)
        {
            accounts.add(new Account("tutor" + i, "password", "Tutor " + i, "I am Tutor " + i, "tutor" + i + "@gmail.com", "https://student" + i + ".com", false));
        }

        return accounts;
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

