package com.spring.kodo.service.inter;

import com.spring.kodo.entity.EnrolledContent;
import com.spring.kodo.util.exception.*;

import java.util.List;

public interface EnrolledContentService
{
    EnrolledContent createNewEnrolledContent(Long parentContentId) throws InputDataValidationException, UnknownPersistenceException, CreateNewEnrolledContentException, ContentNotFoundException;

    EnrolledContent getEnrolledContentByEnrolledContentId(Long enrolledContentId) throws EnrolledContentNotFoundException;

    List<EnrolledContent> getAllEnrolledContents();
}
