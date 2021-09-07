package com.spring.kodo.controller;

import com.spring.kodo.entity.Content;
import com.spring.kodo.entity.Lesson;
import com.spring.kodo.entity.Multimedia;
import com.spring.kodo.entity.Quiz;
import com.spring.kodo.restentity.request.MultimediaReq;
import com.spring.kodo.restentity.request.UpdateLessonReq;
import com.spring.kodo.service.inter.FileService;
import com.spring.kodo.service.inter.LessonService;
import com.spring.kodo.service.inter.MultimediaService;
import com.spring.kodo.service.inter.QuizService;
import com.spring.kodo.util.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/lesson")
public class LessonController
{
    Logger logger = LoggerFactory.getLogger(LessonController.class);

    @Autowired
    private LessonService lessonService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private MultimediaService multimediaService;

    @Autowired
    private FileService fileService;

    @GetMapping("/getAllLessons")
    public List<Lesson> getAllLessons()
    {
        return this.lessonService.getAllLessons();
    }

    @GetMapping("/getLessonByLessonId/{lessonId}")
    public Lesson getLessonByLessonId(@PathVariable Long lessonId)
    {
        try
        {
            return this.lessonService.getLessonByLessonId(lessonId);
        }
        catch (LessonNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    // To be called from CourseController. Lesson should not be updated as a standalone API
    protected List<Long> updateLessonsInACourse(List<UpdateLessonReq> updateLessonReqs) throws ContentNotFoundException, LessonNotFoundException, UpdateContentException, UnknownPersistenceException, CreateNewQuizException, InputDataValidationException, MultimediaExistsException, FileUploadToGCSException, MultimediaNotFoundException {

        List<Long> lessonIds = new ArrayList<>();

        // Update lessons and its contents first [SPECIAL CASE]
        for (UpdateLessonReq updateLessonReq: updateLessonReqs)
        {
            List<Long> contentIds = new ArrayList<>();

            // Retrieve or create quiz
            for (Quiz quiz: updateLessonReq.getQuizzes())
            {
                if (quiz.getContentId() == null)
                {
                    contentIds.add(this.quizService.createNewQuiz(quiz).getContentId());
                }
                else
                {
                    contentIds.add(quiz.getContentId());
                }
            }

            // Retrieve or create multimedia
            for (MultimediaReq multimediaReq: updateLessonReq.getMultimediaReqs())
            {
                Multimedia multimedia = null;

                if (multimediaReq.getMultimedia().getContentId() == null)
                {
                    multimedia = this.multimediaService.createNewMultimedia(multimediaReq.getMultimedia());
                }
                else
                {
                    multimedia = this.multimediaService.getMultimediaByMultimediaId(multimediaReq.getMultimedia().getContentId());
                }

                // If a file is attached, upload it and get a url
                if (multimediaReq.getMultipartFile() != null)
                {
                    String url = this.fileService.upload(multimediaReq.getMultipartFile());
                    multimedia.setUrl(url);
                }

                // Update multimedia and save contentId
                Content updatedMultimedia = this.multimediaService.updateMultimedia(multimedia);
                contentIds.add(updatedMultimedia.getContentId());
            }

            // Update lesson with updated contentIds
            Lesson updatedLesson = this.lessonService.updateLesson(updateLessonReq.getLesson(), contentIds);
            lessonIds.add(updatedLesson.getLessonId());
        }

        // Return list of updated lesson ids
        return lessonIds;
    }
}
