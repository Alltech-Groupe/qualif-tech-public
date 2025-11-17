package com.alltech;

import com.alltech.pepites.Pepite;
import com.alltech.pepites.PepiteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PepiteController.class)
public class PepiteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PepiteService pepiteService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createPepite_ReturnsCreatedPepite() throws Exception {
        String jsonContent = """
        {
          "nom": "Jean",
          "prenom": "Dupont",
          "dateNaissance": "21-04-1987"
        }
        """;

        Pepite savedPepite = Pepite.builder()
                .nom("Jean")
                .prenom("Dupont")
                .dateNaissance(Instant.parse("1987-04-21T00:00:00Z"))
                .build();

        Mockito.when(pepiteService.savePepite(any(PepiteDto.class))).thenReturn(Optional.of(savedPepite));

        mockMvc.perform(post("/pepites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Jean"))
                .andExpect(jsonPath("$.prenom").value("Dupont"));
    }


    @Test
    void getAllPepites_ReturnsList() throws Exception {
        Pepite pepite1 = new Pepite();
        pepite1.setNom("Jean");
        Pepite pepite2 = new Pepite();
        pepite2.setNom("Marie");

        List<Pepite> pepites = Arrays.asList(pepite1, pepite2);
        Mockito.when(pepiteService.getAllPepites()).thenReturn(pepites);

        mockMvc.perform(get("/pepites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(pepites.size()))
                .andExpect(jsonPath("$[0].nom").value("Jean"))
                .andExpect(jsonPath("$[1].nom").value("Marie"));
    }

    @Test
    void updatePepite_ReturnsUpdated() throws Exception {
        Long id = 1L;
        String jsonContent = """
        {
          "nom": "Updated",
          "prenom": "Nom",
          "dateNaissance": "17-11-1988"
        }
        """;

        Pepite update = Pepite.builder()
                .nom("Updated")
                .prenom("Nom")
                .dateNaissance(Instant.parse("1988-11-17T00:00:00Z"))
                .build();

        Mockito.when(pepiteService.updatePepite(any(PepiteDto.class), eq(id))).thenReturn(Optional.of(update));

        mockMvc.perform(put("/pepites/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Updated"));
    }

    @Test
    void updatePepite_NotFound_ReturnsNotFound() throws Exception {
        Long id = 99L;
        Pepite update = new Pepite();

        Mockito.when(pepiteService.updatePepite(any(PepiteDto.class), eq(id))).thenReturn(Optional.empty());

        mockMvc.perform(put("/pepites/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletePepiteById_Success() throws Exception {
        Long id = 5L;

        Mockito.doNothing().when(pepiteService).deleteById(id);

        mockMvc.perform(delete("/pepites/{id}", id))
                .andExpect(status().isOk());
    }
}
