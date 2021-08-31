package com.spring.kodo.service.impl;

import com.spring.kodo.entity.QuizQuestion;
import com.spring.kodo.entity.QuizQuestionOption;
import com.spring.kodo.entity.StudentAttemptAnswer;
import com.spring.kodo.entity.StudentAttemptQuestion;
import com.spring.kodo.repository.StudentAttemptQuestionRepository;
import com.spring.kodo.service.QuizQuestionService;
import com.spring.kodo.service.StudentAttemptAnswerService;
import com.spring.kodo.service.StudentAttemptQuestionService;
import com.spring.kodo.util.MessageFormatterUtil;
import com.spring.kodo.util.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

@Service
public class StudentAttemptQuestionServiceImpl implements StudentAttemptQuestionService
{
    @Autowired
    private StudentAttemptQuestionRepository studentAttemptQuestionRepository;

    @Autowired
    private StudentAttemptAnswerService studentAttemptAnswerService;

    @Autowired
    private QuizQuestionService quizQuestionService;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public StudentAttemptQuestionServiceImpl()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public StudentAttemptQuestion createNewStudentAttemptQuestion(Long quizQuestionId) throws InputDataValidationException, CreateStudentAttemptQuestionException, QuizQuestionNotFoundException, UnknownPersistenceException
    {
        try
        {
            StudentAttemptQuestion newStudentAttemptQuestion = new StudentAttemptQuestion();

            Set<ConstraintViolation<StudentAttemptQuestion>> constraintViolations = validator.validate(newStudentAttemptQuestion);
            if (constraintViolations.isEmpty())
            {
                if (quizQuestionId != null)
                {
                    QuizQuestion quizQuestion = quizQuestionService.getQuizQuestionByQuizQuestionId(quizQuestionId);
                    newStudentAttemptQuestion.setQuizQuestion(quizQuestion);

                    // Persist StudentAttemptQuestion
                    studentAttemptQuestionRepository.saveAndFlush(newStudentAttemptQuestion);
                    return newStudentAttemptQuestion;
                }
                else
                {
                    throw new CreateStudentAttemptQuestionException("QuizQuestionId cannot be null");
                }
            }
            else
            {
                throw new InputDataValidationException(MessageFormatterUtil.prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        catch (DataAccessException ex)
        {
            throw new UnknownPersistenceException(ex.getMessage());
        }
    }

    @Override
    public StudentAttemptQuestion getStudentAttemptQuestionByStudentAttemptQuestionId(Long studentAttemptQuestionId) throws StudentAttemptQuestionNotFoundException
    {
        StudentAttemptQuestion studentAttemptQuestion = studentAttemptQuestionRepository.findById(studentAttemptQuestionId).orElse(null);

        if (studentAttemptQuestion != null)
        {
            return studentAttemptQuestion;
        }
        else
        {
            throw new StudentAttemptQuestionNotFoundException("StudentAttemptQuestion with ID: " + studentAttemptQuestionId + " does not exist!");
        }
    }

    @Override
    public List<StudentAttemptQuestion> getAllStudentAttemptQuestions()
    {
        return studentAttemptQuestionRepository.findAll();
    }

    @Override
    public StudentAttemptQuestion addStudentAttemptAnswerToStudentAttemptQuestion(StudentAttemptQuestion studentAttemptQuestion, StudentAttemptAnswer studentAttemptAnswer) throws UpdateStudentAttemptQuestionException, StudentAttemptQuestionNotFoundException, StudentAttemptAnswerNotFoundException
    {
        if (studentAttemptQuestion != null)
        {
            if (studentAttemptQuestion.getStudentAttemptQuestionId() != null)
            {
                studentAttemptQuestion = getStudentAttemptQuestionByStudentAttemptQuestionId(studentAttemptQuestion.getStudentAttemptQuestionId());

                if (studentAttemptAnswer != null)
                {
                    if (studentAttemptAnswer.getStudentAttemptAnswerId() != null)
                    {
                        studentAttemptAnswer = studentAttemptAnswerService.getStudentAttemptAnswerByStudentAttemptAnswerId(studentAttemptAnswer.getStudentAttemptAnswerId());

                        studentAttemptQuestion.getStudentAttemptAnswers().add(studentAttemptAnswer);

                        studentAttemptQuestionRepository.saveAndFlush(studentAttemptQuestion);
                        return studentAttemptQuestion;
                    }
                    else
                    {
                        throw new UpdateStudentAttemptQuestionException("StudentAttemptAnswer ID cannot be null");
                    }
                }
                else
                {
                    throw new UpdateStudentAttemptQuestionException("StudentAttemptAnswer cannot be null");
                }
            }
            else
            {
                throw new UpdateStudentAttemptQuestionException("StudentAttemptQuestion ID cannot be null");
            }
        }
        else
        {
            throw new UpdateStudentAttemptQuestionException("StudentAttemptQuestion cannot be null");
        }
    }
}
