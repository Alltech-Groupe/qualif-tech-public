package com.alltech.pepites;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pepites")
public class PepiteController {

    @Autowired
    private PepiteService pepiteService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PepiteDto> createPepite(@Valid @RequestBody PepiteDto pepiteDto) {
        return pepiteService.savePepite(pepiteDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PepiteDto> getAllPepites() {
        return pepiteService.getAllPepites();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PepiteDto> updatePepite(@Valid @RequestBody PepiteDto pepiteDto,
                                                  @PathVariable("id") Long id) {
        return pepiteService.updatePepite(pepiteDto, id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void deletePepiteById(@PathVariable("id") Long id) {
        pepiteService.deleteById(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PepiteDto> getPepite(@PathVariable("id") Long id) {
       return ResponseEntity.of(pepiteService.get(id));
    }
}
