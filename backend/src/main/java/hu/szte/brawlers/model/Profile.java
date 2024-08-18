package hu.szte.brawlers.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    @Column(unique = true)
    private String userName;
    private String email;
    private String password;
    @CreationTimestamp
    private LocalDateTime created;
    @Enumerated(EnumType.STRING)
    private Role roles;
}
