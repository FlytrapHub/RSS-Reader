package com.flytrap.rssreader.presentation.resolver;

import com.flytrap.rssreader.domain.member.Member;
import java.util.Optional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Slf4j
@Getter
@Component
@RequestScope
public class AuthorizationContext {

    private Optional<Member> loginMember;

    public void setLoginMember(Member member) {
        this.loginMember = Optional.ofNullable(member);
    }
}
