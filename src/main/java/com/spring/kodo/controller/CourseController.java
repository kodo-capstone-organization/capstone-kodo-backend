package com.spring.kodo.controller;

import com.spring.kodo.entity.Account;
import com.spring.kodo.entity.Course;
import com.spring.kodo.restentity.request.CreateNewCourseReq;
import com.spring.kodo.restentity.response.CourseWithTutorResp;
import com.spring.kodo.service.inter.AccountService;
import com.spring.kodo.service.inter.CourseService;
import com.spring.kodo.service.inter.FileService;
import com.spring.kodo.util.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/course")
public class CourseController {
    Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private FileService fileService;

    @GetMapping("/getCourseByCourseId/{courseId}")
    public CourseWithTutorResp getCourseByCourseId(@PathVariable Long courseId) {
        try {
            Course course = this.courseService.getCourseByCourseId(courseId);
            Account tutor = this.accountService.getAccountByCourseId(courseId);

            CourseWithTutorResp courseWithTutorResp = createCourseWithtutorResp(course, tutor);

            return courseWithTutorResp;
        } catch (CourseNotFoundException | AccountNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getAllCourses")
    public List<CourseWithTutorResp> getAllCourses() {
        try {
            List<Course> courses = this.courseService.getAllCourses();
            List<CourseWithTutorResp> courseWithTutorResps = getAllCoursesWithTutorsByCourses(courses);

            return courseWithTutorResps;
        } catch (AccountNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getAllCoursesByTagTitle/{tagTitle}")
    public List<CourseWithTutorResp> getAllCoursesByTagTitle(@PathVariable String tagTitle) {
        try {
            List<Course> courses = this.courseService.getAllCoursesByTagTitle(tagTitle);
            List<CourseWithTutorResp> courseWithTutorResps = getAllCoursesWithTutorsByCourses(courses);

            return courseWithTutorResps;
        } catch (TagNotFoundException | AccountNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getAllCoursesByKeyword/{keyword}")
    public List<CourseWithTutorResp> getAllCoursesByKeyword(@PathVariable String keyword) {
        try {
            List<Course> courses = this.courseService.getAllCoursesByKeyword(keyword);
            List<CourseWithTutorResp> courseWithTutorResps = getAllCoursesWithTutorsByCourses(courses);

            return courseWithTutorResps;
        } catch (CourseWithKeywordNotFoundException | AccountNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }


    @GetMapping("/getAllCoursesToRecommend/{accountId}")
    public List<Course> getAllCoursesToRecommend(@PathVariable Long accountId) {
        try {
            List<Course> allCoursesToRecommend = this.courseService.getAllCoursesToRecommend(accountId);
            return allCoursesToRecommend;
        } catch (AccountNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, ex.getMessage());
        }
    }

    @PostMapping("/createNewCourse")
    public Course createNewCourse(
            @RequestPart(name = "course", required = true) CreateNewCourseReq createNewCourseReq,
            @RequestPart(name = "bannerPicture", required = false) MultipartFile bannerPicture
    ) {
        if (createNewCourseReq != null) {
            logger.info("HIT account/createNewCourse | POST | Received : " + createNewCourseReq);
            try {
                Course newCourse = new Course(createNewCourseReq.getName(), createNewCourseReq.getDescription(), createNewCourseReq.getPrice(), "");
                newCourse = this.courseService.createNewCourse(newCourse, createNewCourseReq.getTutorId(), createNewCourseReq.getTagTitles());

                // TODO: courseService.updateCourse
//                if (bannerPicture != null)
//                {
//                    String bannerPictureURL = fileService.upload(bannerPicture);
//                    newCourse.setBannerUrl(bannerPictureURL);
//                    newCourse = this.courseService.updateCourse(newCourse, ...null for other relationfields);
//                }

                // Important! Adding course to tutor account
                Account tutor = this.accountService.getAccountByAccountId(createNewCourseReq.getTutorId());
                this.accountService.addCourseToAccount(tutor, newCourse);

                return newCourse;
            } catch (InputDataValidationException | TagNameExistsException | CreateNewCourseException | UpdateCourseException | UpdateAccountException ex) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
            } catch (TagNotFoundException | AccountNotFoundException | CourseNotFoundException ex) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
            } catch (UnknownPersistenceException ex) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Create New Course Request");
        }
    }

    private List<CourseWithTutorResp> getAllCoursesWithTutorsByCourses(List<Course> courses) throws AccountNotFoundException {
        List<CourseWithTutorResp> courseWithTutorResps = new ArrayList<>();

        Long courseId;
        Account tutor;
        CourseWithTutorResp courseWithTutorResp;

        for (Course course : courses) {
            courseId = course.getCourseId();
            tutor = this.accountService.getAccountByCourseId(courseId);

            courseWithTutorResp = createCourseWithtutorResp(course, tutor);
            courseWithTutorResps.add(courseWithTutorResp);
        }

        return courseWithTutorResps;
    }

    private CourseWithTutorResp createCourseWithtutorResp(Course course, Account tutor) {
        CourseWithTutorResp courseWithTutorResp = new CourseWithTutorResp(
                course.getCourseId(),
                course.getName(),
                course.getDescription(),
                course.getPrice(),
                course.getBannerUrl(),
                course.getEnrollment(),
                course.getCourseTags(),
                course.getLessons(),
                tutor
        );

        return courseWithTutorResp;
    }
}
