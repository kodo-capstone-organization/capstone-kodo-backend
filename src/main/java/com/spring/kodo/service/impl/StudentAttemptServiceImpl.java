package com.spring.kodo.service.impl;

import com.spring.kodo.entity.*;
import com.spring.kodo.repository.StudentAttemptAnswerRepository;
import com.spring.kodo.repository.StudentAttemptQuestionRepository;
import com.spring.kodo.repository.StudentAttemptRepository;
import com.spring.kodo.service.*;
import com.spring.kodo.util.MessageFormatterUtil;
import com.spring.kodo.util.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

@Service
public class StudentAttemptServiceImpl implements StudentAttemptService
{
    @Autowired
    private StudentAttemptAnswerRepository studentAttemptAnswerRepository;

    @Autowired
    private StudentAttemptQuestionRepository studentAttemptQuestionRepository;

    @Autowired
    private StudentAttemptRepository studentAttemptRepository;

    @Autowired
    private StudentAttemptQuestionService studentAttemptQuestionService;

    @Autowired
    private QuizQuestionOptionService quizQuestionOptionService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private AccountService accountService;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public StudentAttemptServiceImpl()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public StudentAttempt createNewStudentAttempt(Long quizId, Long studentId) throws AccountNotFoundException, QuizNotFoundException, InputDataValidationException
    {
        StudentAttempt newStudentAttempt = new StudentAttempt();

        Set<ConstraintViolation<StudentAttempt>> constraintViolations = validator.validate(newStudentAttempt);
        if (constraintViolations.isEmpty())
        {
            if (studentId != null)
            {
                Account account = accountService.getAccountByAccountId(studentId);
                account.getStudentAttempts().add(newStudentAttempt);
            }

            if (quizId != null)
            {
                Quiz quiz = quizService.getQuizByQuizId(quizId);
                newStudentAttempt.setQuiz(quiz);
                quiz.getStudentAttempts().add(newStudentAttempt);

                // Link QuizQuestions to StudentAttemptQuestions
                for (QuizQuestion quizQuestion : quiz.getQuizQuestions())
                {
                    StudentAttemptQuestion studentAttemptQuestion = new StudentAttemptQuestion();
                    studentAttemptQuestion.setQuizQuestion(quizQuestion);

                    newStudentAttempt.getStudentAttemptQuestions().add(studentAttemptQuestion);

                    studentAttemptQuestionRepository.save(studentAttemptQuestion);
                }
            }

            // Persist StudentAttempt
            studentAttemptRepository.saveAndFlush(newStudentAttempt);
            return newStudentAttempt;
        }
        else
        {
            throw new InputDataValidationException(MessageFormatterUtil.prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public StudentAttempt getStudentAttemptByStudentAttemptId(Long studentAttemptId) throws StudentAttemptNotFoundException
    {
        StudentAttempt studentAttempt = studentAttemptRepository.findById(studentAttemptId).orElse(null);

        if (studentAttempt != null)
        {
            return studentAttempt;
        }
        else
        {
            throw new StudentAttemptNotFoundException("StudentAttempt with ID: " + studentAttemptId + " does not exist!");
        }
    }

    @Override
    public List<StudentAttempt> getAllStudentAttempts()
    {
        return studentAttemptRepository.findAll();
    }

    @Override
    public void addStudentAttemptAnswerToStudentAttemptQuestion(Long studentAttemptQuestionId, List<Long> quizQuestionOptionIds) throws UpdateStudentAttemptAnswerException, StudentAttemptQuestionNotFoundException, QuizQuestionOptionNotFoundException
    {
        if (studentAttemptQuestionId != null)
        {
            StudentAttemptQuestion studentAttemptQuestion = studentAttemptQuestionService.getStudentAttemptQuestionByStudentAttemptQuestionId(studentAttemptQuestionId);

            for (Long quizQuestionOptionId : quizQuestionOptionIds)
            {
                StudentAttemptAnswer studentAttemptAnswer = new StudentAttemptAnswer();
                QuizQuestionOption quizQuestionOption = quizQuestionOptionService.getQuizQuestionOptionByQuizQuestionOptionId(quizQuestionOptionId);

                studentAttemptAnswer.setQuizQuestionOption(quizQuestionOption);

                studentAttemptQuestion.getStudentAttemptAnswers().add(studentAttemptAnswer);

                studentAttemptAnswerRepository.saveAndFlush(studentAttemptAnswer);
            }
        }
        else
        {
            throw new UpdateStudentAttemptAnswerException("StudentAttempt ID not provided for student attempt to be updated");
        }
    }
}
