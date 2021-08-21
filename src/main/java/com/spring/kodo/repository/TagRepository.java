package com.spring.kodo.repository;

import com.spring.kodo.entity.Account;
import com.spring.kodo.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long>
{
}
