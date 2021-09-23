package com.spring.kodo.service.impl;

import com.spring.kodo.entity.QuizQuestionOption;
import com.spring.kodo.entity.StudentAttempt;
import com.spring.kodo.entity.StudentAttemptAnswer;
import com.spring.kodo.repository.StudentAttemptAnswerRepository;
import com.spring.kodo.service.inter.QuizQuestionOptionService;
import com.spring.kodo.service.inter.StudentAttemptAnswerService;
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
public class StudentAttemptAnswerServiceImpl implements StudentAttemptAnswerService
{
    @Autowired
    private StudentAttemptAnswerRepository studentAttemptAnswerRepository;

    @Autowired
    private QuizQuestionOptionService quizQuestionOptionService;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public StudentAttemptAnswerServiceImpl()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public StudentAttemptAnswer createNewStudentAttemptAnswer(Long leftQuizQuestionOptionId) throws InputDataValidationException, CreateNewStudentAttemptAnswerException, QuizQuestionOptionNotFoundException, UnknownPersistenceException
    {
        try
        {
            if (leftQuizQuestionOptionId != null)
            {
                QuizQuestionOption leftQuizQuestionOption = quizQuestionOptionService.getQuizQuestionOptionByQuizQuestionOptionId(leftQuizQuestionOptionId);
                StudentAttemptAnswer newStudentAttemptAnswer;

                newStudentAttemptAnswer = new StudentAttemptAnswer(leftQuizQuestionOption.getLeftContent());

                Set<ConstraintViolation<StudentAttemptAnswer>> constraintViolations = validator.validate(newStudentAttemptAnswer);
                if (constraintViolations.isEmpty())
                {
                    // Persist StudentAttemptQuestion
                    studentAttemptAnswerRepository.saveAndFlush(newStudentAttemptAnswer);
                    return newStudentAttemptAnswer;
                }
                else
                {
                    throw new InputDataValidationException(MessageFormatterUtil.prepareInputDataValidationErrorsMessage(constraintViolations));
                }
            }
            else
            {
                throw new CreateNewStudentAttemptAnswerException("QuizQuestionOptionId cannot be null");
            }
        }
        catch (DataAccessException ex)
        {
            throw new UnknownPersistenceException(ex.getMessage());
        }
    }

    @Override
    public StudentAttemptAnswer createNewStudentAttemptAnswer(Long leftQuizQuestionOptionId, Long rightQuizQuestionOptionId) throws InputDataValidationException, CreateNewStudentAttemptAnswerException, QuizQuestionOptionNotFoundException, UnknownPersistenceException
    {
        try
        {
            if (leftQuizQuestionOptionId != null)
            {
                QuizQuestionOption leftQuizQuestionOption = quizQuestionOptionService.getQuizQuestionOptionByQuizQuestionOptionId(leftQuizQuestionOptionId);
                QuizQuestionOption rightQuizQuestionOption = quizQuestionOptionService.getQuizQuestionOptionByQuizQuestionOptionId(rightQuizQuestionOptionId);
                StudentAttemptAnswer newStudentAttemptAnswer;

                newStudentAttemptAnswer = new StudentAttemptAnswer(leftQuizQuestionOption.getLeftContent(), rightQuizQuestionOption.getRightContent());

                Set<ConstraintViolation<StudentAttemptAnswer>> constraintViolations = validator.validate(newStudentAttemptAnswer);
                if (constraintViolations.isEmpty())
                {
                    // Persist StudentAttemptQuestion
                    studentAttemptAnswerRepository.saveAndFlush(newStudentAttemptAnswer);
                    return newStudentAttemptAnswer;
                }
                else
                {
                    throw new InputDataValidationException(MessageFormatterUtil.prepareInputDataValidationErrorsMessage(constraintViolations));
                }
            }
            else
            {
                throw new CreateNewStudentAttemptAnswerException("QuizQuestionOptionId cannot be null");
            }
        }
        catch (DataAccessException ex)
        {
            throw new UnknownPersistenceException(ex.getMessage());
        }
    }

    @Override
    public StudentAttemptAnswer getStudentAttemptAnswerByStudentAttemptAnswerId(Long studentAttemptAnswerId) throws StudentAttemptAnswerNotFoundException
    {
        StudentAttemptAnswer studentAttemptAnswer = studentAttemptAnswerRepository.findById(studentAttemptAnswerId).orElse(null);

        if (studentAttemptAnswer != null)
        {
            return studentAttemptAnswer;
        }
        else
        {
            throw new StudentAttemptAnswerNotFoundException("StudentAttemptAnswer with ID: " + studentAttemptAnswerId + " does not exist!");
        }
    }

    @Override
    public List<StudentAttemptAnswer> getAllStudentAttemptAnswers()
    {
        return studentAttemptAnswerRepository.findAll();
    }

    @Override
    public StudentAttemptAnswer updateStudentAttemptAnswer(StudentAttemptAnswer studentAttemptAnswer) throws UpdateStudentAttemptAnswerException, StudentAttemptAnswerNotFoundException
    {
        if (studentAttemptAnswer != null)
        {
            if (studentAttemptAnswer.getStudentAttemptAnswerId() != null)
            {
                StudentAttemptAnswer studentAttemptAnswerToUpdate = getStudentAttemptAnswerByStudentAttemptAnswerId(studentAttemptAnswer.getStudentAttemptAnswerId());

                // Update Non-Relational Fields
                studentAttemptAnswerToUpdate.setLeftContent(studentAttemptAnswer.getLeftContent());
                studentAttemptAnswerToUpdate.setRightContent(studentAttemptAnswer.getRightContent());
                studentAttemptAnswerToUpdate.setCorrect(studentAttemptAnswer.getCorrect());
                studentAttemptAnswerToUpdate.setMarks(studentAttemptAnswer.getMarks());

                studentAttemptAnswerToUpdate = studentAttemptAnswerRepository.saveAndFlush(studentAttemptAnswerToUpdate);
                return studentAttemptAnswerToUpdate;
            }
            else
            {
                throw new UpdateStudentAttemptAnswerException("StudentAttemptAnswer ID not provided for account to be updated");
            }
        }
        else
        {
            throw new UpdateStudentAttemptAnswerException("StudentAttemptAnswer not provided for account to be updated");
        }
    }
}
