package com.spring.kodo.repository;

import com.spring.kodo.entity.Multimedia;
import com.spring.kodo.util.enumeration.MultimediaType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MultimediaRepository extends JpaRepository<Multimedia, Long>
{
    Optional<Multimedia> findByName(String name);

    Optional<Multimedia> findByType(MultimediaType type);
}
