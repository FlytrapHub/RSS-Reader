package com.flytrap.rssreader.presentation.filter;

import com.flytrap.rssreader.infrastructure.properties.AuthProperties;
import com.flytrap.rssreader.presentation.dto.SessionMember;
import com.flytrap.rssreader.presentation.resolver.AuthorizationContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@Order(2)
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter { //TODO 통합테스트 해보면 좋겠습니다.

    private final AuthProperties authProperties;
    private final AuthorizationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain) throws IOException, ServletException {

        HttpSession session = request.getSession(false);

        SessionMember attribute = (session != null)
            ? (SessionMember) session.getAttribute(authProperties.sessionId())
            : null;
        context.setLoginMember(attribute);

        chain.doFilter(request, response);
    }
}
