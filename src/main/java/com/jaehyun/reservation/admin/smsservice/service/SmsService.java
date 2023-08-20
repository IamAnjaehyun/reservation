package com.jaehyun.reservation.admin.smsservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaehyun.reservation.admin.smsservice.dto.SmsDto;
import com.jaehyun.reservation.admin.smsservice.dto.SmsReqDto;
import com.jaehyun.reservation.admin.smsservice.dto.SmsResDto;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsService {

  @Value("${sms_accessKey}")
  private String accessKey;

  @Value("${sms_secretKey}")
  private String secretKey;

  @Value("${sms_serviceId}")
  private String serviceId;

  @Value("${sms_senderPhone}")
  private String phone;

  public String makeSignature(Long time) throws NoSuchAlgorithmException, InvalidKeyException {
    String space = " ";
    String newLine = "\n";
    String method = "POST";
    String url = "/sms/v2/services/" + this.serviceId + "/messages";
    String timestamp = time.toString();
    String accessKey = this.accessKey;
    String secretKey = this.secretKey;

    String message = method
        + space
        + url
        + newLine
        + timestamp
        + newLine
        + accessKey;

    SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8),
        "HmacSHA256");
    Mac mac = Mac.getInstance("HmacSHA256");
    mac.init(signingKey);

    byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));

    return Base64.encodeBase64String(rawHmac);
  }

  public SmsResDto sendSms(SmsDto smsDto)
      throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException {
    Long time = System.currentTimeMillis();

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("x-ncp-apigw-timestamp", time.toString());
    headers.set("x-ncp-iam-access-key", accessKey);
    headers.set("x-ncp-apigw-signature-v2", makeSignature(time));

    List<SmsDto> messages = new ArrayList<>();
    messages.add(smsDto);

    SmsReqDto request = SmsReqDto.builder()
        .type("SMS")
        .contentType("COMM")
        .countryCode("82")
        .from(phone)
        .content(smsDto.getContent())
        .messages(messages)
        .build();

    ObjectMapper objectMapper = new ObjectMapper();
    String body = objectMapper.writeValueAsString(request);
    HttpEntity<String> httpBody = new HttpEntity<>(body, headers);

    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    return restTemplate.postForObject(
        new URI("https://sens.apigw.ntruss.com/sms/v2/services/" + serviceId + "/messages"),
        httpBody, SmsResDto.class);
  }
}
