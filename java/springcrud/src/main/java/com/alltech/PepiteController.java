package com.alltech;

import com.alltech.pepites.Pepite;
import com.alltech.pepites.PepiteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/pepites")
public class PepiteController {

    @Autowired
    private PepiteService pepiteService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Pepite> createPepite(@Valid @RequestBody PepiteDto pepiteDto) {
        return ResponseEntity.of(pepiteService.savePepite(pepiteDto));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Pepite> getAllPepites() {
        return pepiteService.getAllPepites();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Pepite> updatePepite(@Valid @RequestBody PepiteDto pepiteDto,
                                               @PathVariable("id") Long id) {
        return ResponseEntity.of(pepiteService.updatePepite(pepiteDto, id));
    }

    @DeleteMapping("/{id}")
    public void deletePepiteById(@PathVariable("id") Long id) {
        pepiteService.deleteById(id);
    }
}