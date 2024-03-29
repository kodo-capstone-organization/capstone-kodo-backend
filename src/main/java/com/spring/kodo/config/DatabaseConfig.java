package com.spring.kodo.config;

import com.spring.kodo.entity.*;
import com.spring.kodo.service.inter.*;
import com.spring.kodo.util.helper.FormatterHelper;
import com.spring.kodo.util.helper.RandomGeneratorHelper;
import com.spring.kodo.util.enumeration.MultimediaType;
import com.spring.kodo.util.enumeration.QuestionType;
import com.spring.kodo.util.exception.InvalidDateTimeOfCompletionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.spring.kodo.util.helper.ConstantsHelper.*;

@Configuration
public class DatabaseConfig
{
    @Value("${CONFIG_PROFILE_TYPE}")
    private String configProfileType;

    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    private ForumPostService forumPostService;

    @Autowired
    private ForumThreadService forumThreadService;

    @Autowired
    private ForumCategoryService forumCategoryService;

    @Autowired
    private EnrolledContentService enrolledContentService;

    @Autowired
    private EnrolledLessonService enrolledLessonService;

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
    private TransactionService transactionService;

    @Autowired
    private Environment env;

    private List<Account> accounts;
    private List<Account> randomAccounts;
    private List<Tag> tags;
    private List<Course> courses;
    private List<Lesson> lessons;
    private List<Quiz> quizzes;
    private List<QuizQuestion> quizQuestions;
    private List<QuizQuestionOption> quizQuestionOptions;
    private List<Multimedia> multimedias;
    private List<EnrolledCourse> enrolledCourses;
    private List<EnrolledLesson> enrolledLessons;
    private List<EnrolledContent> enrolledContents;
    private List<StudentAttempt> studentAttempts;
    private List<StudentAttemptQuestion> studentAttemptQuestions;
    private List<StudentAttemptAnswer> studentAttemptAnswers;
    private List<ForumCategory> forumCategories;
    private List<ForumThread> forumThreads;
    private List<ForumPost> forumPosts;

    private LocalDateTime COURSE_REFERENCE_DATE = LocalDateTime.now();

    private final Integer LANGUAGES_COUNT = 5; // Current max is 25

    private final Integer TUTOR_COUNT = 15;
    private final Integer STUDENT_COUNT = 10;

    private final Integer RANDOM_STUDENT_COUNT = 100;

    private final Integer LESSON_COUNT = 3;

    private final Integer MULTIMEDIA_COUNT = 5;

    private final Integer QUIZ_COUNT = 1;
    private final Integer QUIZ_QUESTION_COUNT = 6;
    private final Integer QUIZ_QUESTION_OPTION_COUNT = 3;

    private final Integer STUDENT_ENROLLED_COUNT = (int) (0.5 * (LANGUAGES_COUNT * STUDENT_COUNT));
    private final Integer STUDENT_ATTEMPT_COUNT = (int) (0.5 * STUDENT_ENROLLED_COUNT * LESSON_COUNT * QUIZ_COUNT);

    private final Integer FORUM_CATEGORY_COUNT = 3;
    private final Integer FORUM_THREAD_COUNT = 3;
    private final Integer FORUM_POST_COUNT = 3;
    private final Integer FORUM_POST_REPLY_COUNT = 1;

    private final Integer COMPLETE_CONTENT_COUNT = (int) (0.1 * (STUDENT_ENROLLED_COUNT * LESSON_COUNT * (MULTIMEDIA_COUNT + QUIZ_COUNT)));

    private final Integer RATE_ENROLLED_COURSES_COUNT = (int) (0.85 * STUDENT_ENROLLED_COUNT);

    // Timing variables
    private final Integer CONTENT_COMPLETION_MIN_MONTH = 0;
    private final Integer CONTENT_COMPLETION_MAX_MONTH = 3;

    private final Integer FORUM_POST_CREATION_MIN_MONTH = 0;
    private final Integer FORUM_POST_CREATION_MAX_MONTH = 3;

    private final Integer FORUM_THREAD_CREATION_MIN_MONTH = FORUM_POST_CREATION_MAX_MONTH + 1;
    private final Integer FORUM_THREAD_CREATION_MAX_MONTH = FORUM_POST_CREATION_MAX_MONTH + 1 + 3;

    private final Integer TRANSACTION_MIN_MONTH = FORUM_THREAD_CREATION_MAX_MONTH + 1;
    private final Integer TRANSACTION_MAX_MONTH = FORUM_THREAD_CREATION_MAX_MONTH + 1 + 3;

    private final Integer COURSE_CREATION_MIN_MONTH = TRANSACTION_MAX_MONTH + 1;
    private final Integer COURSE_CREATION_MAX_MONTH = TRANSACTION_MAX_MONTH + 1 + 3;

    private final Integer ACCOUNT_CREATION_MIN_MONTH = COURSE_CREATION_MAX_MONTH + 1;
    private final Integer ACCOUNT_CREATION_MAX_MONTH = COURSE_CREATION_MAX_MONTH + 1 + 3;

    private final Integer RANDOM_ACCOUNT_CREATION_MIN_MONTH = 0;
    private final Integer RANDOM_ACCOUNT_CREATION_MAX_MONTH = 12;

    // Don't Edit these
    private final Integer PREFIXED_ADMIN_COUNT = 8;
    private final Integer PREFIXED_TUTOR_COUNT = 0;
    private final Integer PREFIXED_STUDENT_COUNT = 0;

    private final Integer ADMIN_FIRST_INDEX = 0;
    private final Integer ADMIN_SIZE = PREFIXED_ADMIN_COUNT + ADMIN_FIRST_INDEX;
    private final Integer STUDENT_FIRST_INDEX = ADMIN_SIZE;
    private final Integer STUDENT_SIZE = PREFIXED_STUDENT_COUNT + STUDENT_FIRST_INDEX + STUDENT_COUNT;
    private final Integer TUTOR_FIRST_INDEX = STUDENT_SIZE;
    private final Integer TUTOR_SIZE = PREFIXED_TUTOR_COUNT + TUTOR_FIRST_INDEX + TUTOR_COUNT;

    private final Long START_TIME;

