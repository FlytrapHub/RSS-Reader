package com.flytrap.rssreader.domain.post;


import com.flytrap.rssreader.global.event.EventHolder;
import com.flytrap.rssreader.service.dto.PostOpenParam;
import lombok.Getter;

@Deprecated(since = "이제는 사용하지 않는 도메인입니다.")
public record PostOpenEvent(@Getter PostOpenParam value) implements EventHolder<PostOpenParam> {
}
