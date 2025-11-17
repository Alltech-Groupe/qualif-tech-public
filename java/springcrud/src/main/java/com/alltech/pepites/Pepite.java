package com.alltech.pepites;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Pepite implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String nom;

    private String prenom;

    private Instant dateNaissance;

}
