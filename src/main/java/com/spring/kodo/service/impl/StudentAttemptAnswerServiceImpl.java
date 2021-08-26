package com.spring.kodo.service.impl;

import com.spring.kodo.entity.StudentAttemptAnswer;
import com.spring.kodo.repository.StudentAttemptAnswerRepository;
import com.spring.kodo.service.StudentAttemptAnswerService;
import com.spring.kodo.util.exception.InputDataValidationException;
import com.spring.kodo.util.exception.StudentAttemptAnswerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;

@Service
public class StudentAttemptAnswerServiceImpl implements StudentAttemptAnswerService
{
    @Autowired
    private StudentAttemptAnswerRepository studentAttemptAnswerRepository;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public StudentAttemptAnswerServiceImpl()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public StudentAttemptAnswer createNewStudentAttemptAnswer(StudentAttemptAnswer studentAttemptAnswer) throws InputDataValidationException
    {
        return null;
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
