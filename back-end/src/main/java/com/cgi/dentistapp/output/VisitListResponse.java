package com.cgi.dentistapp.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitListResponse {


    private List<VisitOverview> visit;
    private int amountOfPages;

}