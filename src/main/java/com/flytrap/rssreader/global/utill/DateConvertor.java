package com.flytrap.rssreader.global.utill;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateConvertor {

    private static final String DATE_FORMAT_ABBREVIATION = "EEE, dd MMM yyyy HH:mm:ss z";
    private static final String DATE_FORMAT_NUMERICAL = "EEE, dd MMM yyyy HH:mm:ss Z";
    private static final String OFFSET_GMT = "GMT";
    private static final String OFFSET_0000 = "+0000";
    private static final String OFFSET_0900 = "+0900";
    private static final String REGEX_SPACE = "\\s+";

    /**
     * "Wed, 29 Nov 2023 13:49:14 +0900" 및 "Tue, 07 Nov 2023 14:12:30 GMT"
     * 형식의 문자열을 Instant 객체로 변환하는 메서드
     *
     * @param parsingDate Instant 객체로 변경할 날짜 문자열
     * @return Instant 객체
     */
    public static Instant convertToInstant(String parsingDate) {
        String[] dateParts = parsingDate.split(REGEX_SPACE);
        String offsetZ = dateParts[dateParts.length - 1];

        DateTimeFormatter formatter = switch (offsetZ) {
            case OFFSET_GMT -> DateTimeFormatter.ofPattern(DATE_FORMAT_ABBREVIATION)
                .withLocale(Locale.ENGLISH);
            case OFFSET_0000, OFFSET_0900 -> DateTimeFormatter.ofPattern(DATE_FORMAT_NUMERICAL)
                .withLocale(Locale.ENGLISH);
            default -> throw new IllegalStateException("Unexpected value: " + parsingDate);
        };

        return Instant.from(formatter.parse(parsingDate));
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
