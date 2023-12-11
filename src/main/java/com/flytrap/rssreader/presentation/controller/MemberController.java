package com.flytrap.rssreader.presentation.controller;

import com.flytrap.rssreader.domain.member.Member;
import com.flytrap.rssreader.global.model.ApplicationResponse;
import com.flytrap.rssreader.presentation.controller.api.MemberControllerApi;
import com.flytrap.rssreader.presentation.dto.MemberSummary;
import com.flytrap.rssreader.presentation.dto.NameSearch;
import com.flytrap.rssreader.presentation.dto.NameSearch.Result;
import com.flytrap.rssreader.service.MemberService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController implements MemberControllerApi {

    private final MemberService memberService;

    @GetMapping
    public ApplicationResponse<NameSearch.Result> searchMemberByName(NameSearch nameSearch) {
        List<Member> results = memberService.findByName(nameSearch.name());

        List<MemberSummary> memberSummaries = results.stream().map(MemberSummary::from).toList();
        return new ApplicationResponse<>(new Result(memberSummaries));
    }

}
