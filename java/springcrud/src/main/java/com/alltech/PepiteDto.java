package com.alltech;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PepiteDto {

    @Size(min = 3)
    protected String nom;

    @Size(min = 3)
    protected String prenom;

    @JsonDeserialize(using = InstantDateDeserializer.class)
    protected Instant dateNaissance;

}