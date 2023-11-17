package com.flytrap.rssreader.fixture;

import com.flytrap.rssreader.domain.member.OauthServer;
import com.flytrap.rssreader.domain.subscribe.BlogPlatform;
import com.flytrap.rssreader.infrastructure.entity.subscribe.SubscribeEntity;

public class FixtureFields {

    private static final Long LONG_1L = 1L;
    private static final String EMAIL_TEST_GMAIL = "test@gmail.com";
    private static final String LOGIN = "login";
    private static final String NAME = "name";
    private static final String AVATAR_URL = "https://avatarUrl.jpg";
    private static final String RSS_URL = "https://avatarUrl.jpg";
    private static final String GUID = "guid";
    private static final String DATETIME_20231115 = "2023-11-15 00:00:00";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";

    public static class UserResourceFields {
        public static Long id = LONG_1L;
        public static String email = EMAIL_TEST_GMAIL;
        public static String login = LOGIN;
        public static String avatarUrl = AVATAR_URL;
    }

    public static class MemberEntityFields {
        public static Long id = LONG_1L;
        public static String email = EMAIL_TEST_GMAIL;
        public static String name = NAME;
        public static String profile = AVATAR_URL;
        public static Long oauthPk = LONG_1L;
        public static OauthServer oauthServer = OauthServer.GITHUB;
    }

    public static class MemberFields {
        public static Long id = LONG_1L;
        public static String name = NAME;
        public static String email = EMAIL_TEST_GMAIL;
        public static String profile = AVATAR_URL;
        public static Long oauthPk = LONG_1L;
        public static OauthServer oauthServer = OauthServer.GITHUB;
    }

    public static class RssItemResourceFields {
        public static String guid = GUID;
        public static String title = TITLE;
        public static String description = DESCRIPTION;
        public static String pubDate = DATETIME_20231115;
    }

    public static class SubscribeEntityFields {
        public static Long id = LONG_1L;
        public static String url = RSS_URL;
        public static String description = DESCRIPTION;
        public static BlogPlatform platform = BlogPlatform.VELOG;
    }

    public static class PostEntityFields {
        public static Long id = LONG_1L;
        public static String guid = GUID;
        public static String title = TITLE;
        public static String description = DESCRIPTION;
        public static SubscribeEntity subscribe = FixtureFactory.generateSubscribeEntity();
    }
}
