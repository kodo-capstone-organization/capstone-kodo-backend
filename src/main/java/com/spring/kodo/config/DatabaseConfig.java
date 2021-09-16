package com.spring.kodo.config;

import com.spring.kodo.entity.*;
import com.spring.kodo.service.inter.*;
import com.spring.kodo.util.enumeration.MultimediaType;
import com.spring.kodo.util.enumeration.QuestionType;
import com.spring.kodo.util.exception.TagNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Collectors;

import static com.spring.kodo.util.Constants.*;

@Configuration
public class DatabaseConfig
{
    @Value("${CONFIG_PROFILE_TYPE}")
    private String configProfileType;

    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    private RestartService restartService;

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
    private Environment env;

    private List<Account> accounts;
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

    private final Integer LANGUAGES_COUNT = 10; // Current max is 25

    private final Integer TUTOR_COUNT = 25;
    private final Integer STUDENT_COUNT = 25;

    private final Integer LESSON_COUNT = 3;

    private final Integer MULTIMEDIA_COUNT = 2;

    private final Integer QUIZ_COUNT = 1;
    private final Integer QUIZ_QUESTION_COUNT = 3;
    private final Integer QUIZ_QUESTION_OPTION_COUNT = 3;

    private final Integer STUDENT_ENROLLED_COUNT = (int) (0.5 * (LANGUAGES_COUNT * STUDENT_COUNT));
    private final Integer STUDENT_ATTEMPT_COUNT = 5;

    private final Integer FORUM_CATEGORY_COUNT = 1;
    private final Integer FORUM_THREAD_COUNT = 3;
    private final Integer FORUM_POST_COUNT = 3;

    private final Integer COMPLETE_CONTENT_COUNT = (int) (0.5 * (STUDENT_ENROLLED_COUNT * LESSON_COUNT * (MULTIMEDIA_COUNT + QUIZ_COUNT)));

    private final Integer RATE_ENROLLED_COURSES_COUNT = (int) (0.2 * STUDENT_ENROLLED_COUNT);

    // Don't Edit these
    private final Integer PREFIXED_ADMIN_COUNT = 1;
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
        try
        {
            // Stop Heroku from updating Google Cloud SQL on every git change
            if (configProfileType.equals("prod"))
            {
                System.out.println("=== Production Configuration ===");
                return;
            }

            this.tagService.getTagByTagId(1L);

            Scanner sc = new Scanner(System.in);
            System.out.println("1. Reload Data");
            System.out.println("2. Do Nothing");
            System.out.print("> ");

            if (sc.nextInt() == 1)
            {
                dataSourceService.dropDatabase();
                dataSourceService.createDatabase();
                restartService.restartApp();
            }

            sc.close();
        }
        catch (TagNotFoundException ex)
        {
            loadData();
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
        createStudentAttemptsAndStudentAttemptQuestions();
        createStudentAttemptAnswers();
        createForumCategories();
        createForumThreads();
        createForumPosts();
        completeContent();
        rateEnrolledCourses();

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
            accountService.createNewAccount(accounts.get(i), null);
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

                courseService.createNewCourse(course, tutor.getAccountId(), tagTitles);
                accountService.addCourseToAccount(tutor, course);

                tagTitles.clear();
            }

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
        Account student;
        Course course;
        EnrolledCourse enrolledCourse;
        EnrolledLesson enrolledLesson;
        EnrolledContent enrolledContent;

