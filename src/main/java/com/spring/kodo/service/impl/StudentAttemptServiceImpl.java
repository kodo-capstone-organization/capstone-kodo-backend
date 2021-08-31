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
    private StudentAttemptRepository studentAttemptRepository;

    @Autowired
    private StudentAttemptQuestionService studentAttemptQuestionService;

    @Autowired
    private QuizService quizService;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public StudentAttemptServiceImpl()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public StudentAttempt createNewStudentAttempt(Long quizId) throws CreateStudentAttemptException, QuizNotFoundException, InputDataValidationException, CreateStudentAttemptQuestionException, QuizQuestionNotFoundException
    {
        StudentAttempt newStudentAttempt = new StudentAttempt();

        Set<ConstraintViolation<StudentAttempt>> constraintViolations = validator.validate(newStudentAttempt);
        if (constraintViolations.isEmpty())
        {
            if (quizId != null)
            {
                Quiz quiz = quizService.getQuizByQuizId(quizId);
                newStudentAttempt.setQuiz(quiz);
                quiz.getStudentAttempts().add(newStudentAttempt);

                // Link QuizQuestions to StudentAttemptQuestions
                for (QuizQuestion quizQuestion : quiz.getQuizQuestions())
                {
                    StudentAttemptQuestion studentAttemptQuestion = studentAttemptQuestionService.createNewStudentAttemptQuestion(quizQuestion.getQuizQuestionId());
                    newStudentAttempt.getStudentAttemptQuestions().add(studentAttemptQuestion);
                }

                // Persist StudentAttempt
                studentAttemptRepository.saveAndFlush(newStudentAttempt);
                return newStudentAttempt;
            }
            else
            {
                throw new CreateStudentAttemptException("Quiz ID cannot be null");
            }
        }
        else
        {
            throw new InputDataValidationException(MessageFormatterUtil.prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public StudentAttempt getStudentAttemptByStudentAttemptId(Long studentAttemptId) throws
            StudentAttemptNotFoundException
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
}
