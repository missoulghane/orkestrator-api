package com.m2it.orkestrator.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserCommand {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

}
