package com.spring.kodo.service.impl;

import com.spring.kodo.entity.StudentAttemptQuestion;
import com.spring.kodo.repository.StudentAttemptQuestionRepository;
import com.spring.kodo.service.StudentAttemptQuestionService;
import com.spring.kodo.util.exception.StudentAttemptQuestionNotFoundException;
import com.spring.kodo.util.exception.InputDataValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;

@Service
public class StudentAttemptQuestionServiceImpl implements StudentAttemptQuestionService
{
    @Autowired
    private StudentAttemptQuestionRepository studentAttemptQuestionRepository;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public StudentAttemptQuestionServiceImpl()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public StudentAttemptQuestion createNewStudentAttemptQuestion(StudentAttemptQuestion studentAttemptQuestion) throws InputDataValidationException
    {
        return null;
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
}
