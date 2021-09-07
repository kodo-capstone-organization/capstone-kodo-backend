package com.spring.kodo.service.inter;

import com.spring.kodo.entity.Multimedia;
import com.spring.kodo.util.enumeration.MultimediaType;
import com.spring.kodo.util.exception.*;

import java.util.List;

public interface MultimediaService
{
    Multimedia createNewMultimedia(Multimedia multimedia) throws InputDataValidationException, MultimediaExistsException, UnknownPersistenceException;

    List<Multimedia> getAllMultimedias();

    Multimedia getMultimediaByMultimediaId(Long multimediaId) throws MultimediaNotFoundException;

    Multimedia getMultimediaByUrl(String url) throws MultimediaNotFoundException;

    Multimedia getMultimediaByType(MultimediaType multimediaType) throws MultimediaNotFoundException;

    // File handling should have been done before calling this method
    Multimedia updateMultimedia(Multimedia multimedia);

    void deleteMultimedia(Long contentId) throws MultimediaNotFoundException;
}
