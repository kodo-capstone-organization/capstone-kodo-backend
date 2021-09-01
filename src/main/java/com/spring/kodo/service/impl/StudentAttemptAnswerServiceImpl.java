package com.spring.kodo.service.impl;

import com.spring.kodo.entity.QuizQuestionOption;
import com.spring.kodo.entity.StudentAttemptAnswer;
import com.spring.kodo.repository.StudentAttemptAnswerRepository;
import com.spring.kodo.service.QuizQuestionOptionService;
import com.spring.kodo.service.StudentAttemptAnswerService;
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
    public StudentAttemptAnswer createNewStudentAttemptAnswer(Long quizQuestionOptionId) throws InputDataValidationException, CreateNewStudentAttemptAnswerException, QuizQuestionOptionNotFoundException, UnknownPersistenceException
    {
        try
        {
            StudentAttemptAnswer newStudentAttemptAnswer = new StudentAttemptAnswer();

            Set<ConstraintViolation<StudentAttemptAnswer>> constraintViolations = validator.validate(newStudentAttemptAnswer);
            if (constraintViolations.isEmpty())
            {
                if (quizQuestionOptionId != null)
                {
                    QuizQuestionOption quizQuestionOption = quizQuestionOptionService.getQuizQuestionOptionByQuizQuestionOptionId(quizQuestionOptionId);
                    newStudentAttemptAnswer.setQuizQuestionOption(quizQuestionOption);

                    // Persist StudentAttemptQuestion
                    studentAttemptAnswerRepository.saveAndFlush(newStudentAttemptAnswer);
                    return newStudentAttemptAnswer;
                }
                else
                {
                    throw new CreateNewStudentAttemptAnswerException("QuizQuestionOptionId cannot be null");
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
}
