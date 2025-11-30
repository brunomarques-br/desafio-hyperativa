package com.av.apivisa.repository;

import com.av.apivisa.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, String> {

    Optional<Card> findByCardHash(String cardHash);

    boolean existsByCardHash(String cardHash);

    List<Card> findByBatchNumber(String batchNumber);

    Page<Card> findByBatchNumber(String batchNumber, Pageable pageable);

    @Query("SELECT c FROM Card c WHERE c.createdAt BETWEEN :startDate AND :endDate")
    List<Card> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(c) FROM Card c WHERE c.batchNumber = :batchNumber")
    long countByBatchNumber(@Param("batchNumber") String batchNumber);

    @Query("SELECT c.batchNumber, COUNT(c) FROM Card c GROUP BY c.batchNumber")
    List<Object[]> countCardsByBatch();

    @Query("SELECT COUNT(c) > 0 FROM Card c WHERE c.cardHash = :cardHash")
    boolean existsByCardHashCustom(@Param("cardHash") String cardHash);

    @Query("SELECT c FROM Card c WHERE c.createdBy.id = :userId")
    Page<Card> findByUserId(@Param("userId") String userId, Pageable pageable);

    @Query(value = "SELECT * FROM cards WHERE batch_number = :batchNumber ORDER BY line_number LIMIT :limit OFFSET :offset",
            nativeQuery = true)
    List<Card> findCardsByBatchWithPagination(@Param("batchNumber") String batchNumber,
                                              @Param("limit") int limit,
                                              @Param("offset") int offset);
}
