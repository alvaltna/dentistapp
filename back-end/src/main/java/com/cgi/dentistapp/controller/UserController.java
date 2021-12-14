package com.cgi.dentistapp.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.cgi.dentistapp.data.entity.DentistVisitEntity;
import com.cgi.dentistapp.data.input.ForgotPasswordInput;
import com.cgi.dentistapp.data.input.ResetPasswordInput;
import com.cgi.dentistapp.data.input.UserRegistrationInput;
import com.cgi.dentistapp.exception.BadRequestException;
import com.cgi.dentistapp.service.DentistVisitService;
import com.cgi.dentistapp.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


import static com.cgi.dentistapp.security.SecurityConstants.HEADER_STRING;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("api/register")
    public void registerUserAccount(@RequestBody @Valid UserRegistrationInput userRegistrationInput) {
        userService.save(userRegistrationInput);
    }

    @GetMapping("api/check")
    public void checkIfLoggedIn(@RequestHeader(value = HEADER_STRING) String header) {
        try {
            if (header != null) {
                userService.getUsernameFromJwt(header);

            }
        } catch (JWTVerificationException decodeException) {
            throw new BadRequestException("User is not logged in!");
        }
    }

    @PostMapping("api/forgotPassword")
    public void forgotPassword(@RequestBody @Valid ForgotPasswordInput forgotPasswordInput,
                               HttpServletRequest request) {
        userService.forgotPassword(forgotPasswordInput, request.getHeader("referer"));
    }

    @PostMapping("api/resetPassword")
    public void resetPassword(@RequestBody @Valid ResetPasswordInput forgotPasswordInput) {
        userService.resetPassword(forgotPasswordInput);
    }

}
