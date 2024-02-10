package com.flytrap.rssreader.global.utill;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class DateConvertor {

    private static final String DATE_FORMAT_ABBREVIATION = "EEE, dd MMM yyyy HH:mm:ss z";
    private static final String DATE_FORMAT_NUMERICAL = "EEE, dd MMM yyyy HH:mm:ss Z";

    private static final DateTimeFormatter[] FORMATTERS = {
        DateTimeFormatter.ofPattern(DATE_FORMAT_ABBREVIATION).withLocale(Locale.ENGLISH),
        DateTimeFormatter.ofPattern(DATE_FORMAT_NUMERICAL).withLocale(Locale.ENGLISH),
        DateTimeFormatter.ISO_DATE_TIME,
    };

    /**
     * "Wed, 29 Nov 2023 13:49:14 +0900" 및 "Tue, 07 Nov 2023 14:12:30 GMT"
     * 형식 등의 문자열을 Instant 객체로 변환하는 메서드
     *
     * @param parsingDate Instant 객체로 변경할 날짜 문자열
     * @return Instant 객체
     */
    public static Instant convertToInstant(String parsingDate) {

        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                return Instant.from(formatter.parse(parsingDate));
            } catch (DateTimeParseException ignored) {

            }
        }
        throw new IllegalStateException("Unexpected value: " + parsingDate);
    }

    /**
     * 블로그마다 pubDate 형태가 다 달라서 새로운 블로그 구독할때마다 테스트 해야 할듯합니다.
     * 테스트하기 쉽게 하기 위해 main메서드를 만들었습니다.
     * @param args
     */
    public static void main(String[] args) {
        String date = "Tue, 21 Nov 2023 07:30:08 +0000";
        Instant instant = convertToInstant(date);

        System.out.println(instant);
    }
}
