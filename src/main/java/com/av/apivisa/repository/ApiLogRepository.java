package com.av.apivisa.repository;

import com.av.apivisa.entity.ApiLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApiLogRepository extends JpaRepository<ApiLog, Long> {

    @Query("SELECT a FROM ApiLog a ORDER BY a.timestamp DESC")
    List<ApiLog> findAllLogsOrderByDate();

}