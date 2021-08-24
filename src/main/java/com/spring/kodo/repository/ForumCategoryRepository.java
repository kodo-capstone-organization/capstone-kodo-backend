package com.spring.kodo.repository;

import com.spring.kodo.entity.ForumCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForumCategoryRepository extends JpaRepository<ForumCategory, Long>
{
    Optional<ForumCategory> findByName(String name);
}
