package com.rf.librarymanagementsystem.Services;

import com.rf.librarymanagementsystem.exceptions.ApiBadRequestException;
import com.rf.librarymanagementsystem.exceptions.ApiNotFoundException;
import com.rf.librarymanagementsystem.models.Patron;
import com.rf.librarymanagementsystem.repositories.PatronRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PatronService {

    @Autowired
    private final PatronRepository patronRepository;

    public List<Patron> getAllPatrons() {
        return patronRepository.findAll();
    }

    public Patron getPatronById(Long id) {

        if (!patronRepository.existsById(id))
            throw new ApiNotFoundException("Patron not found with id: " + id);

        Patron patron = patronRepository.findById(id).orElse(null);

        if (patron == null)
            throw new ApiBadRequestException("Patron not found");

        return patron;
    }

    public Patron addPatron(Patron patron) {
        return patronRepository.save(patron);
    }

    public Patron updatePatron(Long id, Patron patron) {

        if (!patronRepository.existsById(id))
            throw new ApiNotFoundException("Patron not found with id: " + id);

        Patron patronToUpdate = patronRepository.findById(id).orElse(null);

        if (patronToUpdate != null) {
            // Update the existing patron entity
            // todo: Use mapper to update properties.
            patronToUpdate.setName(patron.getName());
            patronToUpdate.setContactInformation(patron.getContactInformation());
        }

        return patronRepository.save(patronToUpdate);
    }

    public void deletePatron(Long id) {

        if (!patronRepository.existsById(id))
            throw new ApiNotFoundException("Patron not found with id: " + id);

        patronRepository.deleteById(id);
    }


}
