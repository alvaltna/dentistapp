package com.cgi.dentistapp.repository;

import com.cgi.dentistapp.data.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

@Repository
@EnableJpaRepositories
@RestResource(exported = false)
public interface UserRepository extends JpaRepository<AppUser, Long> {

    AppUser findByUsername(String username);

    AppUser findByEmail(String email);

    @Query("select a.id from AppUser a where a.username =:username")
    Long findIdByUsername(@Param("username") String username);

    Optional<AppUser> findByResetToken(String resetToken);

    @Modifying
    @Transactional
    @Query("UPDATE AppUser a SET a.resetToken = :resetToken, " +
            "a.resetTokenCreationDate = :resetTokenCreationDate " +
            "where a.id = :id")
    void addPasswordResetTokenAndDate(@Param("id") Long id,
                                      @Param("resetToken") String resetToken,
                                      @Param("resetTokenCreationDate") Date resetTokenCreationDate);

    @Modifying
    @Transactional
    @Query("UPDATE AppUser a SET a.resetToken = null, " +
            "a.resetTokenCreationDate = null, a.password = :password " +
            "where a.id = :id")
    void changePassword(@Param("id") Long id,
                        @Param("password") String password);
}

