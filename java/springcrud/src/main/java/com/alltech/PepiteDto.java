package com.alltech;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "PepiteDto", description = "DTO représentant une pépite avec nom, prénom et date de naissance")
public class PepiteDto {

    @Size(min = 3)
    @Schema(description = "Nom de la pépite", example = "Dupont", minLength = 3)
    protected String nom;

    @Size(min = 3)
    @Schema(description = "Prénom de la pépite", example = "Jean", minLength = 3)
    protected String prenom;

    @JsonDeserialize(using = InstantDateDeserializer.class)
    @Schema(description = "Date de naissance au format 'dd-MM-yyyy', exemple '21-04-1987'",
            type = "string", format = "date", example = "21-04-1987")
    protected Instant dateNaissance;

}
