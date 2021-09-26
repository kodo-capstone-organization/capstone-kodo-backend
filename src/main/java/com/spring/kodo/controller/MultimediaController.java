package com.spring.kodo.controller;

import com.spring.kodo.entity.Content;
import com.spring.kodo.entity.Lesson;
import com.spring.kodo.entity.Multimedia;
import com.spring.kodo.service.inter.FileService;
import com.spring.kodo.service.inter.LessonService;
import com.spring.kodo.service.inter.MultimediaService;
import com.spring.kodo.util.FileTypeUtil;
import com.spring.kodo.util.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(path="/multimedia")
public class MultimediaController
{
    Logger logger = LoggerFactory.getLogger(MultimediaController.class);

    @Autowired
    private MultimediaService multimediaService;

    @Autowired
    private FileService fileService;

    @Autowired
    private LessonService lessonService;

    @PostMapping("/addNewMultimediaToLesson")
    public Multimedia addNewMultimediaToLesson(@RequestPart(name = "lessonId", required = true) Long lessonId, @RequestPart(name = "file", required = true) MultipartFile file,
                                                   @RequestPart(name = "name", required = true) String name, @RequestPart(name = "description", required = true) String description)
    {
        try {
            String url = this.fileService.upload(file);

            Multimedia multimedia = multimediaService.createNewMultimedia(new Multimedia(name, description, url, FileTypeUtil.getMultimediaType(file.getOriginalFilename())));

            Lesson lesson = this.lessonService.getLessonByLessonId(lessonId);
            this.lessonService.addContentToLesson(lesson, multimedia);

            return multimedia;
        }
        catch (FileUploadToGCSException | UnknownPersistenceException | InputDataValidationException | MultimediaExistsException | UpdateContentException ex)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        catch (LessonNotFoundException | ContentNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @PostMapping("/updateMultimedia")
    public Multimedia updateMultimedia(@RequestPart(name = "multimediaId", required = true) Long multimediaId, @RequestPart(name = "file", required = false) MultipartFile file,
                                       @RequestPart(name = "name", required = true) String name, @RequestPart(name = "description", required = true) String description)
    {
        try
        {
            Multimedia multimedia = this.multimediaService.getMultimediaByMultimediaId(multimediaId);

            if (file != null)
            {
                this.fileService.delete(multimedia.getUrlFilename());
                String newUrl = this.fileService.upload(file);
                multimedia.setUrl(newUrl);
                multimedia.setMultimediaType(FileTypeUtil.getMultimediaType(file.getOriginalFilename()));
            }

            multimedia.setName(name);
            multimedia.setDescription(description);

            return this.multimediaService.updateMultimedia(multimedia);
        }
        catch (MultimediaNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
        catch (FileUploadToGCSException ex)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @DeleteMapping("/deleteMultimediasFromLesson")
    public Boolean deleteMultimediasFromLesson(@RequestPart(name = "lessonId", required = true) Long lessonId,@RequestPart(name = "contentIds", required = true) List<Long> contentIds)
    {
        try
        {
            Lesson lesson = this.lessonService.getLessonByLessonId(lessonId);
            List<Long> updatedContentIds = lesson.getContents().stream().filter((Content content) -> !contentIds.contains(content.getContentId())).map((Content content) -> content.getContentId()).toList();

            this.lessonService.updateLesson(lesson, updatedContentIds);

            for (Long contentId : contentIds) this.multimediaService.deleteMultimedia(contentId);

            return true;
        } catch (LessonNotFoundException | ContentNotFoundException | MultimediaNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (InputDataValidationException | UpdateContentException | UnknownPersistenceException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }
}
