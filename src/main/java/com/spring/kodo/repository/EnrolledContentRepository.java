package com.spring.kodo.repository;

import com.spring.kodo.entity.EnrolledContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrolledContentRepository extends JpaRepository<EnrolledContent, Long>
{
}
