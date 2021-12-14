package com.cgi.dentistapp.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cgi.dentistapp.data.entity.AppUser;
import com.cgi.dentistapp.data.entity.DentistVisitEntity;
import com.cgi.dentistapp.data.input.ForgotPasswordInput;
import com.cgi.dentistapp.data.input.ResetPasswordInput;
import com.cgi.dentistapp.data.input.UserRegistrationInput;
import com.cgi.dentistapp.exception.BadRequestException;
import com.cgi.dentistapp.exception.PageNotFoundException;
import com.cgi.dentistapp.mapper.EntityToOutputObjectMapper;
import com.cgi.dentistapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.cgi.dentistapp.security.SecurityConstants.*;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final EntityToOutputObjectMapper mapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository,
                       EmailService emailService, BCryptPasswordEncoder bCryptPasswordEncoder,
                       EntityToOutputObjectMapper mapper) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.mapper = mapper;
    }

    public AppUser save(UserRegistrationInput registration) {
        if (registration == null) {
            throw new BadRequestException("Something went wrong!");
        }
        if (userRepository.findByUsername(registration.getUsername()) != null) {
            throw new BadRequestException("This username is already in use!");
        }
        if (userRepository.findByEmail(registration.getEmail()) != null) {
            throw new BadRequestException("This email is already in use!");
        }

        AppUser appUser = new AppUser();
        appUser.setFirstName(registration.getFirstName());
        appUser.setLastName(registration.getLastName());
        appUser.setEmail(registration.getEmail());
        appUser.setUsername(registration.getUsername());
        appUser.setPassword(bCryptPasswordEncoder.encode(registration.getPassword()));
        appUser.setGrantedAuthorities(
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        return userRepository.save(appUser);
    }

    public List<GrantedAuthority> getGrantedAuthorities(String username) {
        AppUser user = userRepository.findByUsername(username);

        if (user == null) {
            throw new PageNotFoundException("Couldn't find a user with this username!");
        } else {

            return user.getGrantedAuthorities();
        }
    }

    public String getUsernameFromJwt(String token) {

        System.out.println(getUsernameFromJwtToken(token));
        return getUsernameFromJwtToken(token);
    }

    public static String getUsernameFromJwtToken(String token) {
        return JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""))
                .getSubject();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findByUsername(username);
        if (appUser == null) {
            throw new UsernameNotFoundException(username);
        }

        return new User(appUser.getUsername(), appUser.getPassword(), appUser.getGrantedAuthorities());
    }

    public AppUser getUserByResetToken(String resetToken) {
        Optional<AppUser> user = userRepository.findByResetToken(resetToken);
        if (user.isPresent()) {
            return user.get();

        }
        throw new BadRequestException("This token is not valid anymore!");
    }

    public void forgotPassword(ForgotPasswordInput forgotPasswordInput, String frontEndAddress) {
        AppUser user = userRepository.findByUsername(forgotPasswordInput.getUsername());
        if (user == null) {
            throw new BadRequestException("User with this username does not exist!");
        }
        if (user.getEmail() == null) {
            throw new BadRequestException("This user has no email registered!");
        }
        String token = createPasswordResetTokenForUser(user);
        emailService.sendResetTokenEmail(frontEndAddress, token, user);
    }

    private String createPasswordResetTokenForUser(AppUser user) {
        String token = UUID.randomUUID().toString();
        userRepository.addPasswordResetTokenAndDate(user.getId(), token, new Date());
        return token;
    }

    public void resetPassword(ResetPasswordInput forgotPasswordInput) {
        String token = forgotPasswordInput.getToken();
        if (token == null || token.length() == 0) {
            throw new BadRequestException("This token is not valid!");
        }
        Optional<AppUser> userOptional = userRepository.findByResetToken(token);
        if (!userOptional.isPresent()) {
            throw new BadRequestException("This token is not valid!");
        }
        AppUser user = userOptional.get();

        // Valid for two hours
        Calendar cal = Calendar.getInstance();
        if (((user.getResetTokenCreationDate().getTime() + PASSWORD_RESET_TOKEN_EXPIRATION_TIME) -
                cal.getTime().getTime()) <= 0) {
            throw new BadRequestException("This token is not valid!");
        }

        userRepository.changePassword(user.getId(),
                bCryptPasswordEncoder.encode(forgotPasswordInput.getNewPassword()));
    }
}

