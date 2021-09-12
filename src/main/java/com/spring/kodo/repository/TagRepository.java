package com.spring.kodo.repository;

import com.spring.kodo.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long>
{
    Optional<Tag> findByTitle(String title);

    @Query(value =
            "SELECT t.*\n" +
            "FROM (\n" +
            "         SELECT cct.tag_id\n" +
            "         FROM Account_Enrolled_Courses aec\n" +
            "                  JOIN Course_Course_Tags cct\n" +
            "                       ON aec.enrolled_course_id = cct.course_id\n" +
            "         WHERE aec.account_id = :accountId\n" +
            "         UNION ALL\n" +
            "         SELECT ai.tag_id\n" +
            "         FROM Account_Interests ai\n" +
            "         WHERE ai.account_id = :accountId\n" +
            "     ) AS limitedTagsTable\n" +
            "JOIN Tag t\n" +
            "    ON t.tag_id = limitedTagsTable.tag_id\n" +
            "GROUP BY t.tag_id\n" +
            "ORDER BY COUNT(t.tag_id) DESC\n" +
            "LIMIT :limit",
            nativeQuery = true)
    List<Tag> findTopRelevantTagsThroughFrequencyWithLimitByAccountId(@Param("accountId") Long accountId, @Param("limit") Integer limit);
}
