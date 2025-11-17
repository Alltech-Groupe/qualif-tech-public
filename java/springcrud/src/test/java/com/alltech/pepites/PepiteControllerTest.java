package com.alltech.pepites;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
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

    @MockitoBean
    private PepiteService pepiteService;

    @Test
    void createPepite_ReturnsCreatedPepiteDto() throws Exception {
        String jsonContent = """
        {
          "nom": "Jean",
          "prenom": "Dupont",
          "dateNaissance": "21-04-1987"
        }
        """;

        PepiteDto savedDto = PepiteDto.builder()
                .nom("Jean")
                .prenom("Dupont")
                .dateNaissance(Instant.parse("1987-04-21T00:00:00Z"))
                .build();

        Mockito.when(pepiteService.savePepite(any(PepiteDto.class))).thenReturn(Optional.of(savedDto));

        mockMvc.perform(post("/pepites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Jean"))
                .andExpect(jsonPath("$.prenom").value("Dupont"));
    }

    @Test
    void getAllPepites_ReturnsListOfDto() throws Exception {
        PepiteDto dto1 = PepiteDto.builder().nom("Jean").build();
        PepiteDto dto2 = PepiteDto.builder().nom("Marie").build();

        List<PepiteDto> dtos = Arrays.asList(dto1, dto2);
        Mockito.when(pepiteService.getAllPepites()).thenReturn(dtos);

        mockMvc.perform(get("/pepites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(dtos.size()))
                .andExpect(jsonPath("$[0].nom").value("Jean"))
                .andExpect(jsonPath("$[1].nom").value("Marie"));
    }

    @Test
    void updatePepite_ReturnsUpdatedDto() throws Exception {
        Long id = 1L;
        String jsonContent = """
        {
          "nom": "Updated",
          "prenom": "Nom",
          "dateNaissance": "17-11-1988"
        }
        """;

        PepiteDto updatedDto = PepiteDto.builder()
                .nom("Updated")
                .prenom("Nom")
                .dateNaissance(Instant.parse("1988-11-17T00:00:00Z"))
                .build();

        Mockito.when(pepiteService.updatePepite(any(PepiteDto.class), eq(id))).thenReturn(Optional.of(updatedDto));

        mockMvc.perform(put("/pepites/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Updated"));
    }

    @Test
    void updatePepite_NotFound_ReturnsNotFound() throws Exception {
        Long id = 99L;
        String jsonContent = """
        {
          "nom": "NotFound",
          "prenom": "None",
          "dateNaissance": "01-01-2000"
        }
        """;

        Mockito.when(pepiteService.updatePepite(any(PepiteDto.class), eq(id))).thenReturn(Optional.empty());

        mockMvc.perform(put("/pepites/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
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
