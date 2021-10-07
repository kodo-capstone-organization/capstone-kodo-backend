package com.spring.kodo.repository;

import com.spring.kodo.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>
{
    Optional<Course> findByName(String name);

    @Query(value = "SELECT * FROM course c JOIN course_lessons cl ON c.course_id = cl.course_id WHERE cl.lesson_id = :lessonId", nativeQuery = true)
    Optional<Course> findByLessonId(@Param("lessonId") Long lessonId);

    @Query(value = "SELECT c.* FROM course c\n" +
            "    JOIN course_lessons cl\n" +
            "    JOIN lesson_contents lc\n" +
            "    JOIN enrolled_content ec\n" +
            "        ON c.course_id = cl.course_id\n" +
            "               AND cl.lesson_id = lc.lesson_id\n" +
            "               AND lc.content_id = ec.parent_content_content_id\n" +
            "WHERE ec.enrolled_content_id = :enrolledContentId", nativeQuery = true)
    Optional<Course> findByEnrolledContentId(@Param("enrolledContentId") Long enrolledContentId);

    @Query(value = "SELECT c.* FROM course c JOIN course_lessons cl JOIN lesson_contents lc ON c.course_id = cl.course_id AND cl.lesson_id = lc.lesson_id WHERE lc.content_id = :contentId", nativeQuery = true)
    Optional<Course> findByContentId(@Param("contentId") Long contentId);

    @Query(value = "SELECT c.* FROM course c\n" +
            "JOIN enrolled_course ec\n" +
            "    JOIN enrolled_course_enrolled_lessons ecel\n" +
            "    JOIN enrolled_lesson_enrolled_contents elec\n" +
            "    JOIN enrolled_content_student_attempts ecsa\n" +
            "ON c.course_id = ec.parent_course_course_id\n" +
            "    AND ec.enrolled_course_id = ecel.enrolled_course_id\n" +
            "    AND ecel.enrolled_lesson_id = elec.enrolled_lesson_id\n" +
            "    AND elec.enrolled_content_id = ecsa.enrolled_content_id\n" +
            "WHERE ecsa.student_attempt_id = :studentAttemptId", nativeQuery = true)
    Optional<Course> findByStudentAttemptId(@Param("studentAttemptId") Long studentAttemptId);

    @Query("SELECT c FROM course c WHERE c.isEnrollmentActive = TRUE")
    List<Course> findAllWithActiveEnrollment();

    @Query(value = "SELECT c.* FROM tag t JOIN course_course_tags cct JOIN course c ON t.tag_id = cct.tag_id AND c.course_id = cct.course_id WHERE t.title = :tagTitle", nativeQuery = true)
    List<Course> findAllCoursesByTagTitle(@Param("tagTitle") String tagTitle);

    @Query(value = "SELECT c.* FROM course c JOIN course_course_tags cct ON c.course_id = cct.course_id WHERE cct.tag_id = :tagId", nativeQuery = true)
    List<Course> findAllCoursesByTagId(@Param("tagId") Long tagId);

    @Query("SELECT c FROM course c WHERE c.name LIKE %:keyword%")
    List<Course> findAllCoursesByKeyword(@Param("keyword") String keyword);

    @Query("SELECT a.courses FROM account a WHERE a.accountId = :tutorId")
    List<Course> findAllCoursesByTutorId(@Param("tutorId") Long tutorId);

    @Query(value =
            "SELECT interestsAndEnrolledCourses.*\n" +
                    "FROM\n" +
                    "(\n" +
                    " SELECT DISTINCT c.*\n" +
                    " FROM course c\n" +
                    "    JOIN\n" +
                    "      (\n" +
                    "          SELECT limitedTagsTable.tag_id\n" +
                    "          FROM (\n" +
                    "                   SELECT cct.tag_id\n" +
                    "                   FROM account_enrolled_courses aec\n" +
                    "                            JOIN course_course_tags cct\n" +
                    "                                 ON aec.enrolled_course_id = cct.course_id\n" +
                    "                   WHERE aec.account_id = :accountId\n" +
                    "                   UNION ALL\n" +
                    "                   SELECT ai.tag_id\n" +
                    "                   FROM account_interests ai\n" +
                    "                   WHERE ai.account_id = :accountId\n" +
                    "               ) AS limitedTagsTable\n" +
                    "          GROUP BY limitedTagsTable.tag_id\n" +
                    "          ORDER BY COUNT(limitedTagsTable.tag_id) DESC\n" +
                    "          LIMIT :limit\n" +
                    "      ) AS limitedTagsTable\n" +
                    "      JOIN course_course_tags cct\n" +
                    "          ON c.course_id = cct.course_id\n" +
                    "          AND cct.tag_id = limitedTagsTable.tag_id\n" +
                    ") AS interestsAndEnrolledCourses\n" +
                    "LEFT JOIN\n" +
                    "(\n" +
                    "    SELECT ec.parent_course_course_id AS 'course_id'\n" +
                    "    FROM account_enrolled_courses aec\n" +
                    "             JOIN enrolled_course ec\n" +
                    "                  on aec.enrolled_course_id = ec.enrolled_course_id\n" +
                    "    WHERE aec.account_id = :accountId\n" +
                    ") AS enrolledCourses\n" +
                    "    ON interestsAndEnrolledCourses.course_id = enrolledCourses.course_id\n" +
                    "WHERE enrolledCourses.course_id IS NULL",
            nativeQuery = true)
    List<Course> findAllCoursesToRecommendWithLimitByAccountId(@Param("accountId") Long accountId, @Param("limit") Integer limit);

    @Query(value =
            "SELECT DISTINCT c.*\n" +
                    "FROM course c\n" +
                    "    JOIN course_course_tags cct\n" +
                    "        ON c.course_id = cct.course_id\n" +
                    "LEFT JOIN\n" +
                    "(\n" +
                    "    SELECT ec.parent_course_course_id AS 'course_id'\n" +
                    "    FROM account_enrolled_courses aec\n" +
                    "             JOIN enrolled_course ec\n" +
                    "                  on aec.enrolled_course_id = ec.enrolled_course_id\n" +
                    "    WHERE aec.account_id = :accountId\n" +
                    ") AS enrolledCourses\n" +
                    "ON c.course_id = enrolledCourses.course_id\n" +
                    "WHERE enrolledCourses.course_id IS NULL\n" +
                    "AND cct.tag_id IN :tagIds",
            nativeQuery = true)
    List<Course> findAllCoursesToRecommendByAccountIdAndTagIds(@Param("accountId") Long accountId, @Param("tagIds") List<Long> tagIds);

    @Query(value = "SELECT * FROM course c WHERE c.date_time_of_creation > NOW() - INTERVAL 14 DAY", nativeQuery = true)
    List<Course> findAllCoursesCreatedInTheLast14Days();

    // Popular courses are determined by courses that have
    // COUNT(enrolledCourse) >= FLOOR(MAX(enrolledCourse of All Courses) / 2)
    @Query(value =
            "SELECT c.*\n" +
            "FROM (\n" +
            "         SELECT ec.parent_course_course_id                                    AS course_id,\n" +
            "                PERCENT_RANK() over ( ORDER BY COUNT(ec.enrolled_course_id) ) AS percentile\n" +
            "         FROM enrolled_course ec\n" +
            "         GROUP BY ec.parent_course_course_id\n" +
            "         ORDER BY percentile DESC\n" +
            "     ) AS course_enrollment_percentile\n" +
            "JOIN course c\n" +
            "    ON c.course_id = course_enrollment_percentile.course_id\n" +
            "WHERE course_enrollment_percentile.percentile >= 0.75\n" +
            "ORDER BY c.name",
            nativeQuery = true)
    List<Course> findAllCoursesThatArePopular();

    @Query(value = "SELECT COALESCE(AVG(ec.course_rating), 0) FROM enrolled_course ec WHERE ec.parent_course_course_id = :courseId", nativeQuery = true)
    Double findCourseRatingByCourseId(@Param("courseId") Long courseId);

    @Query(value = "SELECT SUM(t.course_price) FROM `transaction` t WHERE t.course_course_id = :courseId", nativeQuery = true)
    BigDecimal findTotalEarningsByCourseId(@Param("courseId") Long courseId);

    @Query(value = "SELECT SUM(t.course_price) FROM `transaction` t WHERE t.course_course_id = :courseId AND YEAR(t.date_time_of_transaction) BETWEEN :year AND YEAR(DATE_ADD(:year, INTERVAL 1 YEAR))", nativeQuery = true)
    BigDecimal findTotalEarningsByCourseIdAndYear(@Param("courseId") Long courseId, @Param("year") Integer year);

    @Query(value = "SELECT COUNT(*) FROM course c WHERE YEAR(c.date_time_of_creation) = YEAR(NOW()) AND MONTH(c.date_time_of_creation) = MONTH(NOW())", nativeQuery = true)
    Integer findNumberOfNewCoursesForCurrentMonth();

    @Query(value = "SELECT COUNT(*) FROM course c WHERE YEAR(c.date_time_of_creation) = YEAR(CURRENT_DATE - INTERVAL 1 MONTH) AND MONTH(c.date_time_of_creation) = MONTH(CURRENT_DATE - INTERVAL 1 MONTH)", nativeQuery = true)
    Integer findNumberOfNewCoursesForPreviousMonth();
}
