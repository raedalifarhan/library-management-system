package com.rf.librarymanagementsystem.Services;

import com.rf.librarymanagementsystem.exceptions.ApiBadRequestException;
import com.rf.librarymanagementsystem.exceptions.ApiNotFoundException;
import com.rf.librarymanagementsystem.models.Patron;
import com.rf.librarymanagementsystem.repositories.PatronRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PatronServiceTest {
    @Mock
    private PatronRepository patronRepository;

    @InjectMocks
    private PatronService underTest;

    @Captor
    private ArgumentCaptor<String> nameCaptor;

    @BeforeEach
    void setUp() {
        underTest = new PatronService(patronRepository);
    }

    @Test
    void canGetAllPatrons() {
        // given
        List<Patron> patrons = new ArrayList<>();
        given(patronRepository.findAll()).willReturn(patrons);

        // when
        List<Patron> result = underTest.getAllPatrons();

        // then
        assertThat(result).isEqualTo(patrons);
        verify(patronRepository).findAll();
    }

    @Test
    void canGetPatronById() {
        // given
        Long id = 1L;
        Patron patron = new Patron();
        given(patronRepository.existsById(id)).willReturn(true);
        given(patronRepository.findById(id)).willReturn(Optional.of(patron));

        // when
        Patron result = underTest.getPatronById(id);

        // then
        assertThat(result).isEqualTo(patron);
        verify(patronRepository).findById(id);
    }

    @Test
    void willThrowWhenPatronNotFoundById() {
        // given
        Long id = 1L;
        given(patronRepository.existsById(id)).willReturn(false);

        // when/then
        assertThatThrownBy(() -> underTest.getPatronById(id))
                .isInstanceOf(ApiNotFoundException.class)
                .hasMessageContaining("Patron not found with id: " + id);

        verify(patronRepository, never()).findById(anyLong());
    }

    @Test
    void willThrowWhenPatronIsNullById() {
        // given
        Long id = 1L;
        given(patronRepository.existsById(id)).willReturn(true);
        given(patronRepository.findById(id)).willReturn(Optional.empty());

        // when/then
        assertThatThrownBy(() -> underTest.getPatronById(id))
                .isInstanceOf(ApiBadRequestException.class)
                .hasMessageContaining("Patron not found");

        verify(patronRepository).findById(id);
    }

    @Test
    void canAddPatron() {
        // given
        Patron patron = new Patron();
        given(patronRepository.save(any(Patron.class))).willReturn(patron);

        // when
        Patron result = underTest.addPatron(patron);

        // then
        assertThat(result).isEqualTo(patron);
        verify(patronRepository).save(patron);
    }

    @Test
    void canUpdatePatron() {
        // given
        Long id = 1L;
        String newName = "newName";
        Patron existingPatron = new Patron();
        existingPatron.setName("oldName");
        Patron updatedPatron = new Patron();
        updatedPatron.setName(newName);

        given(patronRepository.existsById(id)).willReturn(true);
        given(patronRepository.findById(id)).willReturn(Optional.of(existingPatron));
        given(patronRepository.save(any(Patron.class))).willReturn(updatedPatron);

        // when
        Patron result = underTest.updatePatron(id, updatedPatron);

        // then
        assertThat(result.getName()).isEqualTo(newName);
        verify(patronRepository).save(any(Patron.class));
    }

    @Test
    void willThrowWhenPatronToUpdateByIdNotFound() {
        // given
        Long id = 1L;
        Patron updatedPatron = new Patron();

        given(patronRepository.existsById(id)).willReturn(false);

        // when/then
        assertThatThrownBy(() -> underTest.updatePatron(id, updatedPatron))
                .isInstanceOf(ApiNotFoundException.class)
                .hasMessageContaining("Patron not found with id: " + id);

        verify(patronRepository, never()).save(any(Patron.class));
    }

    @Test
    void willThrowWhenPatronIsNull() {
        // given
        Long id = 1L;
        given(patronRepository.existsById(id)).willReturn(true);
        given(patronRepository.findById(id)).willReturn(Optional.empty());

        // when/then
        assertThatThrownBy(() -> underTest.getPatronById(id))
                .isInstanceOf(ApiBadRequestException.class)
                .hasMessageContaining("Patron not found");

        verify(patronRepository).findById(id);
    }

    @Test
    void canDeletePatron() {
        // given
        Long id = 1L;
        given(patronRepository.existsById(id)).willReturn(true);

        // when
        underTest.deletePatron(id);

        // then
        verify(patronRepository).deleteById(id);
    }

    @Test
    void willThrowWhenPatronToDeleteNotFoundById() {
        // given
        Long id = 1L;
        given(patronRepository.existsById(id)).willReturn(false);

        // when/then
        assertThatThrownBy(() -> underTest.deletePatron(id))
                .isInstanceOf(ApiNotFoundException.class)
                .hasMessageContaining("Patron not found with id: " + id);

        verify(patronRepository, never()).deleteById(anyLong());
    }
}