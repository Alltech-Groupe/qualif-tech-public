package com.alltech.pepites;

import com.alltech.exception.ResourceNotFoundException;
import com.alltech.repository.PepiteRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PepiteService {

    private final PepiteRepository pepiteRepository;

    PepiteService(PepiteRepository pepiteRepository) {
        this.pepiteRepository = pepiteRepository;
    }

    private PepiteDto convertToDto(Pepite pepite) {
        return PepiteDto.builder()
                .id(pepite.getId())
                .nom(pepite.getNom())
                .prenom(pepite.getPrenom())
                .dateNaissance(pepite.getDateNaissance())
                .build();
    }

    private Pepite convertToEntity(PepiteDto dto) {
        return Pepite.builder()
                .nom(dto.getNom())
                .prenom(dto.getPrenom())
                .dateNaissance(dto.getDateNaissance())
                .build();
    }

    public Optional<PepiteDto> savePepite(PepiteDto pepiteDto) {
        Pepite pepite = convertToEntity(pepiteDto);
        Pepite saved = pepiteRepository.save(pepite);
        return Optional.of(convertToDto(saved));
    }

    public List<PepiteDto> getAllPepites() {
        return StreamSupport.stream(pepiteRepository.findAll().spliterator(), true)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<PepiteDto> updatePepite(PepiteDto pepiteDto, Long id) {
        Optional<Pepite> pepiteFromRepo = pepiteRepository.findById(id);
        if (pepiteFromRepo.isPresent()) {
            Pepite pepiteToUpdate = pepiteFromRepo.get();
            pepiteToUpdate.setNom(pepiteDto.getNom());
            pepiteToUpdate.setPrenom(pepiteDto.getPrenom());
            pepiteToUpdate.setDateNaissance(pepiteDto.getDateNaissance());
            Pepite updated = pepiteRepository.save(pepiteToUpdate);
            return Optional.of(convertToDto(updated));
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
    public Optional<PepiteDto> get(Long id) {
        Optional<Pepite> pepite = pepiteRepository.findById(id);
        return pepite.map(this::convertToDto);
    }

}
