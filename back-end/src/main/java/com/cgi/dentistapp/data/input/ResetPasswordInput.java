package com.cgi.dentistapp.data.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordInput {

    @NotNull
    @NotEmpty
    @Size(min = 4)
    private String username;

    @NotNull
    @NotEmpty
    private String token;

    @NotNull
    @NotEmpty
    @Size(min = 6)
    private String newPassword;

}
