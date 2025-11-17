package com.alltech.pepites;

import com.alltech.PepiteDto;
import com.alltech.exception.ResourceNotFoundException;
import com.alltech.repository.PepiteRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class PepiteService {

    private final PepiteRepository pepiteRepository;

    PepiteService(PepiteRepository pepiteRepository) {
        this.pepiteRepository = pepiteRepository;
    }

    public Optional<Pepite> savePepite(PepiteDto pepiteDto) {
        Pepite pepite = Pepite.builder()
                .dateNaissance(pepiteDto.getDateNaissance())
                .nom(pepiteDto.getNom())
                .prenom(pepiteDto.getPrenom()).build();

        return Optional.of(pepiteRepository.save(pepite));
    }

    public List<Pepite> getAllPepites() {
        return StreamSupport.stream(pepiteRepository.findAll().spliterator(), true)
                .toList();
    }

    public Optional<Pepite> updatePepite(Pepite pepite, Long id) {
        Optional<Pepite> pepiteRepo = Optional.of(pepiteRepository.findById(id)).get();
        if (pepiteRepo.isPresent()) {
            Pepite pepite1 = pepiteRepo.get();
            pepite1.setNom(pepite.getNom());
            pepite1.setPrenom(pepite.getNom());
            pepite1.setDateNaissance(pepite.getDateNaissance());
            return Optional.of(pepiteRepository.save(pepite1));
        }
        return Optional.empty();
    }

    public void deleteById(Long id) {
        try {
            pepiteRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Pepite not found with id : " + id);
        }

    }
}
