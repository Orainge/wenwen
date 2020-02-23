package com.orainge.wenwen.service;

import com.orainge.wenwen.util.Response;

public interface AuthService {
    public Response apiRegister(String email, String username, String password);

    public Response apiSendActivate(String email);

    public Response apiSendReset(String email);

    public Response apiActivate(String token);

    public Response toResetPassword(String token);

    public Response apiResetPassword(String token, String password);
}
