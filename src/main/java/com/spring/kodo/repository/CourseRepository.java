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

    @Query(value = "SELECT * FROM Course c JOIN Course_Lessons cl ON c.course_id = cl.course_id WHERE cl.lesson_id = :lessonId", nativeQuery = true)
    Optional<Course> findByLessonId(@Param("lessonId") Long lessonId);

    @Query("SELECT c FROM Course c WHERE c.isEnrollmentActive = TRUE")
    List<Course> findAllWithActiveEnrollment();

    @Query(value = "SELECT c.* FROM Tag t JOIN Course_Course_Tags cct JOIN Course c ON t.tag_id = cct.tag_id AND c.course_id = cct.course_id WHERE t.title = :tagTitle", nativeQuery = true)
    List<Course> findAllCoursesByTagTitle(@Param("tagTitle") String tagTitle);

    @Query(value = "SELECT c.* FROM Course c JOIN Course_Course_Tags cct ON c.course_id = cct.course_id WHERE cct.tag_id = :tagId", nativeQuery = true)
    List<Course> findAllCoursesByTagId(@Param("tagId") Long tagId);

    @Query("SELECT c FROM Course c WHERE c.name LIKE %:keyword%")
    List<Course> findAllCoursesByKeyword(@Param("keyword") String keyword);

    @Query("SELECT a.courses FROM Account a WHERE a.accountId = :tutorId")
    List<Course> findAllCoursesByTutorId(@Param("tutorId") Long tutorId);

    @Query(value =
            "SELECT interestsAndEnrolledCourses.*\n" +
                    "FROM\n" +
                    "(\n" +
                    " SELECT DISTINCT c.*\n" +
                    " FROM Course c\n" +
                    "    JOIN\n" +
                    "      (\n" +
                    "          SELECT limitedTagsTable.tag_id\n" +
                    "          FROM (\n" +
                    "                   SELECT cct.tag_id\n" +
                    "                   FROM Account_Enrolled_Courses aec\n" +
                    "                            JOIN Course_Course_Tags cct\n" +
                    "                                 ON aec.enrolled_course_id = cct.course_id\n" +
                    "                   WHERE aec.account_id = :accountId\n" +
                    "                   UNION ALL\n" +
                    "                   SELECT ai.tag_id\n" +
                    "                   FROM Account_Interests ai\n" +
                    "                   WHERE ai.account_id = :accountId\n" +
                    "               ) AS limitedTagsTable\n" +
                    "          GROUP BY limitedTagsTable.tag_id\n" +
                    "          ORDER BY COUNT(limitedTagsTable.tag_id) DESC\n" +
                    "          LIMIT :limit\n" +
                    "      ) AS limitedTagsTable\n" +
                    "      JOIN Course_Course_Tags cct\n" +
                    "          ON c.course_id = cct.course_id\n" +
                    "          AND cct.tag_id = limitedTagsTable.tag_id\n" +
                    ") AS interestsAndEnrolledCourses\n" +
                    "LEFT JOIN\n" +
                    "(\n" +
                    "    SELECT ec.parent_course_course_id AS 'course_id'\n" +
                    "    FROM Account_Enrolled_Courses aec\n" +
                    "             JOIN Enrolled_Course ec\n" +
                    "                  on aec.enrolled_course_id = ec.enrolled_course_id\n" +
                    "    WHERE aec.account_id = :accountId\n" +
                    ") AS enrolledCourses\n" +
                    "    ON interestsAndEnrolledCourses.course_id = enrolledCourses.course_id\n" +
                    "WHERE enrolledCourses.course_id IS NULL",
            nativeQuery = true)
    List<Course> findAllCoursesToRecommendWithLimitByAccountId(@Param("accountId") Long accountId, @Param("limit") Integer limit);

    @Query(value =
            "SELECT DISTINCT c.*\n" +
                    "FROM Course c\n" +
                    "    JOIN Course_Course_Tags cct\n" +
                    "        ON c.course_id = cct.course_id\n" +
                    "LEFT JOIN\n" +
                    "(\n" +
                    "    SELECT ec.parent_course_course_id AS 'course_id'\n" +
                    "    FROM Account_Enrolled_Courses aec\n" +
                    "             JOIN Enrolled_Course ec\n" +
                    "                  on aec.enrolled_course_id = ec.enrolled_course_id\n" +
                    "    WHERE aec.account_id = :accountId\n" +
                    ") AS enrolledCourses\n" +
                    "ON c.course_id = enrolledCourses.course_id\n" +
                    "WHERE enrolledCourses.course_id IS NULL\n" +
                    "AND cct.tag_id IN :tagIds",
            nativeQuery = true)
    List<Course> findAllCoursesToRecommendByAccountIdAndTagIds(@Param("accountId") Long accountId, @Param("tagIds") List<Long> tagIds);

    @Query(value = "SELECT * FROM Course c WHERE c.date_time_of_creation > NOW() - INTERVAL 14 DAY", nativeQuery = true)
    List<Course> findAllCoursesCreatedInTheLast14Days();

    // Popular courses are determined by courses that have
    // COUNT(enrolledCourse) >= FLOOR(MAX(enrolledCourse of All Courses) / 2)
    @Query(value =
            "SELECT c.course_id\n" +
            "FROM Course c\n" +
            "         JOIN Enrolled_course ec\n" +
            "              ON c.course_id = ec.parent_course_course_id\n" +
            "GROUP BY c.course_id\n" +
            "HAVING COUNT(ec.enrolled_course_id) >=\n" +
            "       (\n" +
            "           SELECT FLOOR(MAX(enrolled_course_count) / 2)\n" +
            "           FROM (\n" +
            "                    SELECT ec.parent_course_course_id,\n" +
            "                           COUNT(ec.enrolled_course_id) AS enrolled_course_count\n" +
            "                    FROM Enrolled_Course ec\n" +
            "                    GROUP BY ec.parent_course_course_id\n" +
            "                ) AS enrolled_course_count_results\n" +
            "       );",
            nativeQuery = true)
    List<Long> findAllCoursesThatArePopular();

    @Query(value = "SELECT AVG(ec.course_rating) FROM Course c JOIN Enrolled_Course ec WHERE c.course_id = :courseId", nativeQuery = true)
    Double findCourseRatingByCourseId(@Param("courseId") Long courseId);

    @Query(value = "SELECT SUM(t.course_price) FROM Transaction t WHERE t.course_course_id = :courseId", nativeQuery = true)
    BigDecimal findTotalEarningsByCourseId(@Param("courseId") Long courseId);

    @Query(value = "SELECT SUM(t.course_price) FROM Transaction t WHERE t.course_course_id = :courseId AND YEAR(t.date_time_of_transaction) BETWEEN :year AND YEAR(DATE_ADD(:year, INTERVAL 1 YEAR))", nativeQuery = true)
    BigDecimal findTotalEarningsByCourseIdAndYear(@Param("courseId") Long courseId, @Param("year") Integer year);
}
