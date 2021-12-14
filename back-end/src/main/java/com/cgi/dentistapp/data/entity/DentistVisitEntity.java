package com.cgi.dentistapp.data.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "dentist_visit")
public class DentistVisitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String dentistName;
    private LocalDateTime visitTime;
    private String procedure;
    private String description;
    @ManyToOne(fetch = FetchType.EAGER)
    private AppUser madeBy;

    public DentistVisitEntity(String dentistName, LocalDateTime visitTime, String procedure, String description, AppUser madeBy) {

        this.dentistName = dentistName;
        this.visitTime = visitTime;
        this.procedure = procedure;
        this.description = description;
        this.madeBy = madeBy;


    }

}
