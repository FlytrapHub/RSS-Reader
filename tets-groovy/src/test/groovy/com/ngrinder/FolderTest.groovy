package com.ngrinder

import net.grinder.script.GTest
import net.grinder.scriptengine.groovy.junit.annotation.BeforeProcess
import net.grinder.scriptengine.groovy.junit.annotation.BeforeThread
import org.junit.Before
import org.junit.Test
import org.ngrinder.http.HTTPRequest
import org.ngrinder.http.HTTPRequestControl
import org.ngrinder.http.HTTPResponse
import org.ngrinder.http.cookie.Cookie
import org.ngrinder.http.cookie.CookieManager

import static net.grinder.script.Grinder.grinder
import static org.hamcrest.Matchers.is
import static org.junit.Assert.assertThat

class FolderTest {

    public static GTest testRecord1
    public static GTest testRecord2
    public static HTTPRequest request
    public static Map<String, String> headers = [:]
    public static String body = "{\n    \"code\": \"파리지옥-이린-감자-에이프-지니-듀이_20230316\"\n}"
    public static List<Cookie> cookies = []

    @BeforeProcess
    public static void beforeProcess() {
        HTTPRequestControl.setConnectionTimeout(300000)
        testRecord1 = new GTest(1, "127.0.0.1")
        testRecord2 = new GTest(2, "127.0.0.1")
        request = new HTTPRequest()

        // Set header data
        headers.put("Content-Type", "application/json")
        grinder.logger.info("before process.")
    }

    @BeforeThread
    public void beforeThread() {
        testRecord1.record(this, "test01")
        testRecord2.record(this, "test02")
        grinder.statistics.delayReports = true
        grinder.logger.info("before thread.")
    }

    @Before
    public void before() {
        request.setHeaders(headers)
        CookieManager.addCookies(cookies)
        grinder.logger.info("before. init headers and cookies")
    }

    //로그인 ->  구독 폴더리스트 조회
    @Test
    public void test01() {
        HTTPResponse response = request.POST("http://127.0.0.1:8080/api/admin/login", body.getBytes("UTF-8"))

        if (response.statusCode == 301 || response.statusCode == 302) {
            grinder.logger.warn("Warning. The response may not be correct. The response code was {}.", response.statusCode)
        } else {
            assertThat(response.statusCode, is(201))
        }
    }

    @Test
    public void test02() {
        grinder.logger.info("test2")
        String url = "http://127.0.0.1:8080/api/folders";

        HTTPResponse response = request.GET(url, params)

        if (response.statusCode == 301 || response.statusCode == 302) {
            grinder.logger.warn("Warning. The response may not be correct. The response code was {}.", response.statusCode)
        } else {
            assertThat(response.statusCode, is(200))
        }
    }
}
