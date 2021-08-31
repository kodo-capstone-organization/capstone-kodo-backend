package com.spring.kodo.service.impl;

import com.spring.kodo.entity.Content;
import com.spring.kodo.entity.Multimedia;
import com.spring.kodo.repository.MultimediaRepository;
import com.spring.kodo.service.FileService;
import com.spring.kodo.service.MultimediaService;
import com.spring.kodo.util.MessageFormatterUtil;
import com.spring.kodo.util.enumeration.MultimediaType;
import com.spring.kodo.util.exception.InputDataValidationException;
import com.spring.kodo.util.exception.MultimediaNotFoundException;
import com.spring.kodo.util.exception.UnknownPersistenceException;
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
public class MultimediaServiceImpl implements MultimediaService
{
    @Autowired
    private MultimediaRepository multimediaRepository;

    @Autowired
    private FileService fileService;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public MultimediaServiceImpl()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Multimedia createNewMultimedia(Multimedia newMultimedia) throws InputDataValidationException, UnknownPersistenceException
    {
        try
        {
            Set<ConstraintViolation<Content>> constraintViolations = validator.validate(newMultimedia);
            if (constraintViolations.isEmpty())
            {
                multimediaRepository.saveAndFlush(newMultimedia);
                return newMultimedia;
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
    public Multimedia getMultimediaByMultimediaId(Long multimediaId) throws MultimediaNotFoundException
    {
        Multimedia multimedia = multimediaRepository.findById(multimediaId).orElse(null);

        if (multimedia != null)
        {
            return multimedia;
        }
        else
        {
            throw new MultimediaNotFoundException("Multimedia with ID: " + multimediaId + " does not exist!");
        }
    }

    @Override
    public Multimedia getMultimediaByUrl(String url) throws MultimediaNotFoundException
    {
        Multimedia multimedia = multimediaRepository.findByUrl(url).orElse(null);

        if (multimedia != null)
        {
            return multimedia;
        }
        else
        {
            throw new MultimediaNotFoundException("Multimedia with Url: " + url + " does not exist!");
        }
    }

    @Override
    public Multimedia getMultimediaByType(MultimediaType multimediaType) throws MultimediaNotFoundException
    {
        Multimedia multimedia = multimediaRepository.findByType(multimediaType).orElse(null);

        if (multimedia != null)
        {
            return multimedia;
        }
        else
        {
            throw new MultimediaNotFoundException("Multimedia with MultimediaType: " + multimediaType + " does not exist!");
        }
    }

    @Override
    public List<Multimedia> getAllMultimedias()
    {
        return multimediaRepository.findAll();
    }

    @Override  // File handling should have been done before calling this method
    public Multimedia updateMultimedia(Multimedia multimedia)
    {
        return multimediaRepository.saveAndFlush(multimedia);
    }

    @Override
    public void deleteMultimedia(Long contentId) throws MultimediaNotFoundException
    {
        Multimedia multimediaToRemove = getMultimediaByMultimediaId(contentId);
        // Delete from filestore
        fileService.delete(multimediaToRemove.getUrlFilename());
        // Delete from db
        multimediaRepository.delete(multimediaToRemove);
    }
}
