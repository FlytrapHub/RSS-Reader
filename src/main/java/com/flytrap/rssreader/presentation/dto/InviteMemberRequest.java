package com.flytrap.rssreader.presentation.dto;

import jakarta.validation.constraints.Min;

public record InviteMemberRequest(@Min(0) long inviteeId) {

}
