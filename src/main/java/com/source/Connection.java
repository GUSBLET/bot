package com.source;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


public class Connection {

    private final RestTemplate restTemplate;

    private final String connectionRequest = "http://puppetpalm.com:8081/";

    public Connection(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public boolean sendRequest(Object userId, String path) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("userTelegramId", userId.toString());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<Boolean> responseEntity = restTemplate.postForEntity(
                connectionRequest + path,
                request,
                Boolean.class);


        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody().booleanValue();
        }
        else {
            return false;
        }
    }

    public boolean sendRequestView(String json, String path) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);

        ResponseEntity<Boolean> responseEntity = restTemplate.postForEntity(
                connectionRequest + path,
                requestEntity,
                Boolean.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody().booleanValue();
        } else {
            return false;
        }
    }

    public String sendRequestGet(String path) {

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(
                path,
                String.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            return null;
        }
    }

    public byte[] sendRequestGetFile(String path) {

        ResponseEntity<byte[]> responseEntity = restTemplate.getForEntity(path, byte[].class);;

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            return null;
        }
    }
}
