package com.spring.kodo.controller;

import com.spring.kodo.entity.Account;
import com.spring.kodo.entity.EnrolledContent;
import com.spring.kodo.restentity.request.CompleteMultimediaReq;
import com.spring.kodo.service.inter.AccountService;
import com.spring.kodo.service.inter.CourseService;
import com.spring.kodo.service.inter.EnrolledContentService;
import com.spring.kodo.service.inter.EnrolledLessonService;
import com.spring.kodo.util.exception.AccountNotFoundException;
import com.spring.kodo.util.exception.EnrolledContentNotFoundException;
import com.spring.kodo.util.exception.EnrolledCourseNotFoundException;
import com.spring.kodo.util.exception.EnrolledLessonNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(path = "/enrolledContent")
public class EnrolledContentController
{
    Logger logger = LoggerFactory.getLogger(EnrolledContentController.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private EnrolledContentService enrolledContentService;

    @GetMapping("/getEnrolledContentByEnrolledContentId/{enrolledContentId}")
    public EnrolledContent getEnrolledContentByEnrolledContentId(@PathVariable Long enrolledContentId)
    {
        try
        {
            EnrolledContent enrolledContent = enrolledContentService.getEnrolledContentByEnrolledContentId(enrolledContentId);

            return enrolledContent;
        }
        catch (EnrolledContentNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getEnrolledContentByEnrolledContentIdAndAccountId/{enrolledContentId}/{accountId}")
    public EnrolledContent getEnrolledContentByEnrolledContentIdAndAccountId(@PathVariable Long enrolledContentId, @PathVariable Long accountId)
    {
        try
        {
            Account account = this.accountService.getAccountByEnrolledContentId(enrolledContentId);

            if (account.getAccountId().equals(accountId))
            {
                EnrolledContent enrolledContent = enrolledContentService.getEnrolledContentByEnrolledContentId(enrolledContentId);

                return enrolledContent;
            }
            else
            {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have access to this content");
            }
        }
        catch (AccountNotFoundException | EnrolledContentNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getEnrolledContentByAccountIdAndContentId/{accountId}/{contentId}")
    public EnrolledContent getEnrolledContentByAccountIdAndContentId(@PathVariable Long accountId, @PathVariable Long contentId)
    {
        try
        {
            EnrolledContent enrolledContent = enrolledContentService.getEnrolledContentByAccountIdAndContentId(accountId, contentId);

            return enrolledContent;
        }
        catch (EnrolledContentNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getAllEnrolledContents")
    public List<EnrolledContent> getAllEnrolledContents()
    {
        return this.enrolledContentService.getAllEnrolledContents();
    }

    @PostMapping("/setDateTimeOfCompletionOfEnrolledContentByEnrolledContentId")
    public EnrolledContent setDateTimeOfCompletionOfEnrolledContentByEnrolledContentId(@RequestPart(required = true, name = "completeMultimediaReq") CompleteMultimediaReq completeMultimediaReq)
    {
        if (completeMultimediaReq != null)
        {
            try
            {
                Boolean complete = completeMultimediaReq.getComplete();
                Long enrolledContentId = completeMultimediaReq.getEnrolledContentId();

                EnrolledContent enrolledContent = enrolledContentService.setDateTimeOfCompletionOfEnrolledContentByEnrolledContentId(complete, enrolledContentId);

                return enrolledContent;
            }
            catch (EnrolledLessonNotFoundException | EnrolledCourseNotFoundException | EnrolledContentNotFoundException ex)
            {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
            }
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Complete Multimedia Request");
        }
    }
}
