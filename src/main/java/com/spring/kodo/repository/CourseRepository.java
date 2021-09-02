package com.spring.kodo.repository;

import com.spring.kodo.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>
{
    Optional<Course> findByName(String name);

    //@Query(value="SELECT * FROM Course c WHERE c.name LIKE %:keyword%", nativeQuery = true)
    //@Query(value="select * from Users u where u.first_name like %:keyword% or u.last_name like %:keyword%", nativeQuery=true)
    //List<UserEntity> findUsersByKeyword(@Param("keyword") String keyword);
    @Query("SELECT c FROM Course c WHERE c.name LIKE %:keyword%")
    Optional<List<Course>> findCourseByKeyword(@Param("keyword") String keyword);
}
