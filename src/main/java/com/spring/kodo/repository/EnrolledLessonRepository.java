package com.spring.kodo.repository;

import com.spring.kodo.entity.EnrolledLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrolledLessonRepository extends JpaRepository<EnrolledLesson, Long>
{
}
