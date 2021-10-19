package com.spring.kodo.controller;

import com.spring.kodo.entity.*;
import com.spring.kodo.restentity.request.CreateNewQuizReq;
import com.spring.kodo.restentity.request.UpdateQuizReq;
import com.spring.kodo.restentity.response.QuizWithStudentAttemptCountResp;
import com.spring.kodo.service.inter.*;
import com.spring.kodo.util.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/quiz")
public class QuizController
{
    Logger logger = LoggerFactory.getLogger(QuizController.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private QuizQuestionService quizQuestionService;

    @Autowired
    private QuizQuestionOptionService quizQuestionOptionService;

    @Autowired
    private LessonService lessonService;

    @PostMapping("/createNewBasicQuiz")
    public Quiz createQuiz(
            @RequestPart(name = "name", required = true) String name, @RequestPart(name = "description", required = true) String description,
            @RequestPart(name = "hours", required = true) Integer hours, @RequestPart(name = "minutes", required = true) Integer minutes,
            @RequestPart(name = "maxAttemptsPerStudent", required = true) Integer maxAttemptsPerStudent, @RequestPart(name = "lessonId", required = true) Long lessonId
    )
    {
        try
        {
            Quiz quiz = quizService.createNewQuiz(new Quiz(name, description, LocalTime.of(hours, minutes), maxAttemptsPerStudent));

            Lesson lesson = lessonService.getLessonByLessonId(lessonId);
            lessonService.addContentToLesson(lesson, quiz);

            return quiz;
        }
        catch (UnknownPersistenceException ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        catch (CreateNewQuizException | InputDataValidationException | UpdateContentException ex)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        catch (LessonNotFoundException | ContentNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @PostMapping("/createNewQuiz")
    public Quiz createQuiz(@RequestPart(name = "quiz", required = true) CreateNewQuizReq createNewQuizReq)
    {
        if (createNewQuizReq != null)
        {
            try
            {
                Quiz quiz = createNewQuizReq.getQuiz();
                List<QuizQuestion> quizQuestions = createNewQuizReq.getQuizQuestions();
                List<List<QuizQuestionOption>> quizQuestionOptionLists = createNewQuizReq.getQuizQuestionOptionLists();

                QuizQuestion quizQuestion;
                List<QuizQuestionOption> quizQuestionOptions;

                quiz = quizService.createNewQuiz(quiz);

                for (int i = 0; i < quizQuestions.size(); i++)
                {
                    quizQuestion = quizQuestions.get(i);
                    quizQuestionOptions = quizQuestionOptionLists.get(i);

                    quizQuestion = quizQuestionService.createNewQuizQuestion(quizQuestion, quiz.getContentId());
                    quizQuestionOptions = quizQuestionOptionService.createNewQuizQuestionOptions(quizQuestionOptions);

                    quizQuestion = quizQuestionService.addQuizQuestionOptionsToQuizQuestion(quizQuestion, quizQuestionOptions);
                    quiz = quizService.addQuizQuestionToQuiz(quiz, quizQuestion);
                }

                return quiz;
            }
            catch (QuizNotFoundException | QuizQuestionNotFoundException | QuizQuestionOptionNotFoundException ex)
            {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
            }
            catch (CreateNewQuizException | CreateNewQuizQuestionException | CreateNewQuizQuestionOptionException | UpdateQuizException | UpdateQuizQuestionException | InputDataValidationException ex)
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Create New Quiz Request");
        }
    }

    @GetMapping("/getQuizByQuizId/{quizId}")
    public Quiz getQuizByQuizId(@PathVariable Long quizId)
    {
        try
        {
            return this.quizService.getQuizByQuizId(quizId);
        }
        catch (QuizNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getQuizByQuizId/{quizId}/{accountId}")
    public Quiz getQuizByQuizIdAndAccountId(@PathVariable Long quizId, @PathVariable Long accountId)
    {
        try
        {
            Account account = accountService.getAccountByQuizId(quizId);

            if (account.getAccountId().equals(accountId))
            {
                return this.quizService.getQuizByQuizId(quizId);
            }
            else
            {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have access to this quiz");
            }
        }
        catch (AccountNotFoundException | QuizNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getAllQuizzes")
    public List<Quiz> getAllQuizzes()
    {
        return this.quizService.getAllQuizzes();
    }


    @GetMapping("/getAllQuizzesWithStudentAttemptCountByEnrolledLessonId/{enrolledLessonId}")
    public List<QuizWithStudentAttemptCountResp> getAllQuizzesWithStudentAttemptCountByEnrolledLessonId(@PathVariable Long enrolledLessonId)
    {
        return this.quizService.getAllQuizzesWithStudentAttemptCountByEnrolledLessonId(enrolledLessonId);
    }

    @PostMapping("/updateQuizWithQuizQuestionsAndQuizQuestionOptions")
    public Quiz updateQuizWithQuizQuestionsAndQuizQuestionOptions(@RequestPart(name = "quiz", required = true) UpdateQuizReq updateQuizReq)
    {
        if (updateQuizReq != null)
        {
            try
            {
                // Remove IDs from QuizQuestions and QuizQuestionOptionLists, if any
                updateQuizReq = disassociatingAndRemovingIdsOfUpdateQuizReq(updateQuizReq);

                // Get items from UpdateQuizReq
                Quiz quiz = updateQuizReq.getQuiz();
                List<QuizQuestion> quizQuestions = updateQuizReq.getQuizQuestions();
                List<List<QuizQuestionOption>> quizQuestionOptionLists = updateQuizReq.getQuizQuestionOptionLists();

                // Update
                quiz = quizService.updateQuiz(quiz, quizQuestions, quizQuestionOptionLists);

                return quiz;
            }
            catch (QuizNotFoundException | QuizQuestionNotFoundException | QuizQuestionOptionNotFoundException ex)
            {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
            }
            catch (CreateNewQuizQuestionException | CreateNewQuizQuestionOptionException | UpdateQuizException | UpdateQuizQuestionException ex)
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
            }
            catch (DeleteQuizQuestionException | DeleteQuizQuestionOptionException ex)
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
            }
            catch (InputDataValidationException | UnknownPersistenceException ex)
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
            }
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Update Quiz Request");
        }
    }

    @DeleteMapping("/deleteQuizWithQuizQuestionsAndQuizQuestionOptionsByQuizId")
    public Boolean deleteQuizWithQuizQuestionsAndQuizQuestionOptionsByQuizId(@RequestPart List<Long> quizIds)
    {
        try
        {
            return quizService.deleteQuizzesWithQuizQuestionsAndQuizQuestionOptionsByQuizId(quizIds);
        }
        catch (QuizNotFoundException | QuizQuestionOptionNotFoundException | QuizQuestionNotFoundException | LessonNotFoundException | ContentNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
        catch (DeleteQuizQuestionOptionException | DeleteQuizQuestionException | DeleteQuizException | UpdateContentException | InputDataValidationException | UnknownPersistenceException ex)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    // Very unique method
    private UpdateQuizReq disassociatingAndRemovingIdsOfUpdateQuizReq(UpdateQuizReq updateQuizReq)
    {
        List<QuizQuestion> quizQuestions = updateQuizReq
                .getQuizQuestions()
                .stream()
                .map(quizQuestion ->
                {
                    quizQuestion.setQuizQuestionId(null);
                    quizQuestion.getQuizQuestionOptions().clear();
                    quizQuestion.setQuiz(null);
                    return quizQuestion;
                })
                .collect(Collectors.toList());

        List<List<QuizQuestionOption>> quizQuestionOptionLists = updateQuizReq
                .getQuizQuestionOptionLists()
                .stream()
                .map(quizQuestionOptionList ->
                        quizQuestionOptionList
                                .stream()
                                .map(quizQuestionOption ->
                                {
                                    quizQuestionOption.setQuizQuestionOptionId(null);
                                    return quizQuestionOption;
                                })
                                .collect(Collectors.toList())
                )
                .collect(Collectors.toList());

        return updateQuizReq;
    }
}