    public DatabaseConfig()
    {
        accounts = new ArrayList<>();
        randomAccounts = new ArrayList<>();
        tags = new ArrayList<>();
        courses = new ArrayList<>();
        lessons = new ArrayList<>();
        quizzes = new ArrayList<>();
        quizQuestions = new ArrayList<>();
        quizQuestionOptions = new ArrayList<>();
        multimedias = new ArrayList<>();
        enrolledCourses = new ArrayList<>();
        enrolledLessons = new ArrayList<>();
        enrolledContents = new ArrayList<>();
        studentAttempts = new ArrayList<>();
        studentAttemptQuestions = new ArrayList<>();
        studentAttemptAnswers = new ArrayList<>();
        forumCategories = new ArrayList<>();
        forumThreads = new ArrayList<>();
        forumPosts = new ArrayList<>();

        START_TIME = System.currentTimeMillis();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onStartUp() throws Exception
    {
        // Stop Heroku from updating Google Cloud SQL on every git change
        if (configProfileType.toLowerCase(Locale.ROOT).equals("dev"))
        {
            Scanner scanner = new Scanner(System.in);

            System.out.print("1. Reload Data\n");
            System.out.print("2. Do Nothing\n");
            System.out.print("> ");

            int option = scanner.nextInt();

            if (option == 1)
            {
                dataSourceService.createDatabase();
                dataSourceService.truncateAllTables();
                loadData();
            }
            else
            {
                System.out.println("\n===== Application started on port: " + env.getProperty("local.server.port") + " =====");
                System.out.println("\n===== No New Data Loaded to Database =====");
            }

            scanner.close();
        }
        else
        {
            System.out.println("\n===== Application started on port: " + env.getProperty("local.server.port") + " =====");
            System.out.println("\n===== No New Data Loaded to Database =====");
        }
    }

    public void loadData() throws Exception
    {
        System.out.println("\n===== Application started on port: " + env.getProperty("local.server.port") + " =====");
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
        createEnrolledCoursesAndEnrolledLessonsEnrolledContents();
        createStudentAttemptsAndStudentAttemptQuestionsAndStudentAttemptAnswersAndCompleteContentAndMarkStudentAttempt();
        createForumCategories();
        createForumThreads();
        createForumPosts();
        completeContent();
        rateEnrolledCourses();
        createRandomAccountsAndEnrolledCourses();

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
        List<Tag> selectedTags;
        int tagCount = 3;
        int tagIndex = 0;

        for (int i = ADMIN_FIRST_INDEX; i < ADMIN_SIZE; i++)
        {
            if (accounts.get(i).getUsername().equals("tanweekek"))
            {
                accountService.createNewAccount(accounts.get(i), Arrays.asList("Java", "IOT", "Mobile", "Angular", "JSF", "PrimeFaces"));
            }
            else
            {
                accountService.createNewAccount(accounts.get(i), null);
            }
        }

        for (int i = STUDENT_FIRST_INDEX; i < STUDENT_SIZE; i++, tagIndex++)
        {
            if (tagIndex + tagCount >= tags.size() - 1)
            {
                tagIndex = 0;
            }

            selectedTags = tags.subList(tagIndex, tagIndex + tagCount);

            accountService.createNewAccount(accounts.get(i), selectedTags.stream().map(Tag::getTitle).collect(Collectors.toList()));
        }

        for (int i = TUTOR_FIRST_INDEX; i < TUTOR_SIZE; i++, tagIndex++)
        {
            if (tagIndex + tagCount >= tags.size() - 1)
            {
                tagIndex = 0;
            }

            selectedTags = tags.subList(tagIndex, tagIndex + tagCount);

            accountService.createNewAccount(accounts.get(i), selectedTags.stream().map(Tag::getTitle).collect(Collectors.toList()));
        }

        accounts = accountService.getAllAccounts();
        accounts.get(TUTOR_FIRST_INDEX).setStripeAccountId("acct_1JZ8h5PDe56gk95T");

        System.out.printf(">> Created Accounts (%d)\n", accounts.size());
    }

    private void createCourses() throws Exception
    {
        Account tutor;
        Course course;
        String tagTitle;

        List<String> tagTitles = new ArrayList<>();

        int tutorIndex = TUTOR_FIRST_INDEX;
        int courseIndex = 0;

        for (int i = 0; i < tags.size(); i++, tutorIndex++)
        {
            tutor = accounts.get(tutorIndex);
            tagTitle = tags.get(i).getTitle();

            for (int j = 0; j < LEVELS.size(); j++, courseIndex++)
            {
                course = courses.get(courseIndex);

                tagTitles.add(tagTitle);
                tagTitles.add(LEVELS.get(j).toLowerCase(Locale.ROOT));

                courseService.createNewCourse(course, tagTitles);

                accountService.updateAccount(tutor);
                accountService.addCourseToAccount(tutor, course);

                tagTitles.clear();

                if (courseIndex == courses.size() - 1)
                {
                    break;
                }
            }

            if (tutorIndex == TUTOR_SIZE - 1)
            {
                tutorIndex = TUTOR_FIRST_INDEX;
            }

            if (courseIndex == courses.size() - 1)
            {
                break;
            }
        }

        courses = courseService.getAllCourses();

        System.out.printf(">> Created Courses (%d)\n", courses.size());
    }

    private void createLessons() throws Exception
    {
        Course course;
        Lesson lesson;

        int courseIndex = 0;
        int lessonIndex = 0;

        for (int i = 0; i < courses.size(); i++, courseIndex++)
        {
            course = courses.get(courseIndex);

            for (int k = 0; k < LESSON_COUNT; k++, lessonIndex++)
            {
                lesson = lessons.get(lessonIndex);

                lessonService.createNewLesson(lesson);
                courseService.addLessonToCourse(course, lesson);
            }
        }

        lessons = lessonService.getAllLessons();

        System.out.printf(">> Created Lessons (%d)\n", lessons.size());
    }

    private void createQuizzes() throws Exception
    {
        Quiz quiz;

        int quizIndex = 0;

        for (Course course : courses)
        {
            for (Lesson lesson : course.getLessons())
            {
                for (int l = 0; l < QUIZ_COUNT; l++, quizIndex++)
                {
                    quiz = quizzes.get(quizIndex);
                    quiz = quizService.createNewQuiz(quiz);

                    lessonService.addContentToLesson(lesson, quiz);
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

                        if (quizQuestion.getQuestionType().equals(QuestionType.MCQ) || quizQuestion.getQuestionType().equals(QuestionType.MATCHING))
                        {
                            for (int a = 0; a < QUIZ_QUESTION_OPTION_COUNT; a++, quizQuestionOptionIndex++)
                            {
                                quizQuestionOption = quizQuestionOptionService.createNewQuizQuestionOption(quizQuestionOptions.get(quizQuestionOptionIndex));

                                quizQuestionService.addQuizQuestionOptionToQuizQuestion(quizQuestion, quizQuestionOption);
                            }
                        }
                        else if (quizQuestion.getQuestionType().equals(QuestionType.TF))
                        {
                            for (int a = 0; a < 2; a++, quizQuestionOptionIndex++)
                            {
                                quizQuestionOption = quizQuestionOptionService.createNewQuizQuestionOption(quizQuestionOptions.get(quizQuestionOptionIndex));

                                quizQuestionService.addQuizQuestionOptionToQuizQuestion(quizQuestion, quizQuestionOption);
                            }
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

        for (Course course : courses)
        {
            for (Lesson lesson : course.getLessons())
            {
                for (int i = 0; i < MULTIMEDIA_COUNT; i++, multimediaIndex++)
                {
                    multimedia = multimediaService.createNewMultimedia(multimedias.get(multimediaIndex));
                    lessonService.addContentToLesson(lesson, multimedia);
                }
            }
        }

        multimedias = multimediaService.getAllMultimedias();

        System.out.printf(">> Created Multimedias (%d)\n", multimedias.size());
    }

    private void createEnrolledCoursesAndEnrolledLessonsEnrolledContents() throws Exception
    {
        int i = 0;

        // Ensure fixed students
        while (i < STUDENT_ENROLLED_COUNT / 2)
        {
            for (int courseIndex = 0; courseIndex < courses.size(); courseIndex++)
            {
                for (int studentIndex = STUDENT_FIRST_INDEX; studentIndex < STUDENT_SIZE && i < STUDENT_ENROLLED_COUNT / 2; studentIndex++, i++)
                {
                    try
                    {
                        createEnrolledCourseAndEnrolledLessonsAndEnrolledContents(accounts.get(studentIndex), courseIndex, TRANSACTION_MIN_MONTH, TRANSACTION_MAX_MONTH);
                    }
                    catch (Exception ex)
                    {
                        i--;
                    }
                }
            }
        }

        // Randomisation for data variation
        while (i < STUDENT_ENROLLED_COUNT)
        {
            int courseIndex = RandomGeneratorHelper.getRandomInteger(0, (courses.size() - 1) * 3 / 4);
            int studentIndex = RandomGeneratorHelper.getRandomInteger(STUDENT_FIRST_INDEX, (STUDENT_SIZE - 1));

            try
            {
                createEnrolledCourseAndEnrolledLessonsAndEnrolledContents(accounts.get(studentIndex), courseIndex, TRANSACTION_MIN_MONTH, TRANSACTION_MAX_MONTH);
                i++;
            }
            catch (Exception ex)
            {
            }
        }

        enrolledCourses = enrolledCourseService.getAllEnrolledCourses();
        enrolledLessons = enrolledLessonService.getAllEnrolledLessons();
        enrolledContents = enrolledContentService.getAllEnrolledContents();

        System.out.printf(">> Created EnrolledCourses (%d)\n", enrolledCourses.size());
        System.out.printf(">> Created EnrolledLessons (%d)\n", enrolledLessons.size());
        System.out.printf(">> Created EnrolledContents (%d)\n", enrolledContents.size());
    }

    private void createEnrolledCourseAndEnrolledLessonsAndEnrolledContents(Account student, int courseIndex, int transactionMinMonth, int transactionMaxMonth) throws Exception
    {
        Account tutor;
        Course course;
        EnrolledCourse enrolledCourse;
        EnrolledLesson enrolledLesson;
        EnrolledContent enrolledContent;
        Transaction transaction;

        String dummyUniqueStripeSessionId;

        course = courses.get(courseIndex);
        tutor = accountService.getAccountByCourseId(course.getCourseId());
        dummyUniqueStripeSessionId = "acct_" + RandomGeneratorHelper.getRandomString(16);

        enrolledCourse = enrolledCourseService.createNewEnrolledCourse(student.getAccountId(), course.getCourseId());
        accountService.addEnrolledCourseToAccount(student, enrolledCourse);

        // Create transaction records assuming students successfully made the payments
        // Using a unique value in place of a real stripe sessionId
        transaction = new Transaction(dummyUniqueStripeSessionId, course.getPrice());
        transaction.setDateTimeOfTransaction(getNextDateTime(transactionMinMonth, transactionMaxMonth));
        transactionService.createNewTransaction(transaction, student.getAccountId(), tutor.getAccountId(), course.getCourseId());

        for (Lesson lesson : course.getLessons())
        {
            enrolledLesson = enrolledLessonService.createNewEnrolledLesson(lesson.getLessonId());
            enrolledCourseService.addEnrolledLessonToEnrolledCourse(enrolledCourse, enrolledLesson);

            for (Content content : lesson.getContents())
            {
                enrolledContent = enrolledContentService.createNewEnrolledContent(content.getContentId());
                enrolledLessonService.addEnrolledContentToEnrolledLesson(enrolledLesson, enrolledContent);
            }
        }
    }

    private void createStudentAttemptsAndStudentAttemptQuestionsAndStudentAttemptAnswersAndCompleteContentAndMarkStudentAttempt() throws Exception
    {
        boolean change = true;

        EnrolledContent enrolledContent;
        StudentAttempt studentAttempt;
        StudentAttemptAnswer studentAttemptAnswer;

        int studentAttemptCounter = 0;
        int markedStudentAttempts = 0;

        for (int i = 0; i < enrolledContents.size(); i++)
        {
            enrolledContent = enrolledContents.get(i);

            try
            {
                studentAttempt = studentAttemptService.createNewStudentAttempt(enrolledContent.getEnrolledContentId());

                enrolledContentService.addStudentAttemptToEnrolledContent(enrolledContent, studentAttempt);

                for (StudentAttemptQuestion studentAttemptQuestion : studentAttempt.getStudentAttemptQuestions())
                {
                    if (studentAttemptQuestion.getQuizQuestion().getQuestionType().equals(QuestionType.MATCHING))
                    {
                        if (change)
                        {
                            for (QuizQuestionOption quizQuestionOption : studentAttemptQuestion.getQuizQuestion().getQuizQuestionOptions())
                            {
                                studentAttemptAnswer = studentAttemptAnswerService.createNewStudentAttemptAnswer(quizQuestionOption.getQuizQuestionOptionId(), quizQuestionOption.getQuizQuestionOptionId());
                                studentAttemptQuestionService.addStudentAttemptAnswerToStudentAttemptQuestion(studentAttemptQuestion, studentAttemptAnswer);
                            }
                        }
                        else
                        {
                            QuizQuestionOption quizQuestionOption1;
                            QuizQuestionOption quizQuestionOption2;
                            List<QuizQuestionOption> quizQuestionOptions = studentAttemptQuestion.getQuizQuestion().getQuizQuestionOptions();

                            for (int j = 0; j < quizQuestionOptions.size() - 1; j++)
                            {
                                quizQuestionOption1 = quizQuestionOptions.get(j);
                                quizQuestionOption2 = quizQuestionOptions.get(j + 1);

                                studentAttemptAnswer = studentAttemptAnswerService.createNewStudentAttemptAnswer(quizQuestionOption1.getQuizQuestionOptionId(), quizQuestionOption2.getQuizQuestionOptionId());
                                studentAttemptQuestionService.addStudentAttemptAnswerToStudentAttemptQuestion(studentAttemptQuestion, studentAttemptAnswer);
                            }

                            quizQuestionOption1 = quizQuestionOptions.get(quizQuestionOptions.size() - 1);
                            quizQuestionOption2 = quizQuestionOptions.get(0);

                            studentAttemptAnswer = studentAttemptAnswerService.createNewStudentAttemptAnswer(quizQuestionOption1.getQuizQuestionOptionId(), quizQuestionOption2.getQuizQuestionOptionId());
                            studentAttemptQuestionService.addStudentAttemptAnswerToStudentAttemptQuestion(studentAttemptQuestion, studentAttemptAnswer);
                        }
                    }
                    else
                    {
                        QuizQuestionOption quizQuestionOption = studentAttemptQuestion.getQuizQuestion().getQuizQuestionOptions().get(change ? 0 : 1);
                        studentAttemptAnswer = studentAttemptAnswerService.createNewStudentAttemptAnswer(quizQuestionOption.getQuizQuestionOptionId());
                        studentAttemptQuestionService.addStudentAttemptAnswerToStudentAttemptQuestion(studentAttemptQuestion, studentAttemptAnswer);
                    }

                    change = !change;
                }
                studentAttemptCounter++;

                // User Click Submit
                enrolledContentService.setFakeDateTimeOfCompletionOfEnrolledContentByEnrolledContentId(getNextDateTime(0, 3), enrolledContent.getEnrolledContentId());

                studentAttemptService.markStudentAttemptByStudentAttemptId(studentAttempt.getStudentAttemptId());
                markedStudentAttempts++;

                if (studentAttemptCounter == STUDENT_ATTEMPT_COUNT)
                {
                    break;
                }
            }
            catch (Exception ex)
            {
            }
        }

        studentAttempts = studentAttemptService.getAllStudentAttempts();
        studentAttemptQuestions = studentAttemptQuestionService.getAllStudentAttemptQuestions();
        studentAttemptAnswers = studentAttemptAnswerService.getAllStudentAttemptAnswers();

        System.out.printf(">> Created StudentAttempts (%d)\n", studentAttempts.size());
        System.out.printf(">> Created StudentAttemptQuestions (%d)\n", studentAttemptQuestions.size());
        System.out.printf(">> Created StudentAttemptAnswers (%d)\n", studentAttemptAnswers.size());
        System.out.printf(">> Marked StudentAttempts (%d)\n", markedStudentAttempts);
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
        List<Account> accountsInCourse;

        ForumCategory forumCategory;
        ForumThread forumThread;
        Account account;

        int forumCategoryIndex = 0;
        int forumThreadIndex = 0;
        int accountIndex = 0;

        for (Course course : courses)
        {
            accountIndex = 0;
            accountsInCourse = accounts.stream().filter(a -> enrolledCourseService.isEnrolledInCourseByStudentIdAndCourseId(a.getAccountId(), course.getCourseId())).toList();

            if (accountsInCourse.size() > 0)
            {
                for (int i = 0; i < FORUM_CATEGORY_COUNT; i++, forumCategoryIndex++)
                {
                    forumCategory = forumCategories.get(forumCategoryIndex);

                    for (int j = 0; j < FORUM_THREAD_COUNT; j++, forumThreadIndex++, accountIndex++)
                    {
                        if (accountIndex >= accountsInCourse.size() - 1)
                        {
                            accountIndex = 0;
                        }

                        account = accountsInCourse.get(accountIndex);

                        forumThread = forumThreads.get(forumThreadIndex);

                        forumThreadService.createNewForumThread(forumThread, account.getAccountId());

                        forumCategoryService.addForumThreadToForumCategory(forumCategory, forumThread);
                    }
                }
            }
        }

        forumThreads = forumThreadService.getAllForumThreads();

        System.out.printf(">> Created ForumThreads (%d)\n", forumThreads.size());
    }

    private void createForumPosts() throws Exception
    {
        List<Account> accountsInCourse;

        ForumThread forumThread;
        ForumPost forumPost;
        ForumPost forumPostReply;
        Account account;

        int forumThreadIndex = 0;
        int forumPostIndex = 0;
        int accountIndex;

        for (Course course : courses)
        {
            accountIndex = 0;
            accountsInCourse = accounts.stream().filter(a -> enrolledCourseService.isEnrolledInCourseByStudentIdAndCourseId(a.getAccountId(), course.getCourseId())).toList();

            if (accountsInCourse.size() > 0)
            {
                for (int i = 0; i < FORUM_CATEGORY_COUNT; i++)
                {
                    for (int j = 0; j < FORUM_THREAD_COUNT; j++, forumThreadIndex++)
                    {
                        forumThread = forumThreads.get(forumThreadIndex);

                        for (int k = 0; k < FORUM_POST_COUNT; k++)
                        {
                            if (accountIndex >= accountsInCourse.size() - 1)
                            {
                                accountIndex = 0;
                            }

                            account = accountsInCourse.get(accountIndex);

                            forumPost = forumPosts.get(forumPostIndex);

                            forumPost = forumPostService.createNewForumPost(forumPost, account.getAccountId());
                            forumThreadService.addForumPostToForumThread(forumThread, forumPost);

                            forumPostIndex++;
                            accountIndex++;

                            for (int l = 0; l < FORUM_POST_REPLY_COUNT; l++, forumPostIndex++, accountIndex++)
                            {
                                if (accountIndex >= accountsInCourse.size() - 1)
                                {
                                    accountIndex = 0;
                                }

                                account = accountsInCourse.get(accountIndex);

                                forumPostReply = forumPosts.get(forumPostIndex);

                                forumPostReply = forumPostService.createNewForumPostReply(forumPostReply, account.getAccountId(), forumPost.getForumPostId());
                                forumThreadService.addForumPostToForumThread(forumThread, forumPostReply);

                                forumPost = forumPostReply;
                            }
                        }
                    }
                }
            }
        }

        forumPosts = forumPostService.getAllForumPosts();

        System.out.printf(">> Created ForumPosts (%d)\n", forumPosts.size());
    }


    private void completeContent() throws Exception
    {
        int iterativeBreak = COMPLETE_CONTENT_COUNT / 3;

        int completedContent = 0;
        int contentPerStudent = 0;
        int contentPerStudentIndex = 0;

        Account student;

        while (completedContent < COMPLETE_CONTENT_COUNT)
        {
            for (int i = STUDENT_FIRST_INDEX; i < STUDENT_SIZE; i++)
            {
                student = accounts.get(i);
                contentPerStudent = 5;
                contentPerStudentIndex = 0;

                for (EnrolledCourse enrolledCourse : student.getEnrolledCourses())
                {
                    for (EnrolledLesson enrolledLesson : enrolledCourse.getEnrolledLessons())
                    {
                        for (EnrolledContent enrolledContent : enrolledLesson.getEnrolledContents())
                        {
                            try
                            {
                                if (enrolledContent.getDateTimeOfCompletion() != null)
                                {
                                    continue;
                                }
                                else if (completedContent < iterativeBreak)
                                {
                                    enrolledContent = enrolledContentService.setFakeDateTimeOfCompletionOfEnrolledContentByEnrolledContentId(getNextDateTime(CONTENT_COMPLETION_MIN_MONTH, CONTENT_COMPLETION_MAX_MONTH), enrolledContent.getEnrolledContentId());
                                    completedContent++;
                                }
                                else if (completedContent >= iterativeBreak)
                                {
                                    if (completedContent == COMPLETE_CONTENT_COUNT || contentPerStudentIndex == contentPerStudent)
                                    {
                                        break;
                                    }
                                    else
                                    {
                                        enrolledContent = enrolledContentService.setFakeDateTimeOfCompletionOfEnrolledContentByEnrolledContentId(getNextDateTime(CONTENT_COMPLETION_MIN_MONTH, CONTENT_COMPLETION_MAX_MONTH), enrolledContent.getEnrolledContentId());
                                        contentPerStudentIndex++;
                                        completedContent++;
                                    }
                                }
                            }
                            catch (InvalidDateTimeOfCompletionException ex)
                            {
//                                System.out.println(ex.getMessage());
                            }
                        }
                        if (completedContent == COMPLETE_CONTENT_COUNT)
                        {
                            break;
                        }
                    }
                    if (completedContent == COMPLETE_CONTENT_COUNT)
                    {
                        break;
                    }
                }
            }
        }

        System.out.printf(">> Completed EnrolledContent (%d)\n", completedContent);
    }

    private void rateEnrolledCourses() throws Exception
    {
        enrolledCourses = enrolledCourseService.getAllEnrolledCourses();

        int courseRatingSet = 0;
        int rating;

        for (EnrolledCourse enrolledCourse : enrolledCourses)
        {
            rating = RandomGeneratorHelper.getRandomInteger(3, 5);

            if (RATE_ENROLLED_COURSES_COUNT == courseRatingSet)
            {
                break;
            }
            else
            {
                enrolledCourseService.setCourseRatingByEnrolledCourseId(enrolledCourse.getEnrolledCourseId(), rating);
                courseRatingSet++;
            }
        }

        System.out.printf(">> Rated EnrolledCourses (%d)\n", courseRatingSet);
    }

    private void createRandomAccountsAndEnrolledCourses() throws Exception
    {
        Account student;

        int courseIndex = 0;

        for (Account account : randomAccounts)
        {
            if (courseIndex == 3)
            {
                courseIndex = 0;
            }

            student = accountService.createNewAccount(account);
            createEnrolledCourseAndEnrolledLessonsAndEnrolledContents(student, courseIndex++, RANDOM_ACCOUNT_CREATION_MIN_MONTH, RANDOM_ACCOUNT_CREATION_MAX_MONTH);
        }

        System.out.printf(">> Create Random Accounts and EnrolledCourses (%d)\n", randomAccounts.size());
    }

    private void addAccounts() throws Exception
    {
        Account account;

        int nameIndex = 0;
        int displayPictureUrlIndex = 0;
        int biographyIndex = 0;
        int randomInt;

        String name;
        String displayPictureUrl;
        String biography;

        accounts.add(new Account("admin", "Password1", "Admin Adam", "I am Admin", "admin@gmail.com", DISPLAY_PICTURE_URLS.get(displayPictureUrlIndex), true));
        accounts.add(new Account("chloe", "Password1", "Admin Chloe", "I am Chloe, the Team Leader of Kodo", "chloe@gmail.com", DISPLAY_PICTURE_URLS.get(13), true));
        accounts.add(new Account("bryson", "Password1", "Admin Bryson", "I am Bryson, the Technical Leader of Kodo", "bryson@gmail.com", DISPLAY_PICTURE_URLS.get(6), true));
        accounts.add(new Account("chandya", "Password1", "Admin Chandya", "I am Chandya", "chandya@gmail.com", DISPLAY_PICTURE_URLS.get(7), true));
        accounts.add(new Account("ayush", "Password1", "Admin Ayush", "I am Ayush", "ayush@gmail.com", DISPLAY_PICTURE_URLS.get(8), true));
        accounts.add(new Account("elaine", "Password1", "Admin Elaine", "I am Elaine", "elaine@gmail.com", DISPLAY_PICTURE_URLS.get(9), true));
        accounts.add(new Account("theo", "Password1", "Admin Theo", "I am Theodoric", "theodoric@gmail.com", DISPLAY_PICTURE_URLS.get(12), true));
        accounts.add(new Account("tanweekek", "Password1", "Prof Tan Wee Kek", "Dr. TAN Wee Kek is currently an Associate Professor in the Department of Information Systems and Analytics at the School of Computing, National University of Singapore. He is also currently serving as an Assistant Dean (Student Life) in the School of Computing. He graduated with a Doctor of Philosophy in Information Systems in July 2013 and a Bachelor of Computing in Information Systems (1st Class Honours) in July 2007, both from the National University of Singapore.\n", "tanweekek@gmail.com", "https://storage.googleapis.com/download/storage/v1/b/kodo-capstone-bucket/o/0ed1576b-7bb1-4fe0-a240-0923d2c3e514.gif?generation=1634200608311607&alt=media", true));


        // Deactive User specific admins;
        for (int i = 1; i < PREFIXED_ADMIN_COUNT; i++)
        {
            account = accounts.get(i);
            account.setDateTimeOfCreation(getNextDateTime(ACCOUNT_CREATION_MIN_MONTH, ACCOUNT_CREATION_MAX_MONTH));
//            account.setIsActive(false);
        }

        for (int i = 1; i <= STUDENT_COUNT; i++, nameIndex++, displayPictureUrlIndex++, biographyIndex++)
        {
            if (nameIndex >= STUDENT_NAMES.size())
            {
                nameIndex = 0;
            }

            if (displayPictureUrlIndex >= DISPLAY_PICTURE_URLS.size())
            {
                displayPictureUrlIndex = 0;
            }

            if (biographyIndex >= STUDENT_BIOGRAPHIES.size())
            {
                biographyIndex = 0;
            }

            name = STUDENT_NAMES.get(nameIndex);
            displayPictureUrl = DISPLAY_PICTURE_URLS.get(displayPictureUrlIndex);
            biography = String.format(STUDENT_BIOGRAPHIES.get(biographyIndex), name);

            account = new Account("student" + i, "Password1", name, biography, "student" + name.toLowerCase(Locale.ROOT) + i + "@gmail.com", displayPictureUrl, false);
            account.setDateTimeOfCreation(getNextDateTime(ACCOUNT_CREATION_MIN_MONTH, ACCOUNT_CREATION_MAX_MONTH));

            accounts.add(account);
        }

        nameIndex = 0;

        for (int i = 1; i <= TUTOR_COUNT; i++, nameIndex++, displayPictureUrlIndex++, biographyIndex++)
        {
            if (nameIndex >= TUTOR_NAMES.size())
            {
                nameIndex = 0;
            }

            if (displayPictureUrlIndex >= DISPLAY_PICTURE_URLS.size())
            {
                displayPictureUrlIndex = 0;
            }

            if (biographyIndex >= TUTOR_BIOGRAPHIES.size())
            {
                biographyIndex = 0;
            }

            name = TUTOR_NAMES.get(nameIndex);
            displayPictureUrl = DISPLAY_PICTURE_URLS.get(displayPictureUrlIndex);
            biography = String.format(TUTOR_BIOGRAPHIES.get(biographyIndex), name);

            account = new Account("tutor" + i, "Password1", name, biography, "tutor" + name.toLowerCase(Locale.ROOT) + i + "@gmail.com", displayPictureUrl, false);
            account.setDateTimeOfCreation(getNextDateTime(ACCOUNT_CREATION_MIN_MONTH, ACCOUNT_CREATION_MAX_MONTH));

            accounts.add(account);
        }

        nameIndex = 0;
        while (nameIndex < RANDOM_STUDENT_COUNT)
        {
            for (int i = 0; nameIndex < RANDOM_STUDENT_COUNT && i < STUDENT_NAMES.size(); i++, nameIndex++, displayPictureUrlIndex++, biographyIndex++)
            {
                if (displayPictureUrlIndex >= DISPLAY_PICTURE_URLS.size())
                {
                    displayPictureUrlIndex = 0;
                }

                if (biographyIndex >= TUTOR_BIOGRAPHIES.size())
                {
                    biographyIndex = 0;
                }

                name = STUDENT_NAMES.get(i);
                randomInt = RandomGeneratorHelper.getRandomInteger(100000, 999999);
                displayPictureUrl = DISPLAY_PICTURE_URLS.get(displayPictureUrlIndex);
                biography = String.format(STUDENT_BIOGRAPHIES.get(biographyIndex), name);

                account = new Account(i + name + randomInt, "Password1", name, biography, name.toLowerCase(Locale.ROOT) + i + randomInt + "@gmail.com", displayPictureUrl, false);
                account.setDateTimeOfCreation(getNextDateTime(RANDOM_ACCOUNT_CREATION_MIN_MONTH, RANDOM_ACCOUNT_CREATION_MAX_MONTH));

                randomAccounts.add(account);
            }

            for (int i = 0; nameIndex < RANDOM_STUDENT_COUNT && i < TUTOR_NAMES.size(); i++, nameIndex++, displayPictureUrlIndex++, biographyIndex++)
            {
                if (displayPictureUrlIndex >= DISPLAY_PICTURE_URLS.size())
                {
                    displayPictureUrlIndex = 0;
                }

                if (biographyIndex >= TUTOR_BIOGRAPHIES.size())
                {
                    biographyIndex = 0;
                }

                name = TUTOR_NAMES.get(i);
                randomInt = RandomGeneratorHelper.getRandomInteger(100000, 999999);
                displayPictureUrl = DISPLAY_PICTURE_URLS.get(displayPictureUrlIndex);
                biography = String.format(TUTOR_BIOGRAPHIES.get(biographyIndex), name);

                account = new Account(i + name + randomInt, "Password1", name, biography, name.toLowerCase(Locale.ROOT) + i + randomInt + "@gmail.com", displayPictureUrl, false);
                account.setDateTimeOfCreation(getNextDateTime(RANDOM_ACCOUNT_CREATION_MIN_MONTH, RANDOM_ACCOUNT_CREATION_MAX_MONTH));

                randomAccounts.add(account);
            }
        }
    }

    private void addTags()
    {
        for (String language : LANGUAGES)
        {
            tags.add(new Tag(language));
        }
    }

    private void addCourses()
    {
        Course course;

        String level;
        BigDecimal price = BigDecimal.valueOf(19.99);
        List<BigDecimal> prices = new ArrayList<>();

        int bannerUrlIndex = 0;
        int langaugeCounter = 0;

        for (int i = 0; i < LEVELS.size(); i++)
        {
            prices.add(price);
            price = price.add(BigDecimal.TEN);
        }

        for (String language : LANGUAGES)
        {
            for (int i = 0; i < LEVELS.size(); i++)
            {
                level = LEVELS.get(i);
                price = prices.get(i);

                course = new Course(
                        String.format("%s %s Course", level, language),
                        String.format("A %s course in %s language", level.toLowerCase(Locale.ROOT), language.toLowerCase(Locale.ROOT)),
                        price,
                        BANNER_URLS.get(bannerUrlIndex++),
                        true, // Assume that enrollment is already active
                        true // Assume that review is already requested since enrollment is already active
                );

                course.setDateTimeOfCreation(getNextDateTime(COURSE_CREATION_MIN_MONTH, COURSE_CREATION_MAX_MONTH));

                courses.add(course);

                if (bannerUrlIndex == BANNER_URLS.size() - 1)
                {
                    bannerUrlIndex = 0;
                }
            }

            langaugeCounter++;
            if (langaugeCounter == LANGUAGES_COUNT)
            {
                break;
            }
        }

        for (int i = 0; i < LEVELS.size(); i++)
        {
            courses.get(courses.size() - 1 - i).setIsEnrollmentActive(false);
            courses.get(courses.size() - 1 - i).setIsReviewRequested(false);
        }
    }

    private void addLessons()
    {
        for (String language : LANGUAGES)
        {
            for (String level : LEVELS)
            {
                for (int i = 1; i <= LESSON_COUNT; i++)
                {
                    lessons.add(
                            new Lesson(
                                    String.format("%s %s Lesson %d", level, language, i),
                                    "A very interesting " + FormatterHelper.getOrdinal(i) + " lesson on " + language, i)
                    );
                }
            }
        }
    }

    private void addQuizzes()
    {
        for (String language : LANGUAGES)
        {
            for (String level : LEVELS)
            {
                for (int i = 1; i <= LESSON_COUNT; i++)
                {
                    for (int j = 1; j <= QUIZ_COUNT; j++)
                    {
                        quizzes.add(
                                new Quiz(
                                        String.format("%s %s Quiz #%d", level, language, j),
                                        "A very interesting " + FormatterHelper.getOrdinal(j) + " quiz on " + language,
                                        i == 1 ?
                                                LocalTime.of(0, 0, 10)
                                                : i == 2 ?
                                                LocalTime.of(0, 3, 30) :
                                                LocalTime.of(0, 30, 0),
                                        10)
                        );
                    }
                }
            }
        }
    }

    private void addQuizQuestions()
    {
        for (String language : LANGUAGES)
        {
            for (String level : LEVELS)
            {
                for (int i = 1; i <= LESSON_COUNT; i++)
                {
                    for (int j = 1; j <= QUIZ_COUNT; j++)
                    {
                        for (int k = 1; k <= QUIZ_QUESTION_COUNT; )
                        {
                            QuizQuestion mcqQuestion = new QuizQuestion(
                                    String.format("%s %s question of quiz for lesson %d of %s %s course", FormatterHelper.getOrdinal(k++), QuestionType.MCQ, i, level, language),
                                    QuestionType.MCQ,
                                    1);

                            QuizQuestion tfQuestion = new QuizQuestion(
                                    String.format("%s %s question of quiz for lesson %d of %s %s course", FormatterHelper.getOrdinal(k++), QuestionType.TF, i, level, language),
                                    QuestionType.TF,
                                    1);

                            QuizQuestion matchingQuestion = new QuizQuestion(
                                    String.format("%s %s question of quiz for lesson %d of %s %s course", FormatterHelper.getOrdinal(k++), QuestionType.MATCHING, i, level, language),
                                    QuestionType.MATCHING,
                                    1);

                            quizQuestions.add(mcqQuestion);
                            quizQuestions.add(tfQuestion);
                            quizQuestions.add(matchingQuestion);
                        }
                    }
                }
            }
        }
    }

    private void addQuizQuestionOptions()
    {
        for (String language : LANGUAGES)
        {
            for (String level : LEVELS)
            {
                for (int i = 1; i <= LESSON_COUNT; i++)
                {
                    for (int j = 1; j <= QUIZ_COUNT; j++)
                    {
                        for (int k = 1; k <= QUIZ_QUESTION_COUNT; k++)
                        {
                            // MCQ
                            for (int l = 1; l <= QUIZ_QUESTION_OPTION_COUNT; l++)
                            {
                                quizQuestionOptions.add(new QuizQuestionOption("Option " + l, null, l == 1));
                            }
                            k++;

                            // TF
                            quizQuestionOptions.add(new QuizQuestionOption(Boolean.TRUE.toString(), null, true));
                            quizQuestionOptions.add(new QuizQuestionOption(Boolean.FALSE.toString(), null, false));
                            k++;

                            // Matching
                            for (int l = 1; l <= QUIZ_QUESTION_OPTION_COUNT; l++)
                            {
                                quizQuestionOptions.add(new QuizQuestionOption(String.valueOf(l), FormatterHelper.getOrdinal(l), true));
                            }
                            k++;
                        }
                    }
                }
            }
        }
    }

    private void addMultimedias()
    {
        int pdfUrlIndex = 0;
        int mp4UrlIndex = 0;
        int docxUrlIndex = 0;
        int zipUrlIndex = 0;
        int pngUrlIndex = 0;

        for (String language : LANGUAGES)
        {
            for (String level : LEVELS)
            {
                for (int i = 1; i <= LESSON_COUNT; i++)
                {
                    multimedias.add(new Multimedia(level + " " + language + " Multimedia #" + i + "-1", "A very interesting " + FormatterHelper.getOrdinal(i) + " PDF on " + language, PDF_URLS.get(pdfUrlIndex++), MultimediaType.PDF));
                    multimedias.add(new Multimedia(level + " " + language + " Multimedia #" + i + "-2", "A very interesting " + FormatterHelper.getOrdinal(i) + " MP4 on " + language, MP4_URLS.get(mp4UrlIndex++), MultimediaType.VIDEO));
                    multimedias.add(new Multimedia(level + " " + language + " Multimedia #" + i + "-3", "A very interesting " + FormatterHelper.getOrdinal(i) + " DOCX on " + language, DOCX_URLS.get(docxUrlIndex++), MultimediaType.DOCUMENT));
                    multimedias.add(new Multimedia(level + " " + language + " Multimedia #" + i + "-4", "A very interesting " + FormatterHelper.getOrdinal(i) + " ZIP on " + language, ZIP_URLS.get(zipUrlIndex++), MultimediaType.ZIP));
                    multimedias.add(new Multimedia(level + " " + language + " Multimedia #" + i + "-5", "A very interesting " + FormatterHelper.getOrdinal(i) + " PNG on " + language, PNG_URLS.get(pngUrlIndex++), MultimediaType.IMAGE));

                    if (pdfUrlIndex == PDF_URLS.size())
                    {
                        pdfUrlIndex = 0;
                    }

                    if (mp4UrlIndex == MP4_URLS.size())
                    {
                        mp4UrlIndex = 0;
                    }

                    if (docxUrlIndex == DOCX_URLS.size())
                    {
                        docxUrlIndex = 0;
                    }

                    if (zipUrlIndex == ZIP_URLS.size())
                    {
                        zipUrlIndex = 0;
                    }

                    if (pngUrlIndex == PNG_URLS.size())
                    {
                        pngUrlIndex = 0;
                    }
                }
            }
        }
    }

    private void addForumCategories()
    {
        ForumCategory forumCategory;

        for (String language : LANGUAGES)
        {
            for (String level : LEVELS)
            {
                for (int i = 1; i <= FORUM_CATEGORY_COUNT; i++)
                {
                    forumCategory = new ForumCategory("Discussion on " + level + " " + language + " Tips #" + i, "A very informative description on " + language + " tips #" + i);
                    forumCategories.add(forumCategory);
                }
            }
        }
    }

    private void addForumThreads()
    {
        ForumThread forumThread;

        for (String language : LANGUAGES)
        {
            for (String level : LEVELS)
            {
                for (int i = 1; i <= FORUM_CATEGORY_COUNT; i++)
                {
                    for (int j = 1; j <= FORUM_THREAD_COUNT; j++)
                    {
                        forumThread = new ForumThread("Thread #" + j + " on " + level + " " + language + " Tips #" + i, "Thread #" + j + " description on " + level + " " + language + " Tips #" + i);
                        forumThread.setTimeStamp(getNextDateTime(FORUM_THREAD_CREATION_MIN_MONTH, FORUM_THREAD_CREATION_MAX_MONTH));

                        forumThreads.add(forumThread);
                    }
                }
            }
        }
    }

    private void addForumPosts()
    {
        ForumPost forumPost;

        for (String language : LANGUAGES)
        {
            for (String level : LEVELS)
            {
                for (int i = 1; i <= FORUM_CATEGORY_COUNT; i++)
                {
                    for (int j = 1; j <= FORUM_THREAD_COUNT; j++)
                    {
                        for (int k = 1; k <= FORUM_POST_COUNT; k++)
                        {
                            forumPost = new ForumPost("Post #" + k + " on " + level + " " + language);
                            forumPost.setTimeStamp(getNextDateTime(FORUM_POST_CREATION_MIN_MONTH, FORUM_POST_CREATION_MAX_MONTH));

                            forumPosts.add(forumPost);
                            for (int l = 1; l <= FORUM_POST_REPLY_COUNT; l++)
                            {
                                forumPost = new ForumPost("Post #" + k + " Reply #" + l + " on " + level + " " + language);
                                forumPost.setTimeStamp(getNextDateTime(FORUM_POST_CREATION_MIN_MONTH, FORUM_POST_CREATION_MAX_MONTH));

                                forumPosts.add(forumPost);
                            }
                        }
                    }
                }
            }
        }
    }

    private LocalDateTime getNextDateTime(int minMonthsBefore, int maxMonthsBefore)
    {
        int days = RandomGeneratorHelper.getRandomInteger(0, 30);
        int hours = RandomGeneratorHelper.getRandomInteger(0, 24);
        int minutes = RandomGeneratorHelper.getRandomInteger(0, 60);
        int seconds = RandomGeneratorHelper.getRandomInteger(0, 60);

        COURSE_REFERENCE_DATE = COURSE_REFERENCE_DATE.plusDays(days);
        COURSE_REFERENCE_DATE = COURSE_REFERENCE_DATE.plusHours(hours);
        COURSE_REFERENCE_DATE = COURSE_REFERENCE_DATE.plusMinutes(minutes);
        COURSE_REFERENCE_DATE = COURSE_REFERENCE_DATE.plusSeconds(seconds);

        if (COURSE_REFERENCE_DATE.isAfter(LocalDateTime.now().minusMonths(minMonthsBefore)))
        {
            COURSE_REFERENCE_DATE = LocalDateTime.now().minusMonths(maxMonthsBefore);
            return getNextDateTime(minMonthsBefore, maxMonthsBefore);
        }
        else
        {
            return COURSE_REFERENCE_DATE;
        }
    }

    private void printTime()
    {
        System.out.printf("\n===== %f seconds =====\n", ((double) (System.currentTimeMillis() - START_TIME)) / 1000);
    }
}

