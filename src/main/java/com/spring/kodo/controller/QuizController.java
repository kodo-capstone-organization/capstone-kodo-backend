package com.spring.kodo.controller;

import com.spring.kodo.entity.Quiz;
import com.spring.kodo.entity.QuizQuestion;
import com.spring.kodo.entity.QuizQuestionOption;
import com.spring.kodo.restentity.request.CreateNewQuizReq;
import com.spring.kodo.restentity.response.QuizWithStudentAttemptCountResp;
import com.spring.kodo.service.inter.QuizQuestionOptionService;
import com.spring.kodo.service.inter.QuizQuestionService;
import com.spring.kodo.service.inter.QuizService;
import com.spring.kodo.util.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(path = "/quiz")
public class QuizController
{
    Logger logger = LoggerFactory.getLogger(QuizController.class);

    @Autowired
    private QuizService quizService;

    @Autowired
    private QuizQuestionService quizQuestionService;

    @Autowired
    private QuizQuestionOptionService quizQuestionOptionService;

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
            catch (CreateNewQuizException | CreateNewQuizQuestionException | UpdateQuizException | UpdateQuizQuestionException | InputDataValidationException ex)
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

    @GetMapping("/deleteQuizWithQuizQuestionsAndQuizQuestionOptionsByQuizId/{quizId}")
    public Boolean deleteQuizWithQuizQuestionsAndQuizQuestionOptionsByQuizId(@PathVariable Long quizId)
    {
        if (quizId != null)
        {
            try
            {
//                Quiz quiz = quizService.getQuizByQuizId(quizId);
//                for (QuizQuestion quizQuestion : quiz.getQuizQuestions())
//                {
//                    for (QuizQuestionOption quizQuestionOption : quizQuestion.getQuizQuestionOptions())
//                    {
//                        quizQuestionOptionService.deleteQuizQuestionOption(quizQuestionOption.getQuizQuestionOptionId());
//                    }
//                    quizQuestionService.deleteQuizQuestion(quizQuestion.getQuizQuestionId());
//                }

                return quizService.deleteQuizWithQuizQuestionsAndQuizQuestionOptionsByQuizId(quizId);
            }
            catch (QuizNotFoundException | QuizQuestionOptionNotFoundException | QuizQuestionNotFoundException ex)
            {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
            }
            catch (DeleteQuizQuestionOptionException | DeleteQuizQuestionException | DeleteQuizException ex)
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
            }
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Quiz ID");
        }
    }
}
