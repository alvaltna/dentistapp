package com.cgi.dentistapp.repository;


import com.cgi.dentistapp.data.entity.DentistVisitEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
@RestResource(exported = false)
public interface VisitRepository extends JpaRepository<DentistVisitEntity, Long> {



    @Query("select d from DentistVisitEntity d where d.madeBy.username = :userName")
    Page<DentistVisitEntity> findAll(@Param("userName") String userName,
                                                  Pageable pageable);

    @Query("select d from DentistVisitEntity d where d.madeBy.username = :userName AND d.id = :id")
    Optional<DentistVisitEntity> findById(@Param("userName") String userName, @Param("id") Long id);

    @Query("select d from DentistVisitEntity d where d.id = :id")
    Optional<DentistVisitEntity> findById( @Param("id") Long id);

    @Query("select d from DentistVisitEntity d where LOWER(d.dentistName) = LOWER(:dentistName) AND d.madeBy.username = :userName")
    Page<DentistVisitEntity> findAllByDentistName(@Param("userName") String userName,
                                                  @Param("dentistName") String dentistName,
                                                  Pageable pageable);

    @Query("select d from DentistVisitEntity d where (d.visitTime = :visitTime AND d.dentistName = :dentistName)")
    List<DentistVisitEntity> findAllByVisitTimeAndDentistName(@Param("dentistName") String dentistName,
                                                              @Param("visitTime") LocalDateTime visitTime);



    @Query("SELECT d FROM DentistVisitEntity d WHERE (LOWER(d.dentistName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(d.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))" +
            "AND d.madeBy.username = :userName")
    Page<DentistVisitEntity> findBySearchTerm(@Param("userName") String userName,
                                              @Param("searchTerm") String searchTerm,
                                              Pageable pageable);

    @Modifying
    @Transactional
    @Query("update DentistVisitEntity d set d.dentistName =:dentistName where d.id =:id")
    void updateDentistName(@Param("id") Long id, @Param("dentistName") String dentistName);

    @Modifying
    @Transactional
    @Query("update DentistVisitEntity d set d.visitTime =:visitTime where d.id =:id")
    void updateVisitTime(@Param("id") Long id, @Param("visitTime") LocalDateTime visitTime);

    @Modifying
    @Transactional
    @Query("update DentistVisitEntity d set d.procedure =:procedure where d.id =:id")
    void updateProcedure(@Param("id") Long id, @Param("procedure") String procedure);

    @Modifying
    @Transactional
    @Query("update DentistVisitEntity d set d.description =:description where d.id =:id")
    void updateDescription(@Param("id") Long id, @Param("description") String description);

    @Modifying
    @Query("delete from DentistVisitEntity d where d.id = :id")
    void deleteById( @Param("id") Long id);




}
