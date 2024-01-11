package com.rf.librarymanagementsystem.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatronDTO {

    private Long id;
    private String name;
    private String contactInformation;
}
