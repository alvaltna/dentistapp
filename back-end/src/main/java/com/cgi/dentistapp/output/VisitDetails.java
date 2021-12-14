package com.cgi.dentistapp.output;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitDetails {

    private long id;
    private String dentistName;
    @JsonFormat(pattern="yyyy-MM-dd HH")
    private LocalDateTime visitTime;
    private String procedure;
    private String description;
    private String madeBy;
    private boolean canDelete;
    private boolean canUnlock;

}