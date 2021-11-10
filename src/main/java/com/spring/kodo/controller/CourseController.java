package com.spring.kodo.controller;

import com.spring.kodo.entity.Account;
import com.spring.kodo.entity.Course;
import com.spring.kodo.entity.Tag;
import com.spring.kodo.entity.rest.request.CreateNewCourseReq;
import com.spring.kodo.entity.rest.request.UpdateCourseReq;
import com.spring.kodo.entity.rest.response.CourseResp;
import com.spring.kodo.entity.rest.response.CourseBasicResp;
import com.spring.kodo.entity.rest.response.CourseWithTutorAndRatingResp;
import com.spring.kodo.entity.rest.response.RecommendedCoursesWithTagsResp;
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
    private EnrolledCourseService enrolledCourseService;

    @GetMapping("/getCourseByCourseId/{courseId}")
    public CourseWithTutorAndRatingResp getCourseByCourseId(@PathVariable Long courseId)
    {
        try
        {
            Course course = this.courseService.getCourseByCourseId(courseId);
            Account tutor = this.accountService.getAccountByCourseId(courseId);
            Double rating = this.courseService.getCourseRatingByCourseId(courseId);

            CourseWithTutorAndRatingResp courseWithTutorAndRatingResp = createCourseWithtutorResp(course, tutor, rating);

            return courseWithTutorAndRatingResp;
        }
        catch (CourseNotFoundException | AccountNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getCourseWithoutEnrollmentByCourseId/{courseId}")
    public CourseWithTutorAndRatingResp getCourseWithoutEnrollmentByCourseId(@PathVariable Long courseId)
    {
        try
        {
            Course course = this.courseService.getCourseByCourseId(courseId);
            Account tutor = this.accountService.getAccountByCourseId(courseId);
            Double rating = this.courseService.getCourseRatingByCourseId(courseId);

            // Explicitly set enrollment to null as it's not needed
            CourseWithTutorAndRatingResp courseWithTutorAndRatingResp = new CourseWithTutorAndRatingResp(
                    course.getCourseId(),
                    course.getName(),
                    course.getDescription(),
                    course.getPrice(),
                    course.getBannerUrl(),
                    course.getDateTimeOfCreation(),
                    null,
                    course.getCourseTags(),
                    course.getLessons(),
                    tutor,
                    course.getBannerPictureFilename(),
                    course.getIsEnrollmentActive(),
                    rating,
                    course.getIsReviewRequested()
            );

            return courseWithTutorAndRatingResp;
        }
        catch (CourseNotFoundException | AccountNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getCourseWithoutEnrollmentByCourseIdAndAccountId/{courseId}/{accountId}")
    public CourseWithTutorAndRatingResp getCourseWithoutEnrollmentByCourseIdAndAccountId(@PathVariable Long courseId, @PathVariable Long accountId)
    {
        try
        {
            Account tutor = this.accountService.getAccountByCourseId(courseId);

            if (tutor.getAccountId().equals(accountId))
            {
                Course course = this.courseService.getCourseByCourseId(courseId);
                Double rating = this.courseService.getCourseRatingByCourseId(courseId);

                // Explicitly set enrollment to null as it's not needed
                CourseWithTutorAndRatingResp courseWithTutorAndRatingResp = new CourseWithTutorAndRatingResp(
                        course.getCourseId(),
                        course.getName(),
                        course.getDescription(),
                        course.getPrice(),
                        course.getBannerUrl(),
                        course.getDateTimeOfCreation(),
                        null,
                        course.getCourseTags(),
                        course.getLessons(),
                        tutor,
                        course.getBannerPictureFilename(),
                        course.getIsEnrollmentActive(),
                        rating,
                        course.getIsReviewRequested()
                );

                return courseWithTutorAndRatingResp;
            }
            else
            {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have access to this course");
            }
        }
        catch (CourseNotFoundException | AccountNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getCourseByContentId/{contentId}")
    public Course getCourseByContentId(@PathVariable Long contentId)
    {
        try
        {
            return this.courseService.getCourseByContentId(contentId);
        }
        catch (CourseNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getCourseByEnrolledContentId/{enrolledContentId}")
    public Course getCourseByEnrolledContentId(@PathVariable Long enrolledContentId)
    {
        try
        {
            return this.courseService.getCourseByEnrolledContentId(enrolledContentId);
        }
        catch (CourseNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getCourseByStudentAttemptId/{studentAttemptId}")
    public Course getCourseByStudentAttemptId(@PathVariable Long studentAttemptId)
    {
        try
        {
            return this.courseService.getCourseByStudentAttemptId(studentAttemptId);
        }
        catch (CourseNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getCourseRatingByCourseId/{courseId}")
    public Double getCourseRatingByCourseId(@PathVariable Long courseId)
    {
        try
        {
            return this.courseService.getCourseRatingByCourseId(courseId);
        }
        catch (CourseNotFoundException ex)
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
    public List<CourseResp> getAllCourses()
    {
        try
        {
            List<Course> courses = this.courseService.getAllCourses();
            List<CourseResp> courseResps = getBasicCourses(courses);

            return courseResps;
        }
        catch (AccountNotFoundException | CourseNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getPendingCourses")
    public List<CourseResp> getPendingCourses()
    {
        try
        {
            List<Course> courses = this.courseService.getCoursesPendingRequest();
            List<CourseResp> courseResps = getBasicCourses(courses);

            return courseResps;
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
        catch (AccountNotFoundException | CourseNotFoundException ex)
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
    public RecommendedCoursesWithTagsResp getAllCoursesToRecommendWithLimitByAccountId(@PathVariable Long accountId, @PathVariable Integer limit)
    {
        try
        {
            List<Tag> topTags = this.tagService.getTopRelevantTagsThroughFrequencyWithLimitByAccountId(accountId, limit);
            List<Long> topTagIds = topTags.stream().map(tag -> tag.getTagId()).collect(Collectors.toList());

            List<Course> allCoursesToRecommend = this.courseService.getAllCoursesToRecommendByAccountIdAndTagIds(accountId, topTagIds);

            List<CourseResp> courseResps = getBasicCourses(allCoursesToRecommend);

            RecommendedCoursesWithTagsResp recommendedCoursesWithTagsResp = new RecommendedCoursesWithTagsResp(courseResps, topTags);

            return recommendedCoursesWithTagsResp;
        }
        catch (AccountNotFoundException | CourseNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getAllCoursesThatArePopular")
    public List<CourseResp> getAllCoursesThatArePopular()
    {
        try
        {
            List<Course> courses = this.courseService.getAllCoursesThatArePopular();
            List<CourseResp> coursesResps = getBasicCourses(courses);

            return coursesResps;
        }
        catch (CourseNotFoundException | AccountNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getNewReleasesCourses")
    public List<CourseResp> getNewReleasesCourses()
    {
        try
        {
            List<Course> courses = this.courseService.getAllCoursesInTheLast14Days();
            List<CourseResp> coursesResps = getBasicCourses(courses);

            return coursesResps;
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
                newCourse = this.courseService.createNewCourse(newCourse, createNewCourseReq.getTagTitles());

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
            @RequestPart(name = "updateCourseReq", required = true) UpdateCourseReq updateCourseReq,
            @RequestPart(name = "bannerPicture", required = false) MultipartFile updatedBannerPicture
    )
    {
        if (updateCourseReq != null)
        {
            logger.info("HIT course/updateCourse | PUT");
            try
            {
                // Update course
                Course updatedCourse = this.courseService.updateCourse(
                        updateCourseReq.getCourse(),
                        null,
                        null,
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
            catch (TagNotFoundException | EnrolledCourseNotFoundException | CourseNotFoundException | LessonNotFoundException ex)
            {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
            }
            catch (UnknownPersistenceException | InputDataValidationException | TagNameExistsException | FileUploadToGCSException | UpdateCourseException ex)
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
            return ResponseEntity.status(HttpStatus.OK).body("Successfully activated course enrollment for course with Course ID: " + toggledCourseId);
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
            rating = this.courseService.getCourseRatingByCourseId(courseId);

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
                course.getDateTimeOfCreation(),
                course.getEnrollment(),
                course.getCourseTags(),
                course.getLessons(),
                tutor,
                course.getBannerPictureFilename(),
                course.getIsEnrollmentActive(),
                rating,
                course.getIsReviewRequested()
        );

        return courseWithTutorAndRatingResp;
    }

    private List<CourseResp> getBasicCourses(List<Course> courses) throws AccountNotFoundException, CourseNotFoundException
    {
        List<CourseResp> courseResps = new ArrayList<>();

        Account tutor;
        Double rating;

        for (Course course : courses)
        {
            tutor = this.accountService.getAccountByCourseId(course.getCourseId());
            rating = this.courseService.getCourseRatingByCourseId(course.getCourseId());

            courseResps.add(new CourseResp(
                    course.getCourseId(),
                    course.getName(),
                    course.getDescription(),
                    course.getPrice(),
                    course.getBannerUrl(),
                    course.getDateTimeOfCreation(),
                    course.getCourseTags(),
                    tutor.getName(),
                    course.getBannerPictureFilename(),
                    course.getIsEnrollmentActive(),
                    rating,
                    course.getEnrollment().size(),
                    course.getIsReviewRequested()
            ));
        }

        return courseResps;
    }

    @GetMapping("/getBasicCourseByCourseId/{courseId}")
    public CourseBasicResp getBasicCourseByCourseId(@PathVariable Long courseId)
    {
        try
        {
            Course course = this.courseService.getCourseByCourseId(courseId);
            Account tutor = this.accountService.getAccountByCourseId(courseId);
            Double rating = this.courseService.getCourseRatingByCourseId(courseId);

            CourseWithTutorAndRatingResp courseWithTutorAndRatingResp = createCourseWithtutorResp(course, tutor, rating);
            CourseBasicResp courseResp = new CourseBasicResp();
            courseResp.setCourseId(courseWithTutorAndRatingResp.getCourseId());
            courseResp.setCourseName(courseWithTutorAndRatingResp.getName());
            courseResp.setTutorName(courseWithTutorAndRatingResp.getTutor().getName());
            return courseResp;
        }
        catch (CourseNotFoundException | AccountNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @DeleteMapping("/toggleReviewRequestStatus/{courseId}&{requestingAccountId}")
    public ResponseEntity toggleReviewRequestStatus(@PathVariable Long courseId, @PathVariable Long requestingAccountId)
    {
        try
        {
            Long toggledCourseId = this.courseService.toggleReviewRequestStatus(courseId, requestingAccountId);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully request for review for course with Course ID: " + toggledCourseId);
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

    @GetMapping("/isTutorByCourseIdAndAccountId/{courseId}/{accountId}")
    public Boolean isTutorByCourseIdAndAccountId(@PathVariable Long courseId, @PathVariable Long accountId)
    {
        try
        {
            courseService.getCourseByCourseId(courseId);

            Account account = accountService.getAccountByCourseId(courseId);

            if (account.getAccountId().equals(accountId))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (AccountNotFoundException | CourseNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/isStudentByCourseIdAndAccountId/{courseId}/{accountId}")
    public Boolean isStudentByCourseIdAndAccountId(@PathVariable Long courseId, @PathVariable Long accountId)
    {
        try
        {
            courseService.getCourseByCourseId(courseId);

            enrolledCourseService.getEnrolledCourseByStudentIdAndCourseId(accountId, courseId);
            return true;
        }
        catch (EnrolledCourseNotFoundException ex)
        {
            return false;
        }
        catch (CourseNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }
}
