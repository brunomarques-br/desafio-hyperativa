package com.av.apivisa.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "api_logs", indexes = {
        @Index(name = "idx_log_timestamp", columnList = "timestamp"),
        @Index(name = "idx_log_endpoint", columnList = "endpoint"),
        @Index(name = "idx_log_user", columnList = "userId")
})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ApiLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String endpoint;

    @Column(nullable = false)
    private String httpMethod;

    @Column(nullable = false)
    private Integer statusCode;

    @Column(columnDefinition = "TEXT")
    private String requestBody;

    @Column(columnDefinition = "TEXT")
    private String responseBody;

    @Column(nullable = false)
    private String clientIp;

    @Column(nullable = false)
    private String userAgent;

    private Long processingTimeMs;

    private String userId;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime timestamp;

    @Column(length = 50)
    private String errorCode;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        ApiLog apiLog = (ApiLog) o;
        return getId() != null && Objects.equals(getId(), apiLog.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
