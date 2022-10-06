package com.db.cb.signalhandler;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ControllerTest {

    @Test
    public void testGetInt() {
        RestTemplate rest = new RestTemplate();
        String url = "http://localhost:8080/home/int/3";
        ResponseEntity<Integer> result = rest.getForEntity(url, Integer.class);
        Assert.assertEquals(200, result.getStatusCodeValue());
        try {
            Assert.assertEquals("3", result.getBody().toString());
        }catch(NullPointerException nptr){
            System.out.println(nptr.getMessage());
        }
    }
    @Test
    public void testGetJson() {
        RestTemplate rest = new RestTemplate();
        String url = "http://localhost:8080/home/json/?signal={signal}";
        String signal = "{\"signal\":\"4\"}";
        ResponseEntity<String> result = rest.getForEntity(url, String.class, signal);
        Assert.assertEquals(200, result.getStatusCodeValue());
    }
}