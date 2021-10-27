package com.spring.kodo.controller;

import com.spring.kodo.entity.*;
import com.spring.kodo.entity.rest.request.CreateNewStudentAttemptReq;
import com.spring.kodo.service.inter.*;
import com.spring.kodo.util.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/studentAttempt")
public class StudentAttemptController
{
    Logger logger = LoggerFactory.getLogger(LessonController.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private StudentAttemptService studentAttemptService;

    @Autowired
    private StudentAttemptQuestionService studentAttemptQuestionService;

    @Autowired
    private StudentAttemptAnswerService studentAttemptAnswerService;

    @Autowired
    private EnrolledContentService enrolledContentService;

    @PostMapping("/createNewStudentAttempt")
    public StudentAttempt createNewStudentAttempt(
            @RequestPart(name = "createNewStudentAttemptReq", required = true) CreateNewStudentAttemptReq createNewStudentAttemptReq
    )
    {
        if (createNewStudentAttemptReq != null)
        {
            try
            {
                // Get items from CreateNewStudentAttemptReq
                Long enrolledContentId = createNewStudentAttemptReq.getEnrolledContentId();
                List<List<Integer[]>> tempLists = createNewStudentAttemptReq.getQuizQuestionOptionIdLists();

                List<List<Long[]>> quizQuestionOptionIdLists = new ArrayList<>();

                for (List<Integer[]> a : tempLists)
                {
                    List<Long[]> tmpList = new ArrayList<>();
                    for (Integer[] b : a)
                    {
                        Long[] longArr = new Long[b.length];
                        for (int i = 0; i < longArr.length; i++)
                        {
                            longArr[i] = b[i].longValue();
                        }
                        tmpList.add(longArr);
                    }
                    quizQuestionOptionIdLists.add(tmpList);
                }

                // Create new StudentAttempt and StudentAttemptQuestions
                StudentAttempt studentAttempt = studentAttemptService.createNewStudentAttempt(enrolledContentId);
                List<StudentAttemptQuestion> studentAttemptQuestions = studentAttempt.getStudentAttemptQuestions();

                // Create new StudentAttemptAnswers
                List<Long[]> quizQuestionOptionIds;
                StudentAttemptQuestion studentAttemptQuestion;
                StudentAttemptAnswer studentAttemptAnswer;
                Long leftQuizQuestionOptionId;
                Long rightQuizQuestionOptionId;

                for (int i = 0; i < studentAttemptQuestions.size(); i++)
                {
                    quizQuestionOptionIds = quizQuestionOptionIdLists.get(i);
                    studentAttemptQuestion = studentAttemptQuestions.get(i);

                    for (Long[] quizQuestionOptionPair : quizQuestionOptionIds)
                    {
                        if (quizQuestionOptionPair.length == 1)
                        {
                            leftQuizQuestionOptionId = quizQuestionOptionPair[0];

                            studentAttemptAnswer = studentAttemptAnswerService.createNewStudentAttemptAnswer(leftQuizQuestionOptionId);
                            studentAttemptQuestionService.addStudentAttemptAnswerToStudentAttemptQuestion(studentAttemptQuestion, studentAttemptAnswer);
                        }
                        else if (quizQuestionOptionPair.length == 2)
                        {
                            leftQuizQuestionOptionId = quizQuestionOptionPair[0];
                            rightQuizQuestionOptionId = quizQuestionOptionPair[1];

                            studentAttemptAnswer = studentAttemptAnswerService.createNewStudentAttemptAnswer(leftQuizQuestionOptionId, rightQuizQuestionOptionId);
                            studentAttemptQuestionService.addStudentAttemptAnswerToStudentAttemptQuestion(studentAttemptQuestion, studentAttemptAnswer);
                        }
                    }
                }

                // Adding StudentAttempt to EnrolledContent
                EnrolledContent enrolledContent = enrolledContentService.getEnrolledContentByEnrolledContentId(enrolledContentId);
                enrolledContentService.addStudentAttemptToEnrolledContent(enrolledContent, studentAttempt);

                // Check if quiz is completed
                enrolledContentService.setDateTimeOfCompletionOfEnrolledContentByEnrolledContentId(true, enrolledContentId);

                studentAttempt = studentAttemptService.markStudentAttemptByStudentAttemptId(studentAttempt.getStudentAttemptId());

                return studentAttempt;
            }
            catch (InputDataValidationException ex)
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
            }
            catch (EnrolledCourseNotFoundException | EnrolledLessonNotFoundException | EnrolledContentNotFoundException | QuizQuestionNotFoundException | QuizQuestionOptionNotFoundException | StudentAttemptQuestionNotFoundException | StudentAttemptAnswerNotFoundException | StudentAttemptNotFoundException ex)
            {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
            }
            catch (CreateNewStudentAttemptException | CreateNewStudentAttemptQuestionException | CreateNewStudentAttemptAnswerException | UpdateStudentAttemptQuestionException | UpdateStudentAttemptAnswerException | UpdateEnrolledContentException ex)
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
            }
            catch (UnknownPersistenceException ex)
            {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
            }
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Create New StudentAttempt Request");
        }
    }

    @GetMapping("/getStudentAttemptByStudentAttemptId/{studentAttemptId}")
    public StudentAttempt getStudentAttemptByStudentAttemptId(@PathVariable Long studentAttemptId)
    {
        try
        {
            return this.studentAttemptService.getStudentAttemptByStudentAttemptId(studentAttemptId);
        }
        catch (StudentAttemptNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getStudentAttemptByStudentAttemptIdAndAccountId/{studentAttemptId}/{accountId}")
    public StudentAttempt getStudentAttemptByStudentAttemptIdAndAccountId(@PathVariable Long studentAttemptId, @PathVariable Long accountId)
    {
        try
        {
            Account account = this.accountService.getAccountByStudentAttemptId(studentAttemptId);

            if (account.getAccountId().equals(accountId))
            {
                return this.studentAttemptService.getStudentAttemptByStudentAttemptId(studentAttemptId);
            }
            else
            {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have access to this student attempt");
            }
        }
        catch (AccountNotFoundException | StudentAttemptNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getAllStudentAttempts")
    public List<StudentAttempt> getAllStudentAttempts()
    {
        return this.studentAttemptService.getAllStudentAttempts();
    }
}