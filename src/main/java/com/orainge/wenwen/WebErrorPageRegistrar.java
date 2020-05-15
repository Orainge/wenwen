package com.orainge.wenwen;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class WebErrorPageRegistrar implements ErrorPageRegistrar {
    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        ErrorPage[] errorPages = new ErrorPage[]{
                new ErrorPage(HttpStatus.FORBIDDEN, "/error/403"),
                new ErrorPage(HttpStatus.NOT_FOUND, "/error/404"),
                new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500")
        };
        registry.addErrorPages(errorPages);
    }
}
