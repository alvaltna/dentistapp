package com.cgi.dentistapp.output;

import java.time.LocalDateTime;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitOverview {

    private Long id;
    private String dentistName;
    private LocalDateTime visitTime;
}