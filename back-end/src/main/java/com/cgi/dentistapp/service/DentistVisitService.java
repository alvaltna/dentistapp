package com.cgi.dentistapp.service;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.cgi.dentistapp.data.dto.DentistVisitDTO;
import com.cgi.dentistapp.data.entity.AppUser;
import com.cgi.dentistapp.data.entity.DentistVisitEntity;
import com.cgi.dentistapp.exception.BadRequestException;
import com.cgi.dentistapp.exception.InvalidArgumentException;
import com.cgi.dentistapp.exception.PageNotFoundException;
import com.cgi.dentistapp.mapper.EntityToOutputObjectMapper;
import com.cgi.dentistapp.output.VisitDetails;
import com.cgi.dentistapp.output.VisitListResponse;
import com.cgi.dentistapp.repository.UserRepository;
import com.cgi.dentistapp.repository.VisitRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.cgi.dentistapp.service.UserService.getUsernameFromJwtToken;

@Service
public class DentistVisitService {

    private final VisitRepository visitRepository;
    private final UserRepository userRepository;
    private final EntityToOutputObjectMapper mapper;


    public DentistVisitService(VisitRepository visitRepository, UserRepository userRepository,
                       EntityToOutputObjectMapper mapper) {
        this.visitRepository = visitRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public VisitDetails getVisitItemById(Long id, String header) {

        AppUser user = null;

        if (header != null && header.length() > 1) {
            String username = getUsernameFromJwtToken(header);
            user = userRepository.findByUsername(username);
            Optional<DentistVisitEntity> visit = visitRepository.findById(username, id);
            if (visit.isPresent()) {
                DentistVisitEntity visit1 = visit.get();
                return mapper.dentistVisitEntityToVisitDetails(visit1, user);
            } else {
                throw new PageNotFoundException("Sorry, page not found!");
            }
        }
        else  {
            throw new JWTDecodeException("Sorry, you are not allowed to access it");
        }
    }

    public DentistVisitEntity getVisitItemById(Long id) {
        Optional<DentistVisitEntity> visit = visitRepository.findById(id);

        if (visit.isPresent()) {
            return visit.get();
        } else {
            throw new PageNotFoundException("Sorry, this page does not exist!");
        }
    }


    public VisitListResponse getVisits(String header,int page, int size, String dentistName, String order, String property) {
        if (header != null && header.length() > 1) {
            String username = getUsernameFromJwtToken(header);
            Pageable pageableRequest = getPageable(page, size, order, property);
            Page<DentistVisitEntity> visits;
            if (dentistName.equals("all")) {
                visits = visitRepository.findAll(username, pageableRequest);
            } else {
                visits = visitRepository.findAllByDentistName(username, dentistName, pageableRequest);
            }

            return getVisitsResponse(visits, page);
        }
        else {
            throw new JWTDecodeException("Sorry, you are not allowed to access this!");
        }
    }

    public VisitListResponse findVisits(String header,int page, int size, String searchTerm, String order, String property) {
        if (header != null && header.length() > 1) {
            String username = getUsernameFromJwtToken(header);
            Pageable pageableRequest = getPageable(page, size, order, property);
            Page<DentistVisitEntity> visits = visitRepository.findBySearchTerm(username, searchTerm, pageableRequest);

            return getVisitsResponse(visits, page);
        }
        else {
            throw new JWTDecodeException("Sorry, you are not allowed to access this!");
        }
    }

    private VisitListResponse getVisitsResponse(Page<DentistVisitEntity> visits, int page) {
        if (page > visits.getTotalPages()) {
            throw new PageNotFoundException("This page does not exist!");
        }

        return new VisitListResponse(mapper.
                dentistVisitEntityListToVisitOverviewList(visits.getContent()), visits.getTotalPages());
    }

    private Pageable getPageable(int page, int size, String order, String property) {
        Pageable pageableRequest = null;

        if (order.equals("ascending")) {

            pageableRequest = PageRequest.of(page, size, new Sort(Sort.Direction.ASC, property));
        } else {
            pageableRequest = PageRequest.of(page, size, new Sort(Sort.Direction.DESC, property));
        }

        return pageableRequest;
    }

    /**
     * Save methods to save posts to DB.
     */

    public VisitDetails addVisit(DentistVisitDTO visitInput, String header) {
        String username = null;
        AppUser user = null;
        DentistVisitEntity visit = new DentistVisitEntity();
        if(visitRepository.findAllByVisitTimeAndDentistName(visitInput.getDentistName(),
                visitInput.getVisitTime()).isEmpty()){
            if (header != null) {
                username = getUsernameFromJwtToken(header);
            }
            if (username != null) {

                    visit.setDentistName(visitInput.getDentistName());
                    visit.setVisitTime(visitInput.getVisitTime());
                    visit.setProcedure(visitInput.getProcedure());
                    visit.setDescription(visitInput.getDescription());
                    user = userRepository.findByUsername(username);
                    visit.setMadeBy(user);
            }

            return mapper.dentistVisitEntityToVisitDetails(visitRepository.save(visit), user);
        }

        else{
            throw new InvalidArgumentException("The appointment time already taken.");
        }

    }

    /**
     * Patch methods to change posts in DB.
     */

    public VisitDetails editVisit(DentistVisitDTO obj, Long visitId) {
        Optional<DentistVisitEntity> visit = visitRepository.findById(visitId);

        if(visitRepository.findAllByVisitTimeAndDentistName(obj.getDentistName(),
                obj.getVisitTime()).isEmpty()) {
            if (visit.isPresent()) {
                String newDentistName = obj.getDentistName();
                LocalDateTime newVisitTime = obj.getVisitTime();
                String newProcedure = obj.getProcedure();
                String newDescription = obj.getDescription();

                if (newDentistName != null && newDentistName.length() != 0) {
                    visitRepository.updateDentistName(visitId, newDentistName);
                }
                if (newVisitTime != null) {
                    visitRepository.updateVisitTime(visitId, newVisitTime);
                }
                if (newProcedure != null && newProcedure.length() != 0) {
                    visitRepository.updateProcedure(visitId, newProcedure);
                }
                if (newDescription != null && newDescription.length() != 0) {
                    visitRepository.updateDescription(visitId, newDescription);
                }

                return mapper.dentistVisitEntityToVisitDetails(getVisitItemById(visitId), null);
            }

            throw new BadRequestException("Problem updating your appointment!");
        }else {

            throw new InvalidArgumentException("The appointment time already taken.");
        }
    }

    /**
     * Delete methods to remove posts from DB.
     */

    public void deleteById(Long id) {
        visitRepository.deleteById(id);
    }

    /**
     * Methods for authorization.
     */
    public void unlockPost(Long id, String header) {

        Optional<DentistVisitEntity> visitToUnlock = visitRepository.findById(id);
        String username = null;

        if (header != null) {
            username = getUsernameFromJwtToken(header);
        }
        if (username != null && visitToUnlock.isPresent()) {

            DentistVisitEntity visitToBeUnlocked = visitToUnlock.get();

            if (visitToBeUnlocked.getMadeBy().getUsername().equals(username)) {

                visitRepository.save(visitToBeUnlocked);
            }
        }
    }

    public String findUsernameOfUser(Long id) {
        return getVisitItemById(id).getMadeBy().getUsername();
    }
}
