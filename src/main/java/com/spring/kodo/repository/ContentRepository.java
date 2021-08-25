package com.spring.kodo.repository;

import com.spring.kodo.entity.Account;
import com.spring.kodo.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long>
{
    Optional<Content> findByName(String name);
}
