package com.spring.kodo.controller;

import com.spring.kodo.entity.*;
import com.spring.kodo.restentity.request.CreateNewCourseReq;
import com.spring.kodo.restentity.request.UpdateCourseReq;
import com.spring.kodo.restentity.response.CourseWithTutorAndRatingResp;
import com.spring.kodo.restentity.response.RecommendedCoursesWithTags;
import com.spring.kodo.service.inter.*;
import com.spring.kodo.util.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/course")
public class CourseController
{
    Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private TagService tagService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private FileService fileService;

    @Autowired
    private LessonController lessonController;

    @GetMapping("/getCourseByCourseId/{courseId}")
    public CourseWithTutorAndRatingResp getCourseByCourseId(@PathVariable Long courseId)
    {
        try
        {
            Course course = this.courseService.getCourseByCourseId(courseId);
            Account tutor = this.accountService.getAccountByCourseId(courseId);
            Double rating = this.courseService.getCourseRating(courseId);

            CourseWithTutorAndRatingResp courseWithTutorAndRatingResp = createCourseWithtutorResp(course, tutor, rating);

            return courseWithTutorAndRatingResp;
        }
        catch (CourseNotFoundException | AccountNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getAllCoursesWithActiveEnrollment")
    public List<CourseWithTutorAndRatingResp> getAllCoursesWithActiveEnrollment()
    {
        try
        {
            List<Course> courses = this.courseService.getAllCoursesWithActiveEnrollment();
            List<CourseWithTutorAndRatingResp> courseWithTutorAndRatingResps = getAllCoursesWithTutorsByCourses(courses);

            return courseWithTutorAndRatingResps;
        }
        catch (AccountNotFoundException | CourseNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getAllCourses")
    public List<CourseWithTutorAndRatingResp> getAllCourses()
    {
        try
        {
            List<Course> courses = this.courseService.getAllCourses();
            List<CourseWithTutorAndRatingResp> courseWithTutorAndRatingResps = getAllCoursesWithTutorsByCourses(courses);

            return courseWithTutorAndRatingResps;
        }
        catch (AccountNotFoundException | CourseNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getAllCoursesByTagTitle/{tagTitle}")
    public List<CourseWithTutorAndRatingResp> getAllCoursesByTagTitle(@PathVariable String tagTitle)
    {
        try
        {
            List<Course> courses = this.courseService.getAllCoursesByTagTitle(tagTitle);
            List<CourseWithTutorAndRatingResp> courseWithTutorAndRatingResps = getAllCoursesWithTutorsByCourses(courses);

            return courseWithTutorAndRatingResps;
        }
        catch (TagNotFoundException | AccountNotFoundException | CourseNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getAllCoursesByKeyword/{keyword}")
    public List<CourseWithTutorAndRatingResp> getAllCoursesByKeyword(@PathVariable String keyword)
    {
        try
        {
            List<Course> courses = this.courseService.getAllCoursesByKeyword(keyword);
            List<CourseWithTutorAndRatingResp> courseWithTutorAndRatingResps = getAllCoursesWithTutorsByCourses(courses);

            return courseWithTutorAndRatingResps;
        }
        catch (CourseWithKeywordNotFoundException | AccountNotFoundException | CourseNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }


    @GetMapping("/getAllCoursesToRecommendWithLimitByAccountId/{accountId}/{limit}")
    public RecommendedCoursesWithTags getAllCoursesToRecommendWithLimitByAccountId(@PathVariable Long accountId, @PathVariable Integer limit)
    {
        try
        {
            List<Tag> topTags = this.tagService.getTopRelevantTagsThroughFrequencyWithLimitByAccountId(accountId, limit);
            List<Long> topTagIds = topTags.stream().map(tag -> tag.getTagId()).collect(Collectors.toList());

            List<Course> allCoursesToRecommend = this.courseService.getAllCoursesToRecommendByAccountIdAndTagIds(accountId, topTagIds);

            List<CourseWithTutorAndRatingResp> courseWithTutorAndRatingResps = new ArrayList<>(allCoursesToRecommend.size());

            Account tutor;
            double courseRating;

            for (Course course : allCoursesToRecommend)
            {
                tutor = this.accountService.getAccountByCourseId(course.getCourseId());
                courseRating = this.courseService.getCourseRating(course.getCourseId());

                courseWithTutorAndRatingResps.add(
                        new CourseWithTutorAndRatingResp(course, tutor, courseRating)
                );
            }

            RecommendedCoursesWithTags recommendedCoursesWithTags = new RecommendedCoursesWithTags(courseWithTutorAndRatingResps, topTags);

            return recommendedCoursesWithTags;
        }
        catch (AccountNotFoundException | CourseNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @PostMapping("/createNewCourse")
    public Course createNewCourse(
            @RequestPart(name = "course", required = true) CreateNewCourseReq createNewCourseReq,
            @RequestPart(name = "bannerPicture", required = false) MultipartFile bannerPicture
    )
    {
        if (createNewCourseReq != null)
        {
            logger.info("HIT course/createNewCourse | POST | Received : " + createNewCourseReq);
            try
            {
                Course newCourse = new Course(createNewCourseReq.getName(), createNewCourseReq.getDescription(), createNewCourseReq.getPrice(), "");
                newCourse = this.courseService.createNewCourse(newCourse, createNewCourseReq.getTutorId(), createNewCourseReq.getTagTitles());

                // Handle banner picture upload
                if (bannerPicture != null)
                {
                    String bannerPictureURL = fileService.upload(bannerPicture);
                    newCourse.setBannerUrl(bannerPictureURL);
                    newCourse = this.courseService.updateCourse(newCourse, null, null, null);
                }

                // Important! Adding course to tutor account
                Account tutor = this.accountService.getAccountByAccountId(createNewCourseReq.getTutorId());
                this.accountService.addCourseToAccount(tutor, newCourse);

                return newCourse;
            }
            catch (InputDataValidationException | TagNameExistsException | CreateNewCourseException | UpdateCourseException | UpdateAccountException | FileUploadToGCSException ex)
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
            }
            catch (TagNotFoundException | AccountNotFoundException | CourseNotFoundException | EnrolledCourseNotFoundException | LessonNotFoundException ex)
            {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
            }
            catch (UnknownPersistenceException ex)
            {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
            }
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Create New Course Request");
        }
    }

    @PutMapping("/updateCourse")
    public Course updateCourse(
            @RequestPart(name="updateCourseReq", required = true) UpdateCourseReq updateCourseReq,
            @RequestPart(name="bannerPicture", required = false) MultipartFile updatedBannerPicture
    )
    {
        if (updateCourseReq != null)
        {
            logger.info("HIT course/updateCourse | PUT");
            try
            {
                // Update all lessons and their contents first
                List<Long> updatedLessonIds = lessonController.updateLessonsInACourse(updateCourseReq.getUpdateLessonReqs());

                // Update course
                Course updatedCourse = this.courseService.updateCourse(
                        updateCourseReq.getCourse(),
                        updateCourseReq.getEnrolledCourseIds(),
                        updatedLessonIds,
                        updateCourseReq.getCourseTagTitles());

                // Check if banner picture is also updated
                if (updatedBannerPicture != null)
                {
                    // Delete existing file in cloud
                    String currentBannerPictureFilename = updatedCourse.getBannerPictureFilename();
                    if (currentBannerPictureFilename != "")
                    {
                        Boolean isDeleted = fileService.delete(currentBannerPictureFilename);
                        if (!isDeleted)
                        {
                            System.err.println("Unable to delete previous banner picture: " + currentBannerPictureFilename + ". Proceeding to overwrite with new picture");
                        }
                    }
                    // Upload new file
                    String updatedBannerPictureURL = fileService.upload(updatedBannerPicture);
                    updatedCourse.setBannerUrl(updatedBannerPictureURL);
                    updatedCourse = courseService.updateCourse(updatedCourse, null, null, null);
                }

                return updatedCourse;
            }
            catch (TagNotFoundException | EnrolledCourseNotFoundException | CourseNotFoundException | ContentNotFoundException | LessonNotFoundException | MultimediaNotFoundException ex)
            {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
            }
            catch (UnknownPersistenceException | InputDataValidationException | TagNameExistsException | FileUploadToGCSException | UpdateContentException | CreateNewQuizException | MultimediaExistsException | UpdateCourseException ex)
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
            }
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Update Course Request");
        }
    }

    @DeleteMapping("/toggleEnrollmentActiveStatus/{courseId}&{requestingAccountId}")
    public ResponseEntity toggleEnrollmentActiveStatus(@PathVariable Long courseId, @PathVariable Long requestingAccountId)
    {
        try
        {
            Long toggledCourseId = this.courseService.toggleEnrollmentActiveStatus(courseId, requestingAccountId);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted course with Course ID: " + toggledCourseId);
        }
        catch (AccountPermissionDeniedException ex)
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ex.getMessage());
        }
        catch (AccountNotFoundException | CourseNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    private List<CourseWithTutorAndRatingResp> getAllCoursesWithTutorsByCourses(List<Course> courses) throws AccountNotFoundException, CourseNotFoundException
    {
        List<CourseWithTutorAndRatingResp> courseWithTutorAndRatingResps = new ArrayList<>();

        Long courseId;
        Account tutor;
        Double rating;
        CourseWithTutorAndRatingResp courseWithTutorAndRatingResp;

        for (Course course : courses)
        {
            courseId = course.getCourseId();
            tutor = this.accountService.getAccountByCourseId(courseId);
            rating = this.courseService.getCourseRating(courseId);

            courseWithTutorAndRatingResp = createCourseWithtutorResp(course, tutor, rating);
            courseWithTutorAndRatingResps.add(courseWithTutorAndRatingResp);
        }

        return courseWithTutorAndRatingResps;
    }

    private CourseWithTutorAndRatingResp createCourseWithtutorResp(Course course, Account tutor, Double rating)
    {
        CourseWithTutorAndRatingResp courseWithTutorAndRatingResp = new CourseWithTutorAndRatingResp(
                course.getCourseId(),
                course.getName(),
                course.getDescription(),
                course.getPrice(),
                course.getBannerUrl(),
                course.getEnrollment(),
                course.getCourseTags(),
                course.getLessons(),
                tutor,
                course.getBannerPictureFilename(),
                course.getIsEnrollmentActive(),
                rating
        );

        return courseWithTutorAndRatingResp;
    }
}
