package com.flytrap.rssreader.domain.post;


import com.flytrap.rssreader.global.event.EventHolder;
import com.flytrap.rssreader.service.dto.PostOpenParam;
import lombok.Getter;

public record PostOpenEvent(@Getter PostOpenParam value) implements EventHolder<PostOpenParam> {

}