        int i = 0;
        while (i < STUDENT_ENROLLED_COUNT)
        {
            for (int courseIndex = 0; courseIndex < courses.size(); courseIndex++)
            {
                for (int studentIndex = STUDENT_FIRST_INDEX; studentIndex < STUDENT_SIZE && i < STUDENT_ENROLLED_COUNT; studentIndex++, i++)
                {
                    try
                    {
                        student = accounts.get(studentIndex);
                        course = courses.get(courseIndex);

                        enrolledCourse = enrolledCourseService.createNewEnrolledCourse(student.getAccountId(), course.getCourseId());
                        accountService.addEnrolledCourseToAccount(student, enrolledCourse);

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
                    catch (Exception ex)
                    {
                        i--;
                    }
                }
            }
        }

        enrolledCourses = enrolledCourseService.getAllEnrolledCourses();
        enrolledLessons = enrolledLessonService.getAllEnrolledLessons();
        enrolledContents = enrolledContentService.getAllEnrolledContents();

        System.out.printf(">> Created EnrolledCourses (%d)\n", enrolledCourses.size());
        System.out.printf(">> Created EnrolledLessons (%d)\n", enrolledLessons.size());
        System.out.printf(">> Created EnrolledContents (%d)\n", enrolledContents.size());
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
                contentPerStudent = getRandomNumber(1, 10);
                contentPerStudentIndex = 0;

                for (EnrolledCourse enrolledCourse : student.getEnrolledCourses())
                {
                    for (EnrolledLesson enrolledLesson : enrolledCourse.getEnrolledLessons())
                    {
                        for (EnrolledContent enrolledContent : enrolledLesson.getEnrolledContents())
                        {
                            if (enrolledContent.getDateTimeOfCompletion() != null)
                            {
                                continue;
                            }
                            else if (completedContent < iterativeBreak)
                            {
                                enrolledContent = enrolledContentService.setDateTimeOfCompletionOfEnrolledContentByAccountIdAndContentId(true, student.getAccountId(), enrolledContent.getParentContent().getContentId());
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
                                    enrolledContent = enrolledContentService.setDateTimeOfCompletionOfEnrolledContentByAccountIdAndContentId(true, student.getAccountId(), enrolledContent.getParentContent().getContentId());
                                    contentPerStudentIndex++;
                                    completedContent++;
                                }
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
        int rating = 1;

        for (EnrolledCourse enrolledCourse : enrolledCourses)
        {
            if (RATE_ENROLLED_COURSES_COUNT == courseRatingSet)
            {
                break;
            }
            else if (enrolledCourse.getDateTimeOfCompletion() != null)
            {
                enrolledCourseService.setCourseRatingByEnrolledCourseId(enrolledCourse.getEnrolledCourseId(), rating++);
                courseRatingSet++;
            }

            if (rating == 6)
            {
                rating = 1;
            }
        }

        System.out.printf(">> Rated EnrolledCourses (%d)\n", courseRatingSet);

        // Testing getAllCoursesToRecommendByAccountId
//        for (int i = 1; i <= 5; i++)
//        {
//            List<Course> courses = courseService.getAllCoursesToRecommendByAccountId((long) i);
//            System.out.println(courses.size());
//            for (Course course : courses)
//            {
//                System.out.println(course.getName());
//            }
//            System.out.println();
//        }
    }

    private void addAccounts() throws Exception
    {
        int nameIndex = 0;
        int displayPictureUrlIndex = 0;
        int biographyIndex = 0;

        String name;
        String displayPictureUrl;
        String biography;

        accounts.add(new Account("admin", "Password1", "Admin Adam", "I am Admin", "admin@gmail.com", DISPLAY_PICTURE_URLS.get(displayPictureUrlIndex), true));

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

            accounts.add(new Account("student" + i, "Password1", "Student " + name, biography, "student" + name.toLowerCase(Locale.ROOT) + i + "@gmail.com", displayPictureUrl, false));
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

            accounts.add(new Account("tutor" + i, "Password1", "Tutor " + name, biography, "tutor" + name.toLowerCase(Locale.ROOT) + i + "@gmail.com", displayPictureUrl, false));
        }

        // UNIQUE for Trisha
        accounts.get(TUTOR_FIRST_INDEX).setStripeAccountId("acct_1JZ8h5PDe56gk95T");
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
        String level;
        BigDecimal price = BigDecimal.valueOf(19.99);

        List<BigDecimal> prices = new ArrayList<>();
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

                courses.add(
                        new Course(
                                String.format("%s %s Course", level, language),
                                String.format("A %s course in %s language", level.toLowerCase(Locale.ROOT), language.toLowerCase(Locale.ROOT)),
                                price,
                                String.format("https://%s%scoursebanner.com", level.toLowerCase(Locale.ROOT), language.toLowerCase(Locale.ROOT))
                        )
                );
            }
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
                                    "A very interesting " + ordinal(i) + " lesson on " + language, i)
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
                                        "A very interesting " + ordinal(j) + " quiz on " + language,
                                        LocalTime.of(0, 30),
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
                        for (int k = 1; k <= QUIZ_QUESTION_COUNT; k++)
                        {
                            quizQuestions.add(
                                    new QuizQuestion(
                                            String.format("%s question of quiz for lesson %d of %s %s course", ordinal(k), i, level, language),
                                            QuestionType.MCQ,
                                            1)
                            );
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
                            for (int l = 1; l <= QUIZ_QUESTION_OPTION_COUNT; l++)
                            {
                                quizQuestionOptions.add(new QuizQuestionOption("Option " + l, null, l == 1));
                            }
                        }
                    }
                }
            }
        }
    }

    private void addMultimedias()
    {
        for (String language : LANGUAGES)
        {
            for (String level : LEVELS)
            {
                for (int i = 1; i <= LESSON_COUNT; i++)
                {
                    multimedias.add(new Multimedia(level + " " + language + " Multimedia #" + i + "-1", "A very interesting " + ordinal(i) + " PDF on " + language, "http://www.africau.edu/images/default/sample.pdf", MultimediaType.PDF));
                    multimedias.add(new Multimedia(level + " " + language + " Multimedia #" + i + "-2", "A very interesting " + ordinal(i) + " video on " + language, "https://www.youtube.com/watch?v=T8y_RsF4TSw&list=RDmvkbCZfwWzA&index=16", MultimediaType.VIDEO));
                }
            }
        }
    }

    private void addForumCategories()
    {
        for (String language : LANGUAGES)
        {
            for (String level : LEVELS)
            {
                for (int i = 1; i <= FORUM_CATEGORY_COUNT; i++)
                {
                    forumCategories.add(new ForumCategory("Discussion on " + level + " " + language + " Tips #" + i, "A very informative description on " + language + " tips #" + i));
                }
            }
        }
    }

    private void addForumThreads()
    {
        for (String language : LANGUAGES)
        {
            for (String level : LEVELS)
            {
                for (int i = 1; i <= FORUM_CATEGORY_COUNT; i++)
                {
                    for (int j = 1; j <= FORUM_THREAD_COUNT; j++)
                    {
                        forumThreads.add(new ForumThread("Thread #" + j + " on " + level + " " + language + " Tips #" + i, "Thread #" + j + " description on " + level + " " + language + " Tips #" + i));
                    }
                }
            }
        }
    }

    private void addForumPosts()
    {
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
                            forumPosts.add(new ForumPost("Post #" + k + " on " + level + " " + language));
                        }
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

