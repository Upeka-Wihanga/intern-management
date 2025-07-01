package com.internsystem.internmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "functions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Function {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long functionId;

    private String functionName;

    private String description;

    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;

    @ManyToOne
    @JoinColumn(name = "developer_intern_id")
    private Intern developerIntern;

    @Enumerated(EnumType.STRING)
    private FunctionStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
