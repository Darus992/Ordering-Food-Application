package pl.dariuszgilewicz.infrastructure.request_form;

import lombok.*;
@Data
public class LoginRequest {

    private String username;
    private String password;
}
