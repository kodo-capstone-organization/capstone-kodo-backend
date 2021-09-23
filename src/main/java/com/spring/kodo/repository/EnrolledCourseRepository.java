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

    @Query(value = "SELECT * FROM Enrolled_Course ec JOIN Enrolled_Course_Enrolled_Lessons ecel ON ec.enrolled_course_id = ecel.enrolled_course_id WHERE ecel.enrolled_lesson_id = :enrolledLessonId", nativeQuery = true)
    Optional<EnrolledCourse> findByEnrolledLessonId(@Param("enrolledLessonId") Long enrolledLessonId);

    @Query(value = "SELECT * FROM Enrolled_Course ec JOIN Course c JOIN Account_Enrolled_Courses aec ON ec.parent_course_course_id = c.course_id AND ec.enrolled_course_id = aec.enrolled_course_id WHERE c.name LIKE :courseName AND aec.account_id = :studentId", nativeQuery = true)
    Optional<EnrolledCourse> findByStudentIdAndCourseName(@Param("studentId") Long studentId, @Param("courseName") String courseName);

    @Query(value = "SELECT * FROM Enrolled_Course ec JOIN Course c JOIN Account_Enrolled_Courses aec ON ec.parent_course_course_id = c.course_id AND ec.enrolled_course_id = aec.enrolled_course_id WHERE c.course_id LIKE :courseId AND aec.account_id = :studentId", nativeQuery = true)
    Optional<EnrolledCourse> findByStudentIdAndCourseId(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

    @Query(value = "SELECT \n" +
            "       a.account_id AS 'studentId', \n" +
            "       a.name AS 'studentName', \n" +
            "       ec.enrolled_course_id AS 'enrolledCourseId', \n" +
            "       COUNT(ecc.date_time_of_completion) /\n" +
            "(\n" +
            "    SELECT COUNT(*)\n" +
            "    FROM Course_Lessons cl\n" +
            "             JOIN Lesson_Contents lc\n" +
            "                  ON cl.lesson_id = lc.lesson_id\n" +
            "    WHERE cl.course_id = :courseId\n" +
            ") AS 'completionPercentage'\n" +
            "FROM Account a\n" +
            "    JOIN Account_Enrolled_Courses aec\n" +
            "    JOIN Enrolled_Course ec\n" +
            "    JOIN Enrolled_Course_Enrolled_Lessons ecel\n" +
            "    JOIN Enrolled_Lesson_Enrolled_Contents elec\n" +
            "    JOIN Enrolled_Content ecc\n" +
            "        ON a.account_id = aec.account_id\n" +
            "        AND aec.enrolled_course_id = ec.enrolled_course_id\n" +
            "        AND ec.enrolled_course_id = ecel.enrolled_course_id\n" +
            "        AND ecel.enrolled_lesson_id = elec.enrolled_lesson_id\n" +
            "        AND elec.enrolled_content_id = ecc.enrolled_content_id\n" +
            "WHERE ec.parent_course_course_id = :courseId\n" +
            "GROUP BY ec.enrolled_course_id;", nativeQuery = true)
    List<EnrolledCourseWithStudentResp> findAllEnrolledCourseCompletionPercentagesByCourseId(@Param("courseId") Long courseId);

    @Query(value = "SELECT \n" +
            "       a.account_id AS 'studentId', \n" +
            "       a.name AS 'studentName', \n" +
            "       a.username AS 'studentUsername', \n" +
            "       a.is_active AS 'studentActive', \n"+
            "       ec.date_time_of_completion AS 'completionDate' \n" +
            "FROM Account a\n" +
            "    JOIN Account_Enrolled_Courses aec\n" +
            "    JOIN Enrolled_Course ec\n" +
            "    JOIN Enrolled_Course_Enrolled_Lessons ecel\n" +
            "    JOIN Enrolled_Lesson_Enrolled_Contents elec\n" +
            "    JOIN Enrolled_Content ecc\n" +
            "        ON a.account_id = aec.account_id\n" +
            "        AND aec.enrolled_course_id = ec.enrolled_course_id\n" +
            "        AND ec.enrolled_course_id = ecel.enrolled_course_id\n" +
            "        AND ecel.enrolled_lesson_id = elec.enrolled_lesson_id\n" +
            "        AND elec.enrolled_content_id = ecc.enrolled_content_id\n" +
            "WHERE ec.parent_course_course_id = :courseId\n" +
            "GROUP BY ec.enrolled_course_id;", nativeQuery = true)
    List<EnrolledCourseWithStudentCompletion> findAllEnrolledCourseStudentsByCourseId(@Param("courseId") Long courseId);
}
