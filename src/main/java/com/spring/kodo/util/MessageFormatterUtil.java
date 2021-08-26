package com.spring.kodo.util;

import javax.validation.ConstraintViolation;
import java.util.Set;

public class MessageFormatterUtil
{
    public static String prepareInputDataValidationErrorsMessage(Set<? extends ConstraintViolation<?>> constraintViolations)
    {
        String msg = "Input data validation error!:";

        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

    public static String getGCSObjectNameFromMediaLink(String mediaLink)
    {
        if (mediaLink != null && mediaLink != "")
        {
            int idx = mediaLink.lastIndexOf('/');
            String lastString = mediaLink.substring(idx + 1);
            // c63e7cf4-030f-49e8-9ab3-ae94f93dc442.jpg?generation=1629954876596626&alt=media
            return lastString.split("\\?")[0];
        }
        else
        {
            return "";
        }
    }
}
