package com.internsystem.internmanagement.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class InternDTO {
    private Long internId;
    private String internCode;
    private String name;
    private String email;
    private String phone;
    private String institute;
    private LocalDate trainingStartDate;
    private LocalDate trainingEndDate;
    private String username;
    private String password;
}
