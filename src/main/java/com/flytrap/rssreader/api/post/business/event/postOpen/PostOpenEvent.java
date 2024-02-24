package com.flytrap.rssreader.api.post.business.event.postOpen;

import com.flytrap.rssreader.global.event.EventHolder;
import com.flytrap.rssreader.service.dto.PostOpenParam;
import lombok.Getter;

public record PostOpenEvent(@Getter PostOpenParam value) implements EventHolder<PostOpenParam> {
}
