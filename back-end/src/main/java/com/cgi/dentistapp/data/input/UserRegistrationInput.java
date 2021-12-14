package com.cgi.dentistapp.data.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationInput {

    @NotNull
    @NotEmpty
    @Size(min = 4)
    private String username;

    @NotNull
    @NotEmpty
    @Size(min = 2)
    private String firstName;

    @NotNull
    @NotEmpty
    @Size(min = 2)
    private String lastName;

    @NotNull
    @NotEmpty
    @Size(min = 6)
    private String password;

    @NotNull
    @NotEmpty
    @Size(min = 4)
    @Email
    private String email;

}
