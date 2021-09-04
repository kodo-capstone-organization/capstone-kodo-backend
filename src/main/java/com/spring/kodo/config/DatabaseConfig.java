package com.spring.kodo.config;

import com.spring.kodo.entity.*;
import com.spring.kodo.service.inter.*;
import com.spring.kodo.util.enumeration.MultimediaType;
import com.spring.kodo.util.enumeration.QuestionType;
import com.spring.kodo.util.exception.InputDataValidationException;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
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

    // Edit these to scale the sample database
    private final Integer PROGRAMMING_LANGUAGES_COUNT = 14; // Current max is 14

    private final Integer PREFIXED_ADMIN_COUNT = 1;
    private final Integer TUTOR_COUNT = 5;
    private final Integer PREFIXED_TUTOR_COUNT = 1;
    private final Integer STUDENT_COUNT = 25;
    private final Integer PREFIXED_STUDENT_COUNT = 2;

    private final Integer LESSON_COUNT = 3;
    private final Integer QUIZ_QUESTION_COUNT = 5;
    private final Integer QUIZ_QUESTION_OPTION_COUNT = 4;
    private final Integer STUDENT_ATTEMPT_COUNT = 3;

    private final Integer FORUM_CATEGORY_COUNT = 3;
    private final Integer FORUM_THREAD_COUNT = 3;
    private final Integer FORUM_POST_COUNT = 3;

    // Don't Edit these
    private final Integer ADMIN_FIRST_INDEX = 0; // 0
    private final Integer ADMIN_LAST_INDEX = ADMIN_FIRST_INDEX + PREFIXED_ADMIN_COUNT; // 1
    private final Integer STUDENT_FIRST_INDEX = ADMIN_LAST_INDEX + 1; // 2
    private final Integer STUDENT_LAST_INDEX = STUDENT_FIRST_INDEX + STUDENT_COUNT - 1; // 51
    private final Integer TUTOR_FIRST_INDEX = STUDENT_LAST_INDEX + 1; // 52
    private final Integer TUTOR_LAST_INDEX = TUTOR_FIRST_INDEX + TUTOR_COUNT - 1; // 56

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
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadDataOnStartup() throws Exception
    {
        long start = System.currentTimeMillis();

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
        List<Account> accounts = addAccounts();
        List<Tag> tags = addTags();
        List<Course> courses = addCourses();
        List<Lesson> lessons = addLessons();
        List<Quiz> quizzes = addQuizzes();
        List<QuizQuestion> quizQuestions = addQuizQuestions();
        List<QuizQuestionOption> quizQuestionOptions = addQuizQuestionOptions();
        List<Multimedia> multimedias = addMultimedias();
        List<ForumCategory> forumCategories = addForumCategories();
        List<ForumThread> forumThreads = addForumThreads();
        List<ForumPost> forumPosts = addForumPosts();

        // Create data set to Database
        System.out.println("\n===== 1.2. Creating Data Lists to Database =====");
        create(accounts,
                tags,
                courses,
                lessons,
                quizzes,
                quizQuestions,
                quizQuestionOptions,
                multimedias,
                forumCategories,
                forumThreads,
                forumPosts
        );

        // Print Ids of saved data list
        System.out.println("\n===== 1.3. Print IDs =====");
        printIds();

        System.out.println("\n===== Init Data Fully Loaded to Database =====");
        long end = System.currentTimeMillis();
        System.out.printf("\n===== %f seconds =====\n", ((double) (end - start)) / 1000);
    }

    private void create(
            List<Account> accounts,
            List<Tag> tags,
            List<Course> courses,
            List<Lesson> lessons,
            List<Quiz> quizzes,
            List<QuizQuestion> quizQuestions,
            List<QuizQuestionOption> quizQuestionOptions,
            List<Multimedia> multimedias,
            List<ForumCategory> forumCategories,
            List<ForumThread> forumThreads,
            List<ForumPost> forumPosts
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
        int forumCategoryIndex = 0;
        int forumThreadIndex = 0;
        int forumPostIndex = 0;
        int quizIndex = 0;
        int quizQuestionIndex = 0;
        int quizQuestionOptionIndex = 0;
        int multimediaIndex = 0;

        Account tutor;
        Account student;
        Course course;
        Tag tag;
        Lesson lesson;
        Quiz quiz;
        QuizQuestion quizQuestion;
        QuizQuestionOption quizQuestionOption;
        EnrolledCourse enrolledCourse;
        StudentAttemptAnswer studentAttemptAnswer;
        CompletedLesson completedLesson;
        Multimedia multimedia;
        ForumCategory forumCategory;
        ForumThread forumThread;
        ForumPost forumPost;

        while (courseIndex < courses.size()
                && tagIndex < tags.size()
                && lessonIndex < lessons.size()
                && forumCategoryIndex < forumCategories.size()
                && forumThreadIndex < forumThreads.size()
                && forumPostIndex < forumPosts.size()
                && quizIndex < quizzes.size()
                && quizQuestionIndex < quizQuestions.size()
                && quizQuestionOptionIndex < quizQuestionOptions.size()
                && multimediaIndex < multimedias.size())
        {
            tutor = accounts.get(getRandomNumber(TUTOR_FIRST_INDEX, TUTOR_LAST_INDEX));
            course = courses.get(courseIndex++);
            tag = tags.get(tagIndex++);

            // Course Creation
            course = courseService.createNewCourse(
                    course,
                    tutor.getAccountId(),
                    Arrays.asList(tag.getTitle())
            );

            // Link Account - Course
            tutor = accountService.addCourseToAccount(tutor, course);

            // ForumCategory Creation
            for (int j = 0; j < FORUM_CATEGORY_COUNT; j++, forumCategoryIndex++)
            {
                forumCategory = forumCategoryService.createNewForumCategory(forumCategories.get(forumCategoryIndex), course.getCourseId());
//                    courseService.addForumCategoryToCourse(course, forumCategory);

                for (int k = 0; k < FORUM_THREAD_COUNT; k++, forumThreadIndex++)
                {
                    forumThread = forumThreadService.createNewForumThread(forumThreads.get(forumThreadIndex), (long) getRandomNumber(STUDENT_FIRST_INDEX, STUDENT_LAST_INDEX));
                    forumCategoryService.addForumThreadToForumCategory(forumCategory, forumThread);

                    for (int l = 0; l < FORUM_POST_COUNT; l++, forumPostIndex++)
                    {
                        forumPost = forumPostService.createNewForumPost(forumPosts.get(forumPostIndex), (long) getRandomNumber(STUDENT_FIRST_INDEX, STUDENT_LAST_INDEX));
                        forumThreadService.addForumPostToForumThread(forumThread, forumPost);
                    }
                }
            }

            for (int i = 0; i < LESSON_COUNT; i++, lessonIndex++, quizIndex++, multimediaIndex++)
            {
                lesson = lessons.get(lessonIndex);
                quiz = quizzes.get(quizIndex);
                multimedia = multimedias.get(multimediaIndex);

                // Lesson Creation
                lesson = lessonService.createNewLesson(lesson);

                // Link Course - Lesson
                course = courseService.addLessonToCourse(course, lesson);

                for (int j = 0; j < QUIZ_QUESTION_COUNT; j++, quizQuestionIndex++, quizQuestionOptionIndex += QUIZ_QUESTION_OPTION_COUNT)
                {
                    // Link Quiz - QuizQuestion
                    quizService.addQuizQuestionToQuiz(
                            // Quiz Creation
                            quizService.createNewQuiz(quiz),
                            // Link QuizQuestion - QuizQuestionOptions
                            quizQuestionService.addQuizQuestionOptionsToQuizQuestion(
                                    // QuizQuestion Creation
                                    quizQuestionService.createNewQuizQuestion(quizQuestions.get(quizQuestionIndex), quiz.getContentId()),
                                    // QuizQuestionOptions Creation
                                    quizQuestionOptionService.createNewQuizQuestionOptions(
                                            quizQuestionOptions.subList(quizQuestionOptionIndex, quizQuestionOptionIndex + QUIZ_QUESTION_OPTION_COUNT)
                                    )));
                }

                // Link Lesson - Quiz
                lesson = lessonService.addContentToLesson(lesson, quiz);

                // StudentAttempt Creation
                for (int j = 0; j < STUDENT_ATTEMPT_COUNT; j++)
                {
                    student = accounts.get(getRandomNumber(STUDENT_FIRST_INDEX, STUDENT_LAST_INDEX));

                    // Create EnrolledCourse
                    try
                    {
                        enrolledCourse = enrolledCourseService.createNewEnrolledCourse(student.getAccountId(), course.getCourseId());

                        // Link Student (Account) - EnrolledCourse
                        student = accountService.addEnrolledCourseToAccount(student, enrolledCourse);

                        // CompletedLesson Creation
                        completedLesson = completedLessonService.createNewCompletedLesson(lesson.getLessonId());

                        // Link EnrolledCourse - CompletedLesson
                        enrolledCourse = enrolledCourseService.addCompletedLessonToEnrolledCourse(enrolledCourse, completedLesson);
                    }
                    catch (Exception ex)
                    {
                        enrolledCourse = enrolledCourseService.getEnrolledCourseByStudentIdAndCourseName(student.getAccountId(), course.getName());
                    }

                    // Create StudentAttempt
                    StudentAttempt studentAttempt = studentAttemptService.createNewStudentAttempt(quiz.getContentId());

                    // Link Student (Account) - StudentAttempt
                    student = accountService.addStudentAttemptToAccount(student, studentAttempt);

                    // StudentAttemptQuestion Creation
                    for (StudentAttemptQuestion studentAttemptQuestion : studentAttempt.getStudentAttemptQuestions())
                    {
                        quizQuestion = studentAttemptQuestion.getQuizQuestion();
                        quizQuestionOption = quizQuestion.getQuizQuestionOptions().get(
                                getRandomNumber(
                                        0,
                                        quizQuestion.getQuizQuestionOptions().size() - 1)
                        );

                        // Create StudentAttemptAnswer
                        studentAttemptAnswer = studentAttemptAnswerService.createNewStudentAttemptAnswer(quizQuestionOption.getQuizQuestionOptionId());

                        // Link StudentAttemptQuestion - StudentAttemptAnswer
                        studentAttemptQuestion = studentAttemptQuestionService.addStudentAttemptAnswerToStudentAttemptQuestion(studentAttemptQuestion, studentAttemptAnswer);
                    }
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

        List<Long> forumCategoryIds = forumCategoryService.getAllForumCategories().stream().map(ForumCategory::getForumCategoryId).collect(Collectors.toList());
        System.out.println(">> Added ForumCategories with forumCategoryIds: " + forumCategoryIds);

        List<Long> forumThreadIds = forumThreadService.getAllForumThreads().stream().map(ForumThread::getForumThreadId).collect(Collectors.toList());
        System.out.println(">> Added ForumThreads with forumThreadIds: " + forumThreadIds);

        List<Long> forumPostIds = forumPostService.getAllForumPosts().stream().map(ForumPost::getForumPostId).collect(Collectors.toList());
        System.out.println(">> Added ForumPosts with forumPostIds: " + forumPostIds);

        List<Long> enrolledCourseIds = enrolledCourseService.getAllEnrolledCourses().stream().map(EnrolledCourse::getEnrolledCourseId).collect(Collectors.toList());
        System.out.println(">> Added EnrolledCourses with enrolledCourseIds: " + enrolledCourseIds);

        List<Long> lessonIds = lessonService.getAllLessons().stream().map(Lesson::getLessonId).collect(Collectors.toList());
        System.out.println(">> Added Lessons with lessonIds: " + lessonIds);

        List<Long> quizIds = quizService.getAllQuizzes().stream().map(Quiz::getContentId).collect(Collectors.toList());
        System.out.println(">> Added Quizzes with quizIds: " + quizIds);

        List<Long> quizQuestionIds = quizQuestionService.getAllQuizQuestions().stream().map(QuizQuestion::getQuizQuestionId).collect(Collectors.toList());
        System.out.println(">> Added QuizQuestions with quizQuestionIds: " + quizQuestionIds);

        List<Long> quizQuestionOptionIds = quizQuestionOptionService.getAllQuizQuestionOptions().stream().map(QuizQuestionOption::getQuizQuestionOptionId).collect(Collectors.toList());
        System.out.println(">> Added QuizQuestionOptions with quizQuestionOptionIds: " + quizQuestionOptionIds);

        List<Long> multimediaIds = multimediaService.getAllMultimedias().stream().map(Multimedia::getContentId).collect(Collectors.toList());
        System.out.println(">> Added Multimedias with multimediaIds: " + multimediaIds);

        List<Long> studentAttemptIds = studentAttemptService.getAllStudentAttempts().stream().map(StudentAttempt::getStudentAttemptId).collect(Collectors.toList());
        System.out.println(">> Added StudentAttempts with studentAttemptIds: " + studentAttemptIds);

        List<Long> studentAttemptQuestionIds = studentAttemptQuestionService.getAllStudentAttemptQuestions().stream().map(StudentAttemptQuestion::getStudentAttemptQuestionId).collect(Collectors.toList());
        System.out.println(">> Added StudentAttemptQuestions with studentAttemptIds: " + studentAttemptQuestionIds);

        List<Long> studentAttemptAnswerIds = studentAttemptAnswerService.getAllStudentAttemptAnswers().stream().map(StudentAttemptAnswer::getStudentAttemptAnswerId).collect(Collectors.toList());
        System.out.println(">> Added StudentAttemptAnswers with studentAttemptIds: " + studentAttemptAnswerIds);

        List<Long> completedLessonIds = completedLessonService.getAllCompletedLessons().stream().map(CompletedLesson::getCompletedLessonId).collect(Collectors.toList());
        System.out.println(">> Added CompletedLessons with completedLessonIds: " + completedLessonIds);
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
            accounts.add(new Account("tutor" + i, "password", "Tutor " + i, "I am Tutor " + i, "tutor" + i + "@gmail.com", "https://tutor" + i + ".com", false));
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

    private List<ForumCategory> addForumCategories()
    {
        List<ForumCategory> forumCategories = new ArrayList<>();

        for (String language : PROGRAMMING_LANGUAGES)
        {
            for (int i = 1; i <= FORUM_CATEGORY_COUNT; i++)
            {
                forumCategories.add(new ForumCategory("Discussion on " + language + " Tips #" + i, "A very informative description on " + language + " tips #" + i));
            }
        }

        return forumCategories;
    }

    private List<ForumThread> addForumThreads()
    {
        List<ForumThread> forumThreads = new ArrayList<>();

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

        return forumThreads;
    }

    private List<ForumPost> addForumPosts()
    {
        List<ForumPost> forumPosts = new ArrayList<>();

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

        return forumPosts;
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

