package com.spring.kodo.repository;

import com.spring.kodo.entity.EnrolledCourse;
import com.spring.kodo.entity.EnrolledLesson;
import com.spring.kodo.restentity.response.EnrolledCourseWithStudentResp;
import com.spring.kodo.restentity.response.EnrolledCourseWithStudentCompletion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrolledCourseRepository extends JpaRepository<EnrolledCourse, Long>
{
    Optional<EnrolledCourse> findByCourseRating(int courseRating);

    @Query(value = "SELECT * FROM enrolled_course ec JOIN enrolled_course_enrolled_lessons ecel ON ec.enrolled_course_id = ecel.enrolled_course_id WHERE ecel.enrolled_lesson_id = :enrolledLessonId", nativeQuery = true)
    Optional<EnrolledCourse> findByEnrolledLessonId(@Param("enrolledLessonId") Long enrolledLessonId);

    @Query(value = "SELECT ec.* FROM enrolled_course ec JOIN enrolled_course_enrolled_lessons ecel JOIN enrolled_lesson_enrolled_contents elec ON ec.enrolled_course_id = ecel.enrolled_course_id AND ecel.enrolled_lesson_id = elec.enrolled_lesson_id WHERE elec.enrolled_content_id = :enrolledContentId", nativeQuery = true)
    Optional<EnrolledCourse> findByEnrolledContentId(@Param("enrolledContentId") Long enrolledContentId);

    @Query(value = "SELECT * FROM enrolled_course ec JOIN course c JOIN account_enrolled_courses aec ON ec.parent_course_course_id = c.course_id AND ec.enrolled_course_id = aec.enrolled_course_id WHERE c.name LIKE :courseName AND aec.account_id = :studentId", nativeQuery = true)
    Optional<EnrolledCourse> findByStudentIdAndCourseName(@Param("studentId") Long studentId, @Param("courseName") String courseName);

    @Query(value = "SELECT * FROM enrolled_course ec JOIN course c JOIN account_enrolled_courses aec ON ec.parent_course_course_id = c.course_id AND ec.enrolled_course_id = aec.enrolled_course_id WHERE c.course_id LIKE :courseId AND aec.account_id = :studentId", nativeQuery = true)
    Optional<EnrolledCourse> findByStudentIdAndCourseId(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

    @Query(value = "SELECT\n" +
            "    a.account_id AS 'accountId',\n" +
            "    a.username AS 'username',\n" +
            "    a.name AS 'name',\n" +
            "    a.bio AS 'bio',\n" +
            "    a.email AS 'email',\n" +
            "    a.display_picture_url AS 'displayPictureUrl',\n" +
            "    a.is_admin AS 'isAdmin',\n" +
            "    a.is_active AS 'isActive',\n" +
            "    a.stripe_account_id AS 'stripeAccountId',\n" +
            "    ec.enrolled_course_id AS 'enrolledCourseId',\n" +
            "    COUNT(ecc.date_time_of_completion) /\n" +
            "    (\n" +
            "        SELECT COUNT(*)\n" +
            "        FROM course_lessons cl\n" +
            "                 JOIN lesson_contents lc\n" +
            "                      ON cl.lesson_id = lc.lesson_id\n" +
            "        WHERE cl.course_id = :courseId\n" +
            "    ) AS 'completionPercentage'\n" +
            "FROM account a\n" +
            "         JOIN account_enrolled_courses aec\n" +
            "         JOIN enrolled_course ec\n" +
            "         JOIN enrolled_course_enrolled_lessons ecel\n" +
            "         JOIN enrolled_lesson_enrolled_contents elec\n" +
            "         JOIN enrolled_content ecc\n" +
            "              ON a.account_id = aec.account_id\n" +
            "                  AND aec.enrolled_course_id = ec.enrolled_course_id\n" +
            "                  AND ec.enrolled_course_id = ecel.enrolled_course_id\n" +
            "                  AND ecel.enrolled_lesson_id = elec.enrolled_lesson_id\n" +
            "                  AND elec.enrolled_content_id = ecc.enrolled_content_id\n" +
            "WHERE ec.parent_course_course_id = :courseId\n" +
            "GROUP BY ec.enrolled_course_id", nativeQuery = true)
    List<EnrolledCourseWithStudentResp> findAllEnrolledCourseCompletionPercentagesByCourseId(@Param("courseId") Long courseId);

    @Query(value = "SELECT \n" +
            "       a.account_id AS 'studentId', \n" +
            "       a.name AS 'studentName', \n" +
            "       a.username AS 'studentUsername', \n" +
            "       a.is_active AS 'studentActive', \n"+
            "       ec.date_time_of_completion AS 'completionDate' \n" +
            "FROM account a\n" +
            "    JOIN account_enrolled_courses aec\n" +
            "    JOIN enrolled_course ec\n" +
            "    JOIN enrolled_course_enrolled_lessons ecel\n" +
            "    JOIN enrolled_lesson_enrolled_contents elec\n" +
            "    JOIN enrolled_content ecc\n" +
            "        ON a.account_id = aec.account_id\n" +
            "        AND aec.enrolled_course_id = ec.enrolled_course_id\n" +
            "        AND ec.enrolled_course_id = ecel.enrolled_course_id\n" +
            "        AND ecel.enrolled_lesson_id = elec.enrolled_lesson_id\n" +
            "        AND elec.enrolled_content_id = ecc.enrolled_content_id\n" +
            "WHERE ec.parent_course_course_id = :courseId\n" +
            "GROUP BY ec.enrolled_course_id;", nativeQuery = true)
    List<EnrolledCourseWithStudentCompletion> findAllEnrolledCourseStudentsByCourseId(@Param("courseId") Long courseId);
}
