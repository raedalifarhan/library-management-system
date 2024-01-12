package com.rf.librarymanagementsystem.controllers;

import com.rf.librarymanagementsystem.DTOs.PatronDTO;
import com.rf.librarymanagementsystem.services.PatronService;
import com.rf.librarymanagementsystem.models.Patron;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/patrons")
public class PatronController {

    private final PatronService patronService;
    @Autowired
    private final ModelMapper modelMapper;

    @GetMapping
    public List<PatronDTO> getAllPatrons() {
        List<Patron> patrons = patronService.getAllPatrons();
        return patrons.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatronDTO> getPatronById(@PathVariable Long id) {
        var addedPatron = patronService.getPatronById(id);
        return ResponseEntity.ok(convertToDTO(addedPatron));
    }

    @PostMapping
    public ResponseEntity<PatronDTO> addPatron(@RequestBody PatronDTO patronDTO) {

        Patron patron = convertToEntity(patronDTO);
        Patron addedPatron = patronService.addPatron(patron);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(addedPatron));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatronDTO> updatePatron(@PathVariable Long id, @RequestBody PatronDTO patronDTO) {

        Patron updatedPatron = patronService.updatePatron(id,
                convertToEntity(patronDTO));

        return ResponseEntity.ok(convertToDTO(updatedPatron));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatron(@PathVariable Long id) {
        patronService.deletePatron(id);
        return ResponseEntity.noContent().build();
    }

    private PatronDTO convertToDTO(Patron patron) {
        return modelMapper.map(patron, PatronDTO.class);
    }

    private Patron convertToEntity(PatronDTO patronDTO) {
        return modelMapper.map(patronDTO, Patron.class);
    }
}
