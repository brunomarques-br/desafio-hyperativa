package com.av.apivisa.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "cards", indexes = {
        @Index(name = "idx_card_hash", columnList = "cardHash"),
        @Index(name = "idx_batch_number", columnList = "batchNumber"),
        @Index(name = "idx_created_at", columnList = "createdAt")
})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 500)
    private String encryptedCardNumber;

    @Column(nullable = false, unique = true, length = 64)
    private String cardHash; // SHA-256 hash for uniqueness check

    @Column(nullable = false, length = 8)
    private String batchNumber;

    @Column(length = 2)
    private String lineIdentifier; // C1, C2, C3, etc.

    @Column(length = 6)
    private String numberingInBatch; // Batch numbering like 000001, 000002, etc.

    @Column(nullable = false)
    private Integer lineNumber; // Line number in the file

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User createdBy;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Version
    private Long version;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Card card = (Card) o;
        return getId() != null && Objects.equals(getId(), card.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
