package com.alltech.pepites;

import com.alltech.PepiteDto;
import com.alltech.exception.ResourceNotFoundException;
import com.alltech.repository.PepiteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PepiteServiceTest {

    @Mock
    private PepiteRepository pepiteRepository;

    @InjectMocks
    private PepiteService pepiteService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void savePepite_ShouldReturnSavedPepite() {
        PepiteDto dto = new PepiteDto("Jean", "Dupont", Instant.now());
        Pepite pepite = Pepite.builder()
                .nom(dto.getNom())
                .prenom(dto.getPrenom())
                .dateNaissance(dto.getDateNaissance())
                .build();

        when(pepiteRepository.save(any(Pepite.class))).thenReturn(pepite);

        Optional<Pepite> result = pepiteService.savePepite(dto);

        assertTrue(result.isPresent());
        assertEquals(dto.getNom(), result.get().getNom());
        verify(pepiteRepository).save(any(Pepite.class));
    }

    @Test
    void getAllPepites_ShouldReturnListOfPepites() {
        Pepite pepite1 = new Pepite();
        Pepite pepite2 = new Pepite();

        List<Pepite> pepites = Arrays.asList(pepite1, pepite2);
        when(pepiteRepository.findAll()).thenReturn(pepites);

        List<Pepite> result = pepiteService.getAllPepites();

        assertEquals(2, result.size());
        verify(pepiteRepository).findAll();
    }

    @Test
    void updatePepite_WhenPepiteExists_ShouldUpdateAndReturn() {
        Long id = 1L;
        Pepite existing = Pepite.builder()
                .nom("Ancien")
                .prenom("Nom")
                .dateNaissance(Instant.now())
                .build();

        PepiteDto update = PepiteDto.builder()
                .nom("Nouveau")
                .prenom("Prénom")
                .dateNaissance(Instant.now())
                .build();

        when(pepiteRepository.findById(id)).thenReturn(Optional.of(existing));
        when(pepiteRepository.save(any(Pepite.class))).thenReturn(existing);

        Optional<Pepite> result = pepiteService.updatePepite(update, id);

        assertTrue(result.isPresent());
        assertEquals(update.getNom(), result.get().getNom());
        verify(pepiteRepository).save(existing);
    }

    @Test
    void updatePepite_WhenPepiteNotFound_ShouldReturnEmpty() {
        Long id = 42L;
        PepiteDto update = new PepiteDto();

        when(pepiteRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Pepite> result = pepiteService.updatePepite(update, id);

        assertTrue(result.isEmpty());
        verify(pepiteRepository, never()).save(any());
    }

    @Test
    void deleteById_WhenPepiteExists_ShouldDeleteWithoutException() {
        Long id = 1L;

        // Pas d'exception levée
        doNothing().when(pepiteRepository).deleteById(id);

        assertDoesNotThrow(() -> pepiteService.deleteById(id));
        verify(pepiteRepository).deleteById(id);
    }

    @Test
    void deleteById_WhenPepiteNotFound_ShouldThrowResourceNotFoundException() {
        Long id = 99L;
        doThrow(EmptyResultDataAccessException.class).when(pepiteRepository).deleteById(id);

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class,
                () -> pepiteService.deleteById(id));
        assertTrue(thrown.getMessage().contains("Pepite not found"));
    }
}
