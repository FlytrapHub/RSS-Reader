package com.flytrap.rssreader.global;

import com.flytrap.rssreader.infrastructure.entity.alert.AlertPlatform;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AlertPlatFormTest {

    @Test
    void testOfCodeWithInvalidCode() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> AlertPlatform.ofCode(3));
    }

    @Test
    void testFindAlertPlatformWithValidPlatform() {
        AlertPlatform slack = AlertPlatform.SLACK;
        Assertions.assertEquals(slack, AlertPlatform.findAlertPlatform(slack));

        AlertPlatform discord = AlertPlatform.DISCORD;
        Assertions.assertEquals(discord, AlertPlatform.findAlertPlatform(discord));
    }
}
