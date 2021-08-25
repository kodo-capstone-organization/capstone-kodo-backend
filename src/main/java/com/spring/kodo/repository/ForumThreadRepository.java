package com.spring.kodo.repository;

import com.spring.kodo.entity.ForumThread;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumThreadRepository extends JpaRepository<ForumThread, Long>
{
}