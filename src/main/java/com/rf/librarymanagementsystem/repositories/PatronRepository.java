package com.rf.librarymanagementsystem.repositories;

import com.rf.librarymanagementsystem.models.Patron;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatronRepository extends JpaRepository<Patron, Long> {

}
