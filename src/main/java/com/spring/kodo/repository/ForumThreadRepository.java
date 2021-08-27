package com.spring.kodo.repository;

import com.spring.kodo.entity.ForumThread;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ForumThreadRepository extends JpaRepository<ForumThread, Long>
{
    Optional<ForumThread> findByName(String name);
}