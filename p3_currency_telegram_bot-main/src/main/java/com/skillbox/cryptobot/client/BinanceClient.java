package com.skillbox.cryptobot.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@Service
@Slf4j
public class BinanceClient {

    private final HttpGet httpGet;
    private final ObjectMapper mapper;
    private final HttpClient httpClient;

    public BinanceClient(@Value("${binance.api.getPrice}") String uri) {
        httpGet = new HttpGet(uri);
        mapper = new ObjectMapper();
        httpClient = HttpClientBuilder.create()
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();
    }

    public double getBitcoinPrice() throws IOException {
        log.info("Выполнение запроса к Binance API для получения цены биткоина.");
        try {
            return mapper.readTree(EntityUtils.toString(httpClient.execute(httpGet).getEntity()))
                    .path("price").asDouble();
        } catch (IOException e) {
            log.error("Ошибка при получении цены биткоина от Binance.", e);
            throw e;
        }
    }
}
