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
}
