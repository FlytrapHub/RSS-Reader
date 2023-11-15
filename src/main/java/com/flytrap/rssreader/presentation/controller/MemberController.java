package com.flytrap.rssreader.presentation.controller;

import com.flytrap.rssreader.global.model.ApplicationResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    @GetMapping
    public ApplicationResponse<Object> searchMemberByName(String name) {

        return new ApplicationResponse<>(null);
    }

}
