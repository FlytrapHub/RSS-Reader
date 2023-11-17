package com.flytrap.rssreader.presentation.controller;

import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.infrastructure.properties.AdminProperties;
import com.flytrap.rssreader.presentation.dto.Login;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminProperties properties;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public void getAdminProperties(@RequestBody Login request, HttpSession session){

        if (request.code().equals(properties.code())) {
            session.setAttribute(authProperties.sessionId(), SessionMember.from(properties.getMember()));
        }
    }

}
