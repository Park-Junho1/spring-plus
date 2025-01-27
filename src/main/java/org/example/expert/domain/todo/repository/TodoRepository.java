package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long>, TodoRepositoryCustom {

    // 모든 연관 데이터를 미리 로드하도록 FETCH JOIN 추가
    @Query("SELECT t FROM Todo t " +
        "LEFT JOIN FETCH t.user u " +
        "LEFT JOIN FETCH t.comments c " +
        "LEFT JOIN FETCH t.managers m " +
        "ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);

    // ID로 조회할 때 모든 연관 데이터 로드
    @Query("SELECT t FROM Todo t " +
        "LEFT JOIN FETCH t.user u " +
        "LEFT JOIN FETCH t.comments c " +
        "LEFT JOIN FETCH t.managers m " +
        "WHERE t.id = :todoId")
    Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);

    // 조건 검색 시 필요한 연관 데이터 로드
    @Query("SELECT DISTINCT t FROM Todo t " +
        "LEFT JOIN FETCH t.user u " +
        "LEFT JOIN FETCH t.comments c " +
        "LEFT JOIN FETCH t.managers m " +
        "WHERE (:weather IS NULL OR t.weather = :weather) " +
        "AND (:startDate IS NULL OR t.modifiedAt >= :startDate) " +
        "AND (:endDate IS NULL OR t.modifiedAt <= :endDate) " +
        "ORDER BY t.modifiedAt DESC")
    Page<Todo> findTodosByFilters(
        @Param("weather") String weather,
        @Param("startDate") String startDate,
        @Param("endDate") String endDate,
        Pageable pageable);

    // 엔티티 그래프 활용
    @EntityGraph(attributePaths = {"user", "comments", "managers"})
    Optional<Todo> findById(Long todoId);
}
