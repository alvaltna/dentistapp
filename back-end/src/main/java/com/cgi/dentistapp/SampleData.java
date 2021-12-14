package com.cgi.dentistapp;

import com.cgi.dentistapp.data.entity.AppUser;
import com.cgi.dentistapp.data.entity.DentistVisitEntity;
import com.cgi.dentistapp.repository.UserRepository;
import com.cgi.dentistapp.repository.VisitRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Stream;

@Component
public class SampleData implements CommandLineRunner {

    private UserRepository userRepository;
    private VisitRepository visitRepository;

    public SampleData(UserRepository userRepository, VisitRepository visitRepository) {

        this.userRepository = userRepository;
        this.visitRepository = visitRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        userRepository.save(
                new AppUser("admin_test",
                        "$2a$10$WRCO20EAIVmqpeTcvOp9he1z/PlMvHuz2/i733ULbKchN7yt54I/m",
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
                )
        );

        AppUser au = new AppUser("user_test",
                "$2a$10$05D4lwsoTRUR9lSDWkqkquPM56UZkiTPJ/cG5uK140GvYyqrGzr5u",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        userRepository.save(au);

        String filler = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has " +
                "been the industry's standard dummy text ever since the 1500s, when an unknown printer took a";



        visitRepository.save(new DentistVisitEntity("Mart Tamm", LocalDateTime.of(2021, 12, 5, 8,0,0),
                    "Pulling tooth", filler, au));
        visitRepository.save(new DentistVisitEntity("Mart Tamm", LocalDateTime.of(2021, 12, 5, 9,0,0),
                "Pulling tooth", filler, au));
        visitRepository.save(new DentistVisitEntity("Mart Tamm", LocalDateTime.of(2021, 12, 5, 10,0,0),
                "Pulling tooth", filler, au));
        visitRepository.save(new DentistVisitEntity("Mart Tamm", LocalDateTime.of(2021, 12, 5, 11,0,0),
                "Pulling tooth", filler, au));
        visitRepository.save(new DentistVisitEntity("Mart Tamm", LocalDateTime.of(2021, 12, 5, 12,0,0),
                "Pulling tooth", filler, au));



    }
}

