package com.av.apivisa.repository;

import com.av.apivisa.entity.BatchStatus;
import com.av.apivisa.entity.FileProcessingBatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FileProcessingBatchRepository extends JpaRepository<FileProcessingBatch, String> {

    Optional<FileProcessingBatch> findByBatchNumber(String batchNumber);

    boolean existsByBatchNumber(String batchNumber);

    List<FileProcessingBatch> findByStatus(BatchStatus status);

    Page<FileProcessingBatch> findByStatus(BatchStatus status, Pageable pageable);

    List<FileProcessingBatch> findByProcessedByEmail(String email);

    List<FileProcessingBatch> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT f FROM FileProcessingBatch f WHERE f.processedAt IS NULL AND f.createdAt < :threshold")
    List<FileProcessingBatch> findStalledBatches(@Param("threshold") LocalDateTime threshold);

    @Modifying
    @Query("UPDATE FileProcessingBatch f SET f.status = :status, f.processedAt = :processedAt WHERE f.id = :id")
    void updateBatchStatus(@Param("id") String id,
                           @Param("status") BatchStatus status,
                           @Param("processedAt") LocalDateTime processedAt);

    @Modifying
    @Query("UPDATE FileProcessingBatch f SET f.registrosProcessados = :processed, f.registrosComErro = :errors WHERE f.id = :id")
    void updateBatchCounts(@Param("id") String id,
                           @Param("processed") Integer processed,
                           @Param("errors") Integer errors);

    @Query("SELECT f.batchNumber, COUNT(c) FROM FileProcessingBatch f LEFT JOIN Card c ON c.batchNumber = f.batchNumber GROUP BY f.batchNumber")
    List<Object[]> countCardsPerBatch();

    @Query("SELECT f.status, COUNT(f) FROM FileProcessingBatch f GROUP BY f.status")
    List<Object[]> countBatchesByStatus();

    @Query(value = "SELECT * FROM file_processing_batches WHERE created_at >= :date ORDER BY created_at DESC LIMIT :limit",
            nativeQuery = true)
    List<FileProcessingBatch> findRecentBatches(@Param("date") LocalDateTime date,
                                                @Param("limit") int limit);

}