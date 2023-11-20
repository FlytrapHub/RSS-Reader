package com.flytrap.rssreader.presentation.resolver;

import com.flytrap.rssreader.domain.member.Member;
import com.flytrap.rssreader.presentation.dto.SessionMember;
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

    private Optional<SessionMember> loginMember;

    public void setLoginMember(SessionMember member) {
        this.loginMember = Optional.ofNullable(member);
    }
}
