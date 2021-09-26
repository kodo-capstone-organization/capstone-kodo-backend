package com.spring.kodo.service.impl;

import com.spring.kodo.entity.Quiz;
import com.spring.kodo.entity.QuizQuestion;
import com.spring.kodo.entity.QuizQuestionOption;
import com.spring.kodo.repository.QuizQuestionRepository;
import com.spring.kodo.service.inter.QuizQuestionOptionService;
import com.spring.kodo.service.inter.QuizQuestionService;
import com.spring.kodo.service.inter.QuizService;
import com.spring.kodo.util.FormatterUtil;
import com.spring.kodo.util.enumeration.QuestionType;
import com.spring.kodo.util.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

@Service
public class QuizQuestionServiceImpl implements QuizQuestionService
{
    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    @Autowired
    private QuizService quizService;

    @Autowired
    private QuizQuestionOptionService quizQuestionOptionService;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public QuizQuestionServiceImpl()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public QuizQuestion createNewQuizQuestion(QuizQuestion newQuizQuestion, Long quizId) throws CreateNewQuizQuestionException, InputDataValidationException, UnknownPersistenceException
    {
        try
        {
            Set<ConstraintViolation<QuizQuestion>> constraintViolations = validator.validate(newQuizQuestion);
            if (constraintViolations.isEmpty())
            {
                try
                {
                    Quiz quiz = quizService.getQuizByQuizId(quizId);
                    newQuizQuestion.setQuiz(quiz);

                    quizQuestionRepository.saveAndFlush(newQuizQuestion);
                    return newQuizQuestion;
                }
                catch (QuizNotFoundException ex)
                {
                    throw new CreateNewQuizQuestionException(ex.getMessage());
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
    public QuizQuestion getQuizQuestionByQuizQuestionId(Long quizQuestionId) throws QuizQuestionNotFoundException
    {
        QuizQuestion quizQuestion = quizQuestionRepository.findById(quizQuestionId).orElse(null);

        if (quizQuestion != null)
        {
            return quizQuestion;
        }
        else
        {
            throw new QuizQuestionNotFoundException("QuizQuestion with ID: " + quizQuestionId + " does not exist!");
        }
    }

    @Override
    public QuizQuestion getQuizQuestionByContent(String content) throws QuizQuestionNotFoundException
    {
        QuizQuestion quizQuestion = quizQuestionRepository.findByContent(content).orElse(null);

        if (quizQuestion != null)
        {
            return quizQuestion;
        }
        else
        {
            throw new QuizQuestionNotFoundException("QuizQuestion with Content: " + content + " does not exist!");
        }
    }

    @Override
    public QuizQuestion getQuizQuestionByQuestionType(QuestionType questionType) throws QuizQuestionNotFoundException
    {
        QuizQuestion quizQuestion = quizQuestionRepository.findByQuestionType(questionType).orElse(null);

        if (quizQuestion != null)
        {
            return quizQuestion;
        }
        else
        {
            throw new QuizQuestionNotFoundException("QuizQuestion with QuestionType: " + questionType + " does not exist!");
        }
    }

    @Override
    public List<QuizQuestion> getAllQuizQuestions()
    {
        return quizQuestionRepository.findAll();
    }

    @Override
    public List<QuizQuestion> getAllQuizQuestionsByQuizId(Long quizId)
    {
        return quizQuestionRepository.findAllQuizQuestionsByQuizId(quizId);
    }

    @Override
    public List<QuizQuestion> getAllQuizQuestionsByTutorId(Long tutorId)
    {
        return quizQuestionRepository.findAllQuizQuestionsByTutorId(tutorId);
    }

    @Override
    public QuizQuestion addQuizQuestionOptionToQuizQuestion(QuizQuestion quizQuestion, QuizQuestionOption quizQuestionOption) throws UpdateQuizQuestionException, QuizQuestionNotFoundException, QuizQuestionOptionNotFoundException
    {
        if (quizQuestion != null)
        {
            if (quizQuestion.getQuizQuestionId() != null)
            {
                quizQuestion = getQuizQuestionByQuizQuestionId(quizQuestion.getQuizQuestionId());

                if (quizQuestionOption != null)
                {
                    if (quizQuestionOption.getQuizQuestionOptionId() != null)
                    {
                        quizQuestionOption = quizQuestionOptionService.getQuizQuestionOptionByQuizQuestionOptionId(quizQuestionOption.getQuizQuestionOptionId());
                        quizQuestion.getQuizQuestionOptions().add(quizQuestionOption);

                        quizQuestionRepository.saveAndFlush(quizQuestion);
                        return quizQuestion;
                    }
                    else
                    {
                        throw new UpdateQuizQuestionException("QuizQuestionOption ID cannot be null");
                    }
                }
                else
                {
                    throw new UpdateQuizQuestionException("QuizQuestionOption cannot be null");
                }
            }
            else
            {
                throw new UpdateQuizQuestionException("QuizQuestion ID cannot be null");
            }
        }
        else
        {
            throw new UpdateQuizQuestionException("QuizQuestion cannot be null");
        }
    }

    @Override
    public QuizQuestion addQuizQuestionOptionsToQuizQuestion(QuizQuestion quizQuestion, List<QuizQuestionOption> quizQuestionOptions) throws UpdateQuizQuestionException, QuizQuestionNotFoundException, QuizQuestionOptionNotFoundException
    {
        if (quizQuestion != null)
        {
            if (quizQuestion.getQuizQuestionId() != null)
            {
                quizQuestion = getQuizQuestionByQuizQuestionId(quizQuestion.getQuizQuestionId());

                for (int i = 0; i < quizQuestionOptions.size(); i++)
                {
                    QuizQuestionOption quizQuestionOption = quizQuestionOptions.get(i);
                    if (quizQuestionOption != null)
                    {
                        if (quizQuestionOption.getQuizQuestionOptionId() != null)
                        {
                            quizQuestionOption = quizQuestionOptionService.getQuizQuestionOptionByQuizQuestionOptionId(quizQuestionOption.getQuizQuestionOptionId());
                            quizQuestion.getQuizQuestionOptions().add(quizQuestionOption);

                            quizQuestionOptions.set(i, quizQuestionOption);
                        }
                        else
                        {
                            throw new UpdateQuizQuestionException("QuizQuestionOption ID cannot be null");
                        }
                    }
                    else
                    {
                        throw new UpdateQuizQuestionException("QuizQuestionOption cannot be null");
                    }
                }

                quizQuestionRepository.saveAndFlush(quizQuestion);
                return quizQuestion;
            }
            else
            {
                throw new UpdateQuizQuestionException("QuizQuestion ID cannot be null");
            }
        }
        else
        {
            throw new UpdateQuizQuestionException("QuizQuestion cannot be null");
        }
    }

    @Override
    public QuizQuestion updateQuizQuestion(QuizQuestion quizQuestion) throws UpdateQuizQuestionException, QuizQuestionNotFoundException, InputDataValidationException
    {
        if (quizQuestion != null)
        {
            if (quizQuestion.getQuizQuestionId() != null)
            {
                Set<ConstraintViolation<QuizQuestion>> constraintViolations = validator.validate(quizQuestion);

                if (constraintViolations.isEmpty())
                {
                    QuizQuestion quizQuestionToUpdate = getQuizQuestionByQuizQuestionId(quizQuestion.getQuizQuestionId());

                    quizQuestionToUpdate.setContent(quizQuestion.getContent());
                    quizQuestionToUpdate.setQuestionType(quizQuestion.getQuestionType());
                    quizQuestionToUpdate.setMarks(quizQuestion.getMarks());

                    quizQuestionRepository.saveAndFlush(quizQuestionToUpdate);
                    return quizQuestionToUpdate;
                }
                else
                {
                    throw new InputDataValidationException(FormatterUtil.prepareInputDataValidationErrorsMessage(constraintViolations));
                }
            }
            else
            {
                throw new UpdateQuizQuestionException("QuizQuestion ID cannot be null");
            }
        }
        else
        {
            throw new UpdateQuizQuestionException("QuizQuestion cannot be null");
        }
    }

    @Override
    public QuizQuestion updateQuizQuestion(QuizQuestion quizQuestion, List<QuizQuestionOption> quizQuestionOptions) throws UpdateQuizQuestionException, QuizQuestionNotFoundException, QuizQuestionOptionNotFoundException, UpdateQuizQuestionOptionException, InputDataValidationException
    {
        QuizQuestion quizQuestionToUpdate = updateQuizQuestion(quizQuestion);

        if (quizQuestionOptions != null)
        {
            boolean update;

            for (QuizQuestionOption quizQuestionOption : quizQuestionOptions)
            {
                update = false;

                for (QuizQuestionOption quizQuestionOptionToCheck : quizQuestionToUpdate.getQuizQuestionOptions())
                {
                    if (quizQuestionOptionToCheck.getQuizQuestionOptionId().equals(quizQuestionOption.getQuizQuestionOptionId()))
                    {
                        update = true;
                        break;
                    }
                }

                if (update)
                {
                    quizQuestionOptionService.updateQuizQuestionOption(quizQuestionOption);
                }
                else
                {
                    quizQuestionToUpdate = addQuizQuestionOptionToQuizQuestion(quizQuestionToUpdate, quizQuestionOption);
                }
            }
        }

        quizQuestionRepository.saveAndFlush(quizQuestionToUpdate);
        return quizQuestionToUpdate;
    }

    @Override
    public Boolean deleteQuizQuestionByQuizQuestionId(Long quizQuestionId) throws DeleteQuizQuestionException, QuizQuestionNotFoundException
    {
        if (quizQuestionId != null)
        {
            QuizQuestion quizQuestionToDelete = getQuizQuestionByQuizQuestionId(quizQuestionId);

            if (!isQuizQuestionContainsStudentAttemptQuestionsByQuizQuestionId(quizQuestionId))
            {
                if (quizQuestionToDelete.getQuizQuestionOptions().size() == 0)
                {
                    quizQuestionRepository.delete(quizQuestionToDelete);
                    return true;
                }
                else
                {
                    throw new DeleteQuizQuestionException("QuizQuestion that has QuizQuestionOptions cannot be deleted");
                }
            }
            else
            {
                throw new DeleteQuizQuestionException("QuizQuestion that has StudentAttemptQuestions cannot be deleted");
            }
        }
        else
        {
            throw new DeleteQuizQuestionException("QuizQuestion ID cannot be null");
        }
    }

    @Override
    public Boolean deleteQuizQuestionWithQuizQuestionOptionsByQuizQuestionId(Long quizQuestionId) throws DeleteQuizQuestionException, QuizQuestionNotFoundException, QuizQuestionOptionNotFoundException, DeleteQuizQuestionOptionException
    {
        if (quizQuestionId != null)
        {
            QuizQuestion quizQuestionToDelete = getQuizQuestionByQuizQuestionId(quizQuestionId);
            List<QuizQuestionOption> quizQuestionOptionsToDelete = quizQuestionToDelete.getQuizQuestionOptions();

            for (QuizQuestionOption quizQuestionOption : quizQuestionOptionsToDelete)
            {
                quizQuestionOptionService.deleteQuizQuestionOptionByQuizQuestionOptionId(quizQuestionOption.getQuizQuestionOptionId());
            }
            quizQuestionToDelete.getQuizQuestionOptions().clear();

            quizQuestionRepository.delete(quizQuestionToDelete);
            return true;
        }
        else
        {
            throw new DeleteQuizQuestionException("QuizQuestion ID cannot be null");
        }
    }

    @Override
    public Boolean isQuizQuestionContainsStudentAttemptQuestionsByQuizQuestionId(Long quizQuestionId)
    {
        return quizQuestionRepository.containsStudentAttemptQuestions(quizQuestionId);
    }
}
