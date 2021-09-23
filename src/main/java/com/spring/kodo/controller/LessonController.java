package com.spring.kodo.controller;

import com.spring.kodo.entity.*;
import com.spring.kodo.restentity.request.MultimediaReq;
import com.spring.kodo.restentity.request.UpdateLessonReq;
import com.spring.kodo.service.inter.*;
import com.spring.kodo.util.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
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

    @Autowired
    private CourseService courseService;

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

    @PostMapping("/createNewLesson")
    public Lesson createNewLesson(@RequestPart(name = "courseId", required = true) Long courseId, @RequestPart(name = "name", required = true) String name,
                                  @RequestPart(name = "description", required = true) String description, @RequestPart(name = "sequence", required = true) Integer sequence)
    {
        try
        {
            Lesson lesson = this.lessonService.createNewLesson(new Lesson(name, description, sequence));

            Course course = this.courseService.getCourseByCourseId(courseId);
            this.courseService.addLessonToCourse(course, lesson);

            return lesson;
        }
        catch (UnknownPersistenceException | InputDataValidationException | UpdateCourseException ex)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        catch (CourseNotFoundException | LessonNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    // To be called from CourseController. Lesson should not be updated as a standalone API
    protected List<Long> updateLessonsInACourse(List<UpdateLessonReq> updateLessonReqs, List<MultipartFile>  lessonMultimedias) throws ContentNotFoundException, LessonNotFoundException, UpdateContentException, UnknownPersistenceException, CreateNewQuizException, InputDataValidationException, MultimediaExistsException, FileUploadToGCSException, MultimediaNotFoundException, CourseNotFoundException {

        List<Long> lessonIds = new ArrayList<>();

        // Update lessons and its contents first [SPECIAL CASE]
        for (UpdateLessonReq updateLessonReq: updateLessonReqs)
        {
            // Create new lesson
            if (updateLessonReq.getLesson().getLessonId() == null) {
                Lesson newLesson = lessonService.createNewLesson(new Lesson(updateLessonReq.getLesson().getName(), updateLessonReq.getLesson().getDescription(), updateLessonReq.getLesson().getSequence()));
                updateLessonReq.getLesson().setLessonId(newLesson.getLessonId());
            }

            List<Long> contentIds = new ArrayList<>();

            // Used to match file to multimediaReq
            HashMap<String, MultipartFile> fileMap = new HashMap<String, MultipartFile>();

            if (lessonMultimedias != null) {
                for (MultipartFile file : lessonMultimedias) {
                    fileMap.put(file.getOriginalFilename(), file);
                }
            }

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

                    // Append new multimedia to updateLessonReq
                    multimediaReq.setMultipartFile(fileMap.get(multimediaReq.getMultimedia().getNewFilename()));

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
