package com.spring.kodo.service.impl;

import com.spring.kodo.entity.StudentAttempt;
import com.spring.kodo.repository.StudentAttemptRepository;
import com.spring.kodo.service.StudentAttemptService;
import com.spring.kodo.util.exception.InputDataValidationException;
import com.spring.kodo.util.exception.StudentAttemptNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;

@Service
public class StudentAttemptServiceImpl implements StudentAttemptService
{
    @Autowired
    private StudentAttemptRepository studentAttemptRepository;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public StudentAttemptServiceImpl()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public StudentAttempt createNewStudentAttempt(StudentAttempt studentAttempt) throws InputDataValidationException
    {
        return null;
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

}
