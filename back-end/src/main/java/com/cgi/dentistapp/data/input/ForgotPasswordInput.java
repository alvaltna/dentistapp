package com.cgi.dentistapp.data.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswordInput {

    @NotNull
    @NotEmpty
    private String username;
}
