package com.cgi.dentistapp.mapper;



import com.cgi.dentistapp.data.entity.AppUser;
import com.cgi.dentistapp.data.entity.DentistVisitEntity;
import com.cgi.dentistapp.output.VisitDetails;
import com.cgi.dentistapp.output.VisitOverview;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class EntityToOutputObjectMapper {

    public List<VisitOverview> dentistVisitEntityListToVisitOverviewList(List<DentistVisitEntity> visits) {
        return visits.stream().map(this::dentistVisitEntityToVisitOverview).collect(toList());
    }

    private VisitOverview dentistVisitEntityToVisitOverview(DentistVisitEntity visit) {
        VisitOverview visitOverview = new VisitOverview();
        visitOverview.setId(visit.getId());
        visitOverview.setDentistName(visit.getDentistName());
        visitOverview.setVisitTime(visit.getVisitTime());

        return visitOverview;
    }

    public VisitDetails dentistVisitEntityToVisitDetails(DentistVisitEntity visit, AppUser currentUser) {

        VisitDetails visitDetails = new VisitDetails();
        visitDetails.setId(visit.getId());
        visitDetails.setDentistName(visit.getDentistName());
        visitDetails.setVisitTime(visit.getVisitTime());
        visitDetails.setProcedure(visit.getProcedure());
        visitDetails.setDescription(visit.getDescription());
        visitDetails.setMadeBy(visit.getMadeBy().getUsername());


        if (currentUser != null) {
            if (currentUser.getUsername().equals(visitDetails.getMadeBy()) ||
                    currentUser.getGrantedAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                visitDetails.setCanDelete(true);
            }
            if (currentUser.getUsername().equals(visitDetails.getMadeBy())) {

                visitDetails.setCanUnlock(true);
            }
        }
        else {
            visitDetails.setCanDelete(false);
            visitDetails.setCanUnlock(false);
        }

        return visitDetails;
    }

}

