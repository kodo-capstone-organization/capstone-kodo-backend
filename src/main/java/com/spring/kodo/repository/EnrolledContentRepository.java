package com.spring.kodo.repository;

import com.spring.kodo.entity.EnrolledContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnrolledContentRepository extends JpaRepository<EnrolledContent, Long>
{
    @Query(value = "SELECT * FROM account_enrolled_courses aec JOIN enrolled_course_enrolled_lessons ecel JOIN enrolled_lesson_enrolled_contents elec JOIN enrolled_content ec ON aec.enrolled_course_id = ecel.enrolled_course_id AND ecel.enrolled_lesson_id = elec.enrolled_lesson_id AND elec.enrolled_content_id = ec.enrolled_content_id WHERE aec.account_id = :accountId AND parent_content_content_id = :contentId", nativeQuery = true)
    Optional<EnrolledContent> findByAccountIdAndContentId(@Param("accountId") Long accountId, @Param("contentId") Long contentId);
}
