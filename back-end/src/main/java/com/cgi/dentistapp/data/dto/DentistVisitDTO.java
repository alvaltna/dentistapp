package com.cgi.dentistapp.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DentistVisitDTO {

    @Size(min = 1, max = 50)
    private String dentistName;

    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd HH")
    private LocalDateTime visitTime;

    @NotNull
    @NotEmpty
    @Size(min=5)
    private String procedure;

    @NotNull
    @NotEmpty
    @Size(min = 5)
    private String description;


}
