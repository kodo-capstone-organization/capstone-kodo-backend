package com.spring.kodo.repository;

import com.spring.kodo.entity.Tag;
import com.spring.kodo.entity.rest.response.TagWithAccountsCountAndCoursesCount;
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
            "SELECT\n" +
                    "t.tag_id AS tagId,\n" +
                    "t.title AS title,\n" +
                    "(\n" +
                    "    SELECT COUNT(*)\n" +
                    "    FROM account_interests a\n" +
                    "    WHERE a.tag_id = t.tag_id\n" +
                    ") AS accountsCount,\n" +
                    "(\n" +
                    "    SELECT COUNT(*)\n" +
                    "    FROM course_course_tags cct\n" +
                    "    WHERE cct.tag_id = t.tag_id\n" +
                    ") AS coursesCount\n" +
                    "FROM tag t;", nativeQuery = true)
    List<TagWithAccountsCountAndCoursesCount> findAllTagsWithAccountsCountAndCoursesCount();

    @Query(value =
            "SELECT t.*\n" +
                    "FROM (\n" +
                    "         SELECT cct.tag_id\n" +
                    "         FROM account_enrolled_courses aec\n" +
                    "                  JOIN course_course_tags cct\n" +
                    "                       ON aec.enrolled_course_id = cct.course_id\n" +
                    "         WHERE aec.account_id = :accountId\n" +
                    "         UNION ALL\n" +
                    "         SELECT ai.tag_id\n" +
                    "         FROM account_interests ai\n" +
                    "         WHERE ai.account_id = :accountId\n" +
                    "     ) AS limitedTagsTable\n" +
                    "JOIN tag t\n" +
                    "    ON t.tag_id = limitedTagsTable.tag_id\n" +
                    "GROUP BY t.tag_id\n" +
                    "ORDER BY COUNT(t.tag_id) DESC\n" +
                    "LIMIT :limit",
            nativeQuery = true)
    List<Tag> findTopRelevantTagsThroughFrequencyWithLimitByAccountId(@Param("accountId") Long accountId, @Param("limit") Integer limit);
}
