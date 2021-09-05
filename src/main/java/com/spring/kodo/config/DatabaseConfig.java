package com.spring.kodo.config;

import com.spring.kodo.entity.*;
import com.spring.kodo.service.inter.*;
import com.spring.kodo.util.enumeration.MultimediaType;
import com.spring.kodo.util.enumeration.QuestionType;
import com.spring.kodo.util.exception.InputDataValidationException;
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
import java.util.stream.Collectors;

@Configuration
public class DatabaseConfig
{
    @Autowired
    private ForumPostService forumPostService;

    @Autowired
    private ForumThreadService forumThreadService;

    @Autowired
    private ForumCategoryService forumCategoryService;

    @Autowired
    private CompletedLessonService completedLessonService;

    @Autowired
    private EnrolledCourseService enrolledCourseService;

    @Autowired
    private StudentAttemptAnswerService studentAttemptAnswerService;

    @Autowired
    private StudentAttemptQuestionService studentAttemptQuestionService;

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

    private List<String> PROGRAMMING_LANGUAGES;

    private List<Account> accounts;
    private List<Tag> tags;
    private List<Course> courses;
    private List<Lesson> lessons;
    private List<Quiz> quizzes;
    private List<QuizQuestion> quizQuestions;
    private List<QuizQuestionOption> quizQuestionOptions;
    private List<Multimedia> multimedias;
    private List<EnrolledCourse> enrolledCourses;
    private List<StudentAttempt> studentAttempts;
    private List<StudentAttemptQuestion> studentAttemptQuestions;
    private List<StudentAttemptAnswer> studentAttemptAnswers;
    private List<CompletedLesson> completedLessons;
    private List<ForumCategory> forumCategories;
    private List<ForumThread> forumThreads;
    private List<ForumPost> forumPosts;

    // Edit these to scale the sample database
    private final Integer PROGRAMMING_LANGUAGES_COUNT = 14; // Current max is 14

    private final Integer TUTOR_COUNT = 5;
    private final Integer STUDENT_COUNT = 10;

    private final Integer LESSON_COUNT = 1;
    private final Integer QUIZ_COUNT = 1;
    private final Integer QUIZ_QUESTION_COUNT = 5;
    private final Integer QUIZ_QUESTION_OPTION_COUNT = 4;

    private final Integer STUDENT_ENROLLED_COUNT = 10;
    private final Integer STUDENT_ATTEMPT_COUNT = 5;

    private final Integer FORUM_CATEGORY_COUNT = 3;
    private final Integer FORUM_THREAD_COUNT = 3;
    private final Integer FORUM_POST_COUNT = 3;

    // Don't Edit these
    private final Integer PREFIXED_ADMIN_COUNT = 1;
    private final Integer PREFIXED_TUTOR_COUNT = 1;
    private final Integer PREFIXED_STUDENT_COUNT = 2;

    private final Integer ADMIN_FIRST_INDEX = 0;
    private final Integer ADMIN_SIZE = PREFIXED_ADMIN_COUNT + ADMIN_FIRST_INDEX;
    private final Integer STUDENT_FIRST_INDEX = ADMIN_SIZE;
    private final Integer STUDENT_SIZE = PREFIXED_STUDENT_COUNT + STUDENT_FIRST_INDEX + STUDENT_COUNT;
    private final Integer TUTOR_FIRST_INDEX = STUDENT_SIZE;
    private final Integer TUTOR_SIZE = PREFIXED_TUTOR_COUNT + TUTOR_FIRST_INDEX + TUTOR_COUNT;

    private final Long START_TIME;

