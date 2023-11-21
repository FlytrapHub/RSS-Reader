package com.flytrap.rssreader.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class SubscribeService {

    //TODO:
    // step1검색을 누르면 유효한 RSS인지 검증한다
    // step2 이미 구독한 블로그라면 즉 DB에 저장되어있다면
    // 새로 구독하지말고 DB에있는 url(정보를) 가져다써라
    //  step3하지만 새로운 블로그라면 새로 추가해주자
    //  새로운 구독이라면 DB 관계형테이블 구독 에 넣어라

}
