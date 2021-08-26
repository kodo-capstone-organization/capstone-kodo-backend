package com.spring.kodo.service.impl;

import com.spring.kodo.entity.Multimedia;
import com.spring.kodo.repository.MultimediaRepository;
import com.spring.kodo.service.MultimediaService;
import com.spring.kodo.util.enumeration.MultimediaType;
import com.spring.kodo.util.exception.MultimediaNotFoundException;
import com.spring.kodo.util.exception.InputDataValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;

@Service
public class MultimediaServiceImpl implements MultimediaService
{
    @Autowired
    private MultimediaRepository multimediaRepository;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public MultimediaServiceImpl()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Multimedia createNewMultimedia(Multimedia multimedia) throws InputDataValidationException
    {
        return null;
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
}
