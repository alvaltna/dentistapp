package com.cgi.dentistapp.controller;

import com.cgi.dentistapp.data.dto.DentistVisitDTO;
import com.cgi.dentistapp.output.VisitDetails;
import com.cgi.dentistapp.output.VisitListResponse;
import com.cgi.dentistapp.service.DentistVisitService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Date;

import static com.cgi.dentistapp.security.SecurityConstants.*;

// PS! VisitListResponse holds the response and the amount of pages!

@RestController
public class VisitController {

    private final DentistVisitService visitService;

    public VisitController(DentistVisitService visitService) {
        this.visitService = visitService;
    }

    @GetMapping("api/visits/{id}")
        public VisitDetails getVisitItemById(@PathVariable Long id,
                                            @RequestHeader(value = HEADER_STRING) String header) {
            return visitService.getVisitItemById(id, header);
        }

        @GetMapping("api/visits")
        public VisitListResponse getVisits(@RequestParam(value = "page", defaultValue = "0") int page,
                                          @RequestParam(value = "size", defaultValue = "4") int size,
                                          @RequestParam(value = "dentistName", defaultValue = "all") String dentistName,
                                          @RequestParam(value = "order", defaultValue = "ascending") String order,
                                          @RequestParam(value = "sortBy", defaultValue = "visitTime") String property,
                                           @RequestHeader(value = HEADER_STRING) String header) {
            if (dentistName.equals("home")) {
                dentistName = "all";
            }
            return visitService.getVisits(header,page, size, dentistName, order, property);
        }

        @GetMapping("api/visits/find")
        public VisitListResponse findVisits(@RequestParam(value = "page", defaultValue = "0") int page,
                                          @RequestParam(value = "size", defaultValue = "4") int size,
                                          @RequestParam(value = "searchTerm", defaultValue = "") String searchTerm,
                                          @RequestParam(value = "order", defaultValue = "ascending") String order,
                                          @RequestParam(value = "sortBy", defaultValue = "visitTime") String property,
                                            @RequestHeader(value = HEADER_STRING) String header) {
            return visitService.findVisits(header, page, size, searchTerm, order, property);
        }

        @PostMapping("api/add/visit")
        public VisitDetails addVisit(@RequestBody @Valid DentistVisitDTO visit,
                                    @RequestHeader(value = HEADER_STRING) String header) {
            return visitService.addVisit(visit, header);
        }

        @PostMapping("api/unlock/{id}")
        @PreAuthorize("@dentistVisitService.findUsernameOfUser(#id) == authentication.name || hasAuthority('ROLE_ADMIN')")
        public void unlock(@PathVariable Long id, @RequestHeader(value = HEADER_STRING) String header) {

            visitService.unlockPost(id, header);
        }

        @DeleteMapping("api/delete/visit/{id}")
        @PreAuthorize("@dentistVisitService.findUsernameOfUser(#id) == authentication.name || hasAuthority('ROLE_ADMIN')")
        public void delete(@PathVariable Long id) {
            visitService.deleteById(id);
        }

        @PatchMapping("api/edit/visit/{id}")
        @PreAuthorize("@dentistVisitService.findUsernameOfUser(#id) == authentication.name || hasAuthority('ROLE_ADMIN')")
        public VisitDetails editVisit(@RequestBody @Valid DentistVisitDTO obj, @PathVariable Long id) {
            return visitService.editVisit(obj, id);
        }

}


