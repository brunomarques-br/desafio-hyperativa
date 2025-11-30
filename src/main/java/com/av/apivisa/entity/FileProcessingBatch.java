package com.av.apivisa.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "file_processing_batches")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class FileProcessingBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true, length = 45)
    private String batchNumber; // LOTE0001000010

    @Column(nullable = false, length = 29)
    private String nome; // DESAFIO-HYPERATIVA

    @Column(nullable = false, length = 8)
    private String data; // 20180524

    @Column(nullable = false)
    private Integer quantidadeRegistros;

    @Column(nullable = false)
    private Integer registrosProcessados = 0;

    @Column(nullable = false)
    private Integer registrosComErro = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BatchStatus status = BatchStatus.PROCESSING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User processedBy;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime processedAt;

    @Column(columnDefinition = "TEXT")
    private String errorDetails;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        FileProcessingBatch that = (FileProcessingBatch) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