    public DatabaseConfig()
    {
        PROGRAMMING_LANGUAGES = Arrays.asList(
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

        accounts = new ArrayList<>();
        tags = new ArrayList<>();
        courses = new ArrayList<>();
        lessons = new ArrayList<>();
        quizzes = new ArrayList<>();
        quizQuestions = new ArrayList<>();
        quizQuestionOptions = new ArrayList<>();
        multimedias = new ArrayList<>();
        enrolledCourses = new ArrayList<>();
        studentAttempts = new ArrayList<>();
        studentAttemptQuestions = new ArrayList<>();
        studentAttemptAnswers = new ArrayList<>();
        completedLessons = new ArrayList<>();
        forumCategories = new ArrayList<>();
        forumThreads = new ArrayList<>();
        forumPosts = new ArrayList<>();

        START_TIME = System.currentTimeMillis();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadDataOnStartup() throws Exception
    {
        System.out.println("\n===== Application started on port: " + env.getProperty("local.server.port") + " =====");
        System.out.println("\n===== 0. Checking settings =====");
        if (PROGRAMMING_LANGUAGES_COUNT <= PROGRAMMING_LANGUAGES.size())
        {
            PROGRAMMING_LANGUAGES = PROGRAMMING_LANGUAGES.subList(0, PROGRAMMING_LANGUAGES_COUNT);
        }
        else
        {
            throw new InputDataValidationException("Programming Language size has to be <= " + PROGRAMMING_LANGUAGES.size());
        }

        System.out.println("\n===== 1. Loading Init Data to Database =====");

        // Populate data lists
        System.out.println("\n===== 1.1. Populating Data Lists =====");
        addAccounts();
        addTags();
        addCourses();
        addLessons();
        addQuizzes();
        addQuizQuestions();
        addQuizQuestionOptions();
        addMultimedias();
        addForumCategories();
        addForumThreads();
        addForumPosts();

        // Create data set to Database
        System.out.println("\n===== 1.2. Creating Data Lists to Database =====");
        createTags();
        createAccounts();
        createCourses();
        createLessons();
        createQuizzes();
        createQuizQuestions();
        createQuizQuestionOptions();
        createMultimedias();
        createEnrolledCourse();
        createStudentAttemptsAndStudentAttemptQuestions();
        createStudentAttemptAnswers();
        createCompletedLessons();
        createForumCategories();
        createForumThreads();
        createForumPosts();

        System.out.println("\n===== Init Data Fully Loaded to Database =====");

        printTime();
    }

    private void createTags() throws Exception
    {
        for (int i = 0; i < tags.size(); i++)
        {
            tags.set(i, tagService.createNewTag(tags.get(i)));
        }

        System.out.printf(">> Created Tags (%d)\n", tagService.getAllTags().size());
    }

    private void createAccounts() throws Exception
    {
        for (int i = ADMIN_FIRST_INDEX; i < ADMIN_SIZE; i++)
        {
            accountService.createNewAccount(accounts.get(i), null);
        }

        for (int i = STUDENT_FIRST_INDEX; i < STUDENT_SIZE; i++)
        {
            accountService.createNewAccount(accounts.get(i), tags.subList(getRandomNumber(0, tags.size() / 2 - 1), getRandomNumber(tags.size() / 2 - 1, tags.size() - 1)).stream().map(Tag::getTitle).collect(Collectors.toList()));
        }

        for (int i = TUTOR_FIRST_INDEX; i < TUTOR_SIZE; i++)
        {
            accountService.createNewAccount(accounts.get(i), null);
        }

        accounts = accountService.getAllAccounts();

        System.out.printf(">> Created Accounts (%d)\n", accounts.size());
    }

    private void createCourses() throws Exception
    {
        Account tutor;
        Course course;

        int tutorIndex = TUTOR_FIRST_INDEX;

        for (int i = 0; i < courses.size(); i++, tutorIndex++)
        {
            tutor = accounts.get(tutorIndex);
            course = courses.get(i);

            courseService.createNewCourse(course, tutor.getAccountId(), Arrays.asList(tags.get(i).getTitle()));
            accountService.addCourseToAccount(tutor, course);

            if (tutorIndex == TUTOR_SIZE - 1)
            {
                tutorIndex = TUTOR_FIRST_INDEX;
            }
        }

        courses = courseService.getAllCourses();

        System.out.printf(">> Created Courses (%d)\n", courses.size());
    }

    private void createLessons() throws Exception
    {
        Lesson lesson;

        int lessonIndex = 0;

        for (int i = 0; i < courses.size(); i++)
        {
            for (int j = 0; j < LESSON_COUNT; j++, lessonIndex++)
            {
                lesson = lessons.get(lessonIndex);

                lessonService.createNewLesson(lesson);
                courseService.addLessonToCourse(courses.get(i), lesson);
            }
        }

        lessons = lessonService.getAllLessons();

        System.out.printf(">> Created Lessons (%d)\n", lessons.size());
    }

    private void createQuizzes() throws Exception
    {
        Quiz quiz;

        int quizIndex = 0;

        for (int i = 0; i < courses.size(); i++)
        {
            for (int j = 0; j < LESSON_COUNT; j++)
            {
                for (int l = 0; l < QUIZ_COUNT; l++, quizIndex++)
                {
                    quiz = quizzes.get(quizIndex);
                    quiz = quizService.createNewQuiz(quiz);

                    lessonService.addContentToLesson(lessons.get(j), quiz);
                }
            }
        }

        quizzes = quizService.getAllQuizzes();

        System.out.printf(">> Created Quizzes (%d)\n", quizzes.size());
    }

    private void createQuizQuestions() throws Exception
    {
        Quiz quiz;
        QuizQuestion quizQuestion;

        int quizIndex = 0;
        int quizQuestionIndex = 0;

        for (int i = 0; i < courses.size(); i++)
        {
            for (int j = 0; j < LESSON_COUNT; j++)
            {
                for (int k = 0; k < QUIZ_COUNT; k++, quizIndex++)
                {
                    quiz = quizzes.get(quizIndex);

                    for (int l = 0; l < QUIZ_QUESTION_COUNT; l++, quizQuestionIndex++)
                    {
                        quizQuestion = quizQuestionService.createNewQuizQuestion(quizQuestions.get(quizQuestionIndex), quiz.getContentId());

                        quizService.addQuizQuestionToQuiz(quiz, quizQuestion);
                    }
                }
            }
        }

        quizQuestions = quizQuestionService.getAllQuizQuestions();

        System.out.printf(">> Created QuizQuestions (%d)\n", quizQuestions.size());
    }

    private void createQuizQuestionOptions() throws Exception
    {
        QuizQuestion quizQuestion;
        QuizQuestionOption quizQuestionOption;

        int quizQuestionIndex = 0;
        int quizQuestionOptionIndex = 0;

        for (int i = 0; i < courses.size(); i++)
        {
            for (int j = 0; j < LESSON_COUNT; j++)
            {
                for (int k = 0; k < QUIZ_COUNT; k++)
                {
                    for (int l = 0; l < QUIZ_QUESTION_COUNT; l++, quizQuestionIndex++)
                    {
                        quizQuestion = quizQuestions.get(quizQuestionIndex);

                        for (int a = 0; a < QUIZ_QUESTION_OPTION_COUNT; a++, quizQuestionOptionIndex++)
                        {
                            quizQuestionOption = quizQuestionOptionService.createNewQuizQuestionOption(quizQuestionOptions.get(quizQuestionOptionIndex));

                            quizQuestionService.addQuizQuestionOptionToQuizQuestion(quizQuestion, quizQuestionOption);
                        }
                    }
                }
            }
        }

        quizQuestionOptions = quizQuestionOptionService.getAllQuizQuestionOptions();

        System.out.printf(">> Created QuizQuestionOptions (%d)\n", quizQuestionOptions.size());
    }

    private void createMultimedias() throws Exception
    {
        Multimedia multimedia;

        int multimediaIndex = 0;

        for (int i = 0; i < courses.size(); i++)
        {
            for (int j = 0; j < LESSON_COUNT; j++, multimediaIndex++)
            {
                multimedia = multimediaService.createNewMultimedia(multimedias.get(multimediaIndex));
                lessonService.addContentToLesson(lessons.get(j), multimedia);
            }
        }

        multimedias = multimediaService.getAllMultimedias();

        System.out.printf(">> Created Multimedias (%d)\n", multimedias.size());
    }

    private void createEnrolledCourse() throws Exception
    {
        Account student;
        EnrolledCourse enrolledCourse;

        int studentIndex = STUDENT_FIRST_INDEX;
        int courseIndex = 0;

        for (int i = 0; i < STUDENT_ENROLLED_COUNT; i++, studentIndex++, courseIndex++)
        {
            try
            {
                student = accounts.get(studentIndex);
                enrolledCourse = enrolledCourseService.createNewEnrolledCourse(student.getAccountId(), courses.get(courseIndex).getCourseId());
                accountService.addEnrolledCourseToAccount(student, enrolledCourse);
            }
            catch (Exception ex)
            {
            }

            if (studentIndex == STUDENT_SIZE - 1)
            {
                studentIndex = STUDENT_FIRST_INDEX;
            }

            if (courseIndex == courses.size() - 1)
            {
                courseIndex = 0;
            }
        }

        enrolledCourses = enrolledCourseService.getAllEnrolledCourses();

        System.out.printf(">> Created EnrolledCourses (%d)\n", enrolledCourses.size());
    }

    private void createStudentAttemptsAndStudentAttemptQuestions() throws Exception
    {
        Account student;
        StudentAttempt studentAttempt;

        int quizIndex = 0;

        for (EnrolledCourse enrolledCourse : enrolledCourses)
        {
            for (int i = 0; i < STUDENT_ATTEMPT_COUNT; i++, quizIndex++)
            {
                student = accountService.getAccountByEnrolledCourseId(enrolledCourse.getEnrolledCourseId());
                studentAttempt = studentAttemptService.createNewStudentAttempt(quizzes.get(quizIndex).getContentId());

                accountService.addStudentAttemptToAccount(student, studentAttempt);

                if (quizIndex == quizzes.size() - 1)
                {
                    quizIndex = 0;
                }
            }
        }

        studentAttempts = studentAttemptService.getAllStudentAttempts();
        studentAttemptQuestions = studentAttemptQuestionService.getAllStudentAttemptQuestions();

        System.out.printf(">> Created StudentAttempts (%d)\n", studentAttempts.size());
        System.out.printf(">> Created StudentAttemptQuestions (%d)\n", studentAttemptQuestions.size());
    }

    private void createStudentAttemptAnswers() throws Exception
    {
        StudentAttemptAnswer studentAttemptAnswer;

        for (StudentAttempt studentAttempt : studentAttempts)
        {
            for (StudentAttemptQuestion studentAttemptQuestion : studentAttempt.getStudentAttemptQuestions())
            {
                for (QuizQuestionOption quizQuestionOption : studentAttemptQuestion.getQuizQuestion().getQuizQuestionOptions())
                {
                    studentAttemptAnswer = studentAttemptAnswerService.createNewStudentAttemptAnswer(quizQuestionOption.getQuizQuestionOptionId());
                    studentAttemptQuestionService.addStudentAttemptAnswerToStudentAttemptQuestion(studentAttemptQuestion, studentAttemptAnswer);
                }
            }
        }

        studentAttemptAnswers = studentAttemptAnswerService.getAllStudentAttemptAnswers();

        System.out.printf(">> Created StudentAttemptAnswers (%d)\n", studentAttemptAnswers.size());
    }

    private void createCompletedLessons() throws Exception
    {
        completedLessons = completedLessonService.getAllCompletedLessons();

        System.out.printf(">> Created CompletedLessons (%d)\n", completedLessons.size());
    }

    private void createForumCategories() throws Exception
    {
        int forumCategoryIndex = 0;

        for (Course course : courses)
        {
            for (int i = 0; i < FORUM_CATEGORY_COUNT; i++, forumCategoryIndex++)
            {
                forumCategoryService.createNewForumCategory(forumCategories.get(forumCategoryIndex), course.getCourseId());
            }
        }

        forumCategories = forumCategoryService.getAllForumCategories();

        System.out.printf(">> Created ForumCategories (%d)\n", forumCategories.size());
    }

    private void createForumThreads() throws Exception
    {
        ForumCategory forumCategory;
        ForumThread forumThread;

        int forumCategoryIndex = 0;
        int forumThreadIndex = 0;
        int accountIndex = STUDENT_FIRST_INDEX;

        for (Course course : courses)
        {
            for (int i = 0; i < FORUM_CATEGORY_COUNT; i++, forumCategoryIndex++)
            {
                forumCategory = forumCategories.get(forumCategoryIndex);

                for (int j = 0; j < FORUM_THREAD_COUNT; j++, forumThreadIndex++)
                {
                    forumThread = forumThreads.get(forumThreadIndex);

                    forumThreadService.createNewForumThread(forumThread, (long) accountIndex);

                    forumCategoryService.addForumThreadToForumCategory(forumCategory, forumThread);

                    if (accountIndex == TUTOR_SIZE - 1)
                    {
                        accountIndex = STUDENT_FIRST_INDEX;
                    }
                }
            }
        }

        forumThreads = forumThreadService.getAllForumThreads();

        System.out.printf(">> Created ForumThreads (%d)\n", forumThreads.size());
    }

    private void createForumPosts() throws Exception
    {
        ForumThread forumThread;
        ForumPost forumPost;

        int forumThreadIndex = 0;
        int forumPostIndex = 0;
        int accountIndex = STUDENT_FIRST_INDEX;

        for (Course course : courses)
        {
            for (int i = 0; i < FORUM_CATEGORY_COUNT; i++)
            {
                for (int j = 0; j < FORUM_THREAD_COUNT; j++, forumThreadIndex++)
                {
                    forumThread = forumThreads.get(forumThreadIndex);

                    for (int k = 0; k < FORUM_POST_COUNT; k++, forumPostIndex++)
                    {

                        forumPost = forumPosts.get(forumPostIndex);

                        forumPost = forumPostService.createNewForumPost(forumPost, accounts.get(accountIndex).getAccountId());

                        forumThreadService.addForumPostToForumThread(forumThread, forumPost);

                        if (accountIndex == TUTOR_SIZE - 1)
                        {
                            accountIndex = STUDENT_FIRST_INDEX;
                        }
                    }
                }
            }
        }

        forumPosts = forumPostService.getAllForumPosts();

        System.out.printf(">> Created ForumPosts (%d)\n", forumPosts.size());
    }

    private void addAccounts()
    {
        accounts.add(new Account("admin", "password", "Admin Adam", "I am Admin", "admin@gmail.com", "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/1131f24e-b080-4420-a897-88bcee2b2787.gif?generation=1630265308844077&alt=media", true));

        accounts.add(new Account("student1", "password", "Student Samuel", "I am Student Samuel", "studentsamuel@gmail.com", "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/cba20ec5-5739-4853-b425-ba39647cd8cc.gif?generation=1630266661221190&alt=media", false));
        accounts.add(new Account("student2", "password", "Student Sunny", "I am Student Sunny", "studentsunny@gmail.com", "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/46a24305-9b12-4445-b779-5ee1d56b94d7.gif?generation=1630266556687403&alt=media", false));
        for (int i = 3; i <= STUDENT_COUNT + 2; i++)
        {
            accounts.add(new Account("student" + i, "password", "Student " + i, "I am Student " + i, "student" + i + "@gmail.com", "https://student" + i + ".com", false));
        }

        accounts.add(new Account("tutor1", "password", "Tutor Trisha", "I am Tutor 1", "tutor1@gmail.com", "https://storage.googleapis.com/download/storage/v1/b/capstone-kodo-bucket/o/18700b5a-4890-430f-9bab-1d312862c030.gif?generation=1630266710675423&alt=media", false));
        for (int i = 2; i <= TUTOR_COUNT + 1; i++)
        {
            accounts.add(new Account("tutor" + i, "password", "Tutor " + i, "I am Tutor " + i, "tutor" + i + "@gmail.com", "https://tutor" + i + ".com", false));
        }
    }

    private void addTags()
    {
        for (String language : PROGRAMMING_LANGUAGES)
        {
            tags.add(new Tag(language));
        }
    }

    private void addCourses()
    {
        for (String language : PROGRAMMING_LANGUAGES)
        {
            courses.add(new Course(language + " Course", "A beginner course in " + language + " language.", BigDecimal.valueOf(19.99), "https://" + language.toLowerCase() + "coursebanner.com"));
        }
    }

    private void addLessons()
    {
        for (String language : PROGRAMMING_LANGUAGES)
        {
            for (int i = 1; i <= LESSON_COUNT; i++)
            {
                lessons.add(new Lesson(language + " Lesson " + i, "A very interesting " + ordinal(i) + " lesson on " + language, i));
            }
        }
    }

    private void addQuizzes()
    {
        for (String language : PROGRAMMING_LANGUAGES)
        {
            for (int i = 1; i <= LESSON_COUNT; i++)
            {
                for (int j = 1; j <= QUIZ_COUNT; j++)
                {
                    quizzes.add(new Quiz(language + " Quiz #" + j, "A very interesting " + ordinal(j) + " quiz on " + language, LocalTime.of(0, 30), 10));
                }
            }
        }
    }

    private void addQuizQuestions()
    {
        for (String language : PROGRAMMING_LANGUAGES)
        {
            for (int i = 1; i <= LESSON_COUNT; i++)
            {
                for (int j = 1; j <= QUIZ_COUNT; j++)
                {
                    for (int k = 1; k <= QUIZ_QUESTION_COUNT; k++)
                    {
                        quizQuestions.add(new QuizQuestion(ordinal(k) + " question of quiz for lesson " + i + " of " + language + " course", QuestionType.MCQ, 1));
                    }
                }
            }
        }
    }

    private void addQuizQuestionOptions()
    {
        for (String language : PROGRAMMING_LANGUAGES)
        {
            for (int i = 1; i <= LESSON_COUNT; i++)
            {
                for (int j = 1; j <= QUIZ_COUNT; j++)
                {
                    for (int k = 1; k <= QUIZ_QUESTION_COUNT; k++)
                    {
                        for (int l = 1; l <= QUIZ_QUESTION_OPTION_COUNT; l++)
                        {
                            quizQuestionOptions.add(new QuizQuestionOption("Option " + l, null, l == 1));
                        }
                    }
                }
            }
        }
    }

    private void addMultimedias()
    {
        for (String language : PROGRAMMING_LANGUAGES)
        {
            for (int i = 1; i <= LESSON_COUNT; i++)
            {
                multimedias.add(new Multimedia(language + " Multimedia #" + i, "A very interesting " + ordinal(i) + " multimedia on " + language, "https://" + language + "multimedia" + i, MultimediaType.PDF));
            }
        }
    }

    private void addForumCategories()
    {
        for (String language : PROGRAMMING_LANGUAGES)
        {
            for (int i = 1; i <= FORUM_CATEGORY_COUNT; i++)
            {
                forumCategories.add(new ForumCategory("Discussion on " + language + " Tips #" + i, "A very informative description on " + language + " tips #" + i));
            }
        }
    }

    private void addForumThreads()
    {
        for (String language : PROGRAMMING_LANGUAGES)
        {
            for (int i = 1; i <= FORUM_CATEGORY_COUNT; i++)
            {
                for (int j = 1; j <= FORUM_THREAD_COUNT; j++)
                {
                    forumThreads.add(new ForumThread("Thread #" + j + " on " + language + " Tips #" + i, "Thread #" + j + " description on " + language + " Tips #" + i));
                }
            }
        }
    }

    private void addForumPosts()
    {
        for (String language : PROGRAMMING_LANGUAGES)
        {
            for (int i = 1; i <= FORUM_CATEGORY_COUNT; i++)
            {
                for (int j = 1; j <= FORUM_THREAD_COUNT; j++)
                {
                    for (int k = 1; k <= FORUM_POST_COUNT; k++)
                    {
                        forumPosts.add(new ForumPost("Post #" + k + " on " + language));
                    }
                }
            }
        }
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

    private void printTime()
    {
        System.out.printf("\n===== %f seconds =====\n", ((double) (System.currentTimeMillis() - START_TIME)) / 1000);
    }
}

