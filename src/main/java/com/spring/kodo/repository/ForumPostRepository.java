package com.spring.kodo.repository;

import com.spring.kodo.entity.ForumCategory;
import com.spring.kodo.entity.ForumPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForumPostRepository extends JpaRepository<ForumPost, Long>
{
}
