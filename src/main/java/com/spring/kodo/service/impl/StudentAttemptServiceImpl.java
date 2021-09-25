package com.spring.kodo.service.impl;

import com.spring.kodo.entity.*;
import com.spring.kodo.repository.StudentAttemptRepository;
import com.spring.kodo.service.inter.EnrolledContentService;
import com.spring.kodo.service.inter.StudentAttemptAnswerService;
import com.spring.kodo.service.inter.StudentAttemptQuestionService;
import com.spring.kodo.service.inter.StudentAttemptService;
import com.spring.kodo.util.FormatterUtil;
import com.spring.kodo.util.enumeration.QuestionType;
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
public class StudentAttemptServiceImpl implements StudentAttemptService
{
    @Autowired
    private StudentAttemptRepository studentAttemptRepository;

    @Autowired
    private StudentAttemptQuestionService studentAttemptQuestionService;

    @Autowired
    private StudentAttemptAnswerService studentAttemptAnswerService;

    @Autowired
    private EnrolledContentService enrolledContentService;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public StudentAttemptServiceImpl()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public StudentAttempt createNewStudentAttempt(Long enrolledContentId) throws UnknownPersistenceException, InputDataValidationException, CreateNewStudentAttemptException, EnrolledContentNotFoundException, CreateNewStudentAttemptQuestionException, QuizQuestionNotFoundException
    {
        try
        {
            StudentAttempt newStudentAttempt = new StudentAttempt();

            Set<ConstraintViolation<StudentAttempt>> constraintViolations = validator.validate(newStudentAttempt);
            if (constraintViolations.isEmpty())
            {
                if (enrolledContentId != null)
                {
                    EnrolledContent enrolledContent = enrolledContentService.getEnrolledContentByEnrolledContentId(enrolledContentId);

                    if (enrolledContent.getParentContent() instanceof Quiz)
                    {
                        Quiz quiz = (Quiz) enrolledContent.getParentContent();
                        newStudentAttempt.setQuiz(quiz);
                        quiz.getStudentAttempts().add(newStudentAttempt);

                        // Create StudentAttemptQuestion
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
                        throw new CreateNewStudentAttemptException("Quiz ID cannot be null");
                    }
                }
                else
                {
                    throw new CreateNewStudentAttemptException("Quiz ID cannot be null");
                }
            }
            else
            {
                throw new InputDataValidationException(FormatterUtil.prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        catch (DataAccessException ex)
        {
            throw new UnknownPersistenceException(ex.getMessage());
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

    @Override
    public Long deleteStudentAttemptByStudentAttemptId(Long studentAttemptId) throws StudentAttemptNotFoundException
    {
        StudentAttempt studentAttempt = getStudentAttemptByStudentAttemptId(studentAttemptId);
        studentAttemptRepository.delete(studentAttempt);

        return studentAttemptId;
    }

    @Override
    public Boolean isStudentAttemptCompleted(Long studentAttemptId) throws StudentAttemptNotFoundException
    {
        StudentAttempt studentAttempt = getStudentAttemptByStudentAttemptId(studentAttemptId);
        Quiz quiz = studentAttempt.getQuiz();

        List<StudentAttemptQuestion> studentAttemptQuestions = studentAttempt.getStudentAttemptQuestions();
        List<QuizQuestion> quizQuestions = quiz.getQuizQuestions();

        if (studentAttemptQuestions.size() == quizQuestions.size())
        {
            StudentAttemptQuestion studentAttemptQuestion;
            QuizQuestion quizQuestion;

            List<StudentAttemptAnswer> studentAttemptAnswers;
            List<QuizQuestionOption> quizQuestionOptions;

            for (int i = 0; i < studentAttemptQuestions.size(); i++)
            {
                studentAttemptQuestion = studentAttemptQuestions.get(i);
                quizQuestion = quiz.getQuizQuestions().get(i);

                studentAttemptAnswers = studentAttemptQuestion.getStudentAttemptAnswers();
                quizQuestionOptions = quizQuestion.getQuizQuestionOptions();

                if (quizQuestion.getQuestionType().equals(QuestionType.MCQ))
                {
                    if (studentAttemptAnswers.size() == 0)
                    {
                        return false;
                    }
                }
                else if (quizQuestion.getQuestionType().equals(QuestionType.TF))
                {
                    if (studentAttemptAnswers.size() == 0)
                    {
                        return false;
                    }
                }
                else if (quizQuestion.getQuestionType().equals(QuestionType.MATCHING))
                {
                    if (studentAttemptAnswers.size() < quizQuestionOptions.size())
                    {
                        return false;
                    }
                }
                else
                {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public StudentAttempt markStudentAttemptByStudentAttemptId(Long studentAttemptId) throws StudentAttemptNotFoundException, UpdateStudentAttemptAnswerException, StudentAttemptAnswerNotFoundException
    {
        StudentAttempt studentAttempt = getStudentAttemptByStudentAttemptId(studentAttemptId);
        Quiz quiz = studentAttempt.getQuiz();

        List<StudentAttemptQuestion> studentAttemptQuestions = studentAttempt.getStudentAttemptQuestions();
        List<QuizQuestion> quizQuestions = quiz.getQuizQuestions();

        if (studentAttemptQuestions.size() == quizQuestions.size())
        {
            StudentAttemptQuestion studentAttemptQuestion;
            QuizQuestion quizQuestion;

            List<StudentAttemptAnswer> studentAttemptAnswers;
            List<QuizQuestionOption> quizQuestionOptions;

            for (int i = 0; i < studentAttemptQuestions.size(); i++)
            {
                studentAttemptQuestion = studentAttemptQuestions.get(i);
                quizQuestion = quiz.getQuizQuestions().get(i);

                studentAttemptAnswers = studentAttemptQuestion.getStudentAttemptAnswers();
                quizQuestionOptions = quizQuestion.getQuizQuestionOptions();

                if (quizQuestion.getQuestionType().equals(QuestionType.MCQ))
                {
                    // Check options against one another
                    // Duplicated but wait for FE implementation, check if it works well before merge
                    for (QuizQuestionOption quizQuestionOption : quizQuestionOptions)
                    {
                        if (quizQuestionOption.getCorrect())
                        {
                            for (StudentAttemptAnswer studentAttemptAnswer : studentAttemptAnswers)
                            {
                                if (studentAttemptAnswer.getCorrect() == null)
                                {
                                    if (quizQuestionOption.getLeftContent().equals(studentAttemptAnswer.getLeftContent()))
                                    {
                                        studentAttemptAnswer.setCorrect(true);
                                        studentAttemptAnswer.setMarks((double) quizQuestion.getMarks());
                                    }
                                    else
                                    {
                                        studentAttemptAnswer.setCorrect(false);
                                        studentAttemptAnswer.setMarks(0.0);
                                    }

                                    studentAttemptAnswerService.updateStudentAttemptAnswer(studentAttemptAnswer);
                                }
                            }
                        }
                    }
                }
                else if (quizQuestion.getQuestionType().equals(QuestionType.TF))
                {
                    // Check options against one another
                    // Duplicated but wait for FE implementation, check if it works well before merge
                    for (QuizQuestionOption quizQuestionOption : quizQuestionOptions)
                    {
                        if (quizQuestionOption.getCorrect())
                        {
                            for (StudentAttemptAnswer studentAttemptAnswer : studentAttemptAnswers)
                            {
                                if (studentAttemptAnswer.getCorrect() == null)
                                {
                                    if (quizQuestionOption.getLeftContent().equals(studentAttemptAnswer.getLeftContent()))
                                    {
                                        studentAttemptAnswer.setCorrect(true);
                                        studentAttemptAnswer.setMarks((double) quizQuestion.getMarks());
                                    }
                                    else
                                    {
                                        studentAttemptAnswer.setCorrect(false);
                                        studentAttemptAnswer.setMarks(0.0);
                                    }

                                    studentAttemptAnswerService.updateStudentAttemptAnswer(studentAttemptAnswer);
                                }
                            }
                        }
                    }
                }
                else if (quizQuestion.getQuestionType().equals(QuestionType.MATCHING))
                {
                    // Check options against one another
                    for (QuizQuestionOption quizQuestionOption : quizQuestionOptions)
                    {
                        for (StudentAttemptAnswer studentAttemptAnswer : studentAttemptAnswers)
                        {
                            if (studentAttemptAnswer.getCorrect() == null || !studentAttemptAnswer.getCorrect())
                            {
                                if (quizQuestionOption.getLeftContent().equals(studentAttemptAnswer.getLeftContent())
                                        && quizQuestionOption.getRightContent().equals(studentAttemptAnswer.getRightContent()))
                                {
                                    studentAttemptAnswer.setCorrect(true);
                                    studentAttemptAnswer.setMarks(FormatterUtil.round(quizQuestion.getMarks() / (double) quizQuestion.getQuizQuestionOptions().size(), 2));
                                }
                                else
                                {
                                    studentAttemptAnswer.setCorrect(false);
                                    studentAttemptAnswer.setMarks(0.0);
                                }

                                studentAttemptAnswerService.updateStudentAttemptAnswer(studentAttemptAnswer);
                            }
                        }
                    }
                }
            }
        }

        studentAttemptRepository.saveAndFlush(studentAttempt);
        return studentAttempt;
    }
}
