package com.alltech.pepites;

import com.alltech.serializer.InstantDateDeserializer;
import com.alltech.serializer.JsonInstantDateSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected Long id;

    @Size(min = 3)
    @Schema(description = "Nom de la pépite", example = "Dupont", minLength = 3)
    protected String nom;

    @Size(min = 3)
    @Schema(description = "Prénom de la pépite", example = "Jean", minLength = 3)
    protected String prenom;


    @JsonSerialize(using = JsonInstantDateSerializer.class)
    @JsonDeserialize(using = InstantDateDeserializer.class)
    @Schema(description = "Date de naissance au format 'dd-MM-yyyy', exemple '21-04-1987'",
            type = "string", format = "date", example = "21-04-1987")
    protected Instant dateNaissance;

    public PepiteDto(String nom, String prenom, Instant dateNaissance) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
    }
}
