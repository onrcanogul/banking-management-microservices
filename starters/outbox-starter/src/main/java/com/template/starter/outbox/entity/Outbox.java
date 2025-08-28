package com.template.starter.outbox.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox", indexes = {
    @Index(name = "IX_IS_PUBLISHED", columnList = "IS_PUBLISHED")
})
@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Outbox {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String aggregateType;
    private String aggregateId;

    private String type;
    @Basic(fetch = FetchType.EAGER)
    @Column(columnDefinition = "TEXT")
    private String payload;

    private String destination;
    private boolean published = false;
    private Instant createdAt = Instant.now();
}
