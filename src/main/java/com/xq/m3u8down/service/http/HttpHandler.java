package com.xq.m3u8down.service.http;

import com.xq.m3u8down.util.JacksonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.codec.json.Jackson2SmileDecoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * Description: http请求服务类
 *
 * @author 13797
 * @version v0.0.1
 * 2021/11/19 23:58
 */
public class HttpHandler {
    private static final Logger logger = LoggerFactory.getLogger(HttpHandler.class);
    public final static WebClient WEB_CLIENT;

    static {
        Jackson2JsonDecoder decoder = new Jackson2JsonDecoder(JacksonUtil.getObjectMapper());
        Jackson2JsonEncoder encoder = new Jackson2JsonEncoder(JacksonUtil.getObjectMapper());
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs()
                        .jackson2JsonDecoder(decoder)
                )
                .codecs(configurer -> configurer.defaultCodecs()
                        .jackson2JsonEncoder(encoder)
                )
                .codecs(configurer -> configurer.defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024)
                )
                .build();
        WEB_CLIENT = WebClient.builder()
                .exchangeStrategies(exchangeStrategies)
                .build();
    }

    public static WebClient getWebClient() {
        return WEB_CLIENT;
    }

    public static <T> Mono<T> exchangeGet(URI uri, Class<T> tClass) {
        return getWebClient().get()
                .uri(uri)
                .retrieve()
                .bodyToMono(tClass);
    }

    public static <T> Mono<T> exchangeGet(URI uri, ParameterizedTypeReference<T> typeReference) {
        return getWebClient().get()
                .uri(uri)
                .retrieve()
                .bodyToMono(typeReference);
    }

    public static Mono<String> exchangeGetString(URI uri) {
        return getWebClient().get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class);
    }

    public static Mono<byte[]> exchangeGetBytes(URI uri) {
        return getWebClient().get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatus::isError, (response) -> Mono.empty())
                .bodyToMono(byte[].class)
                .onErrorContinue((throwable, o) -> logger.error("{} 请求失败", uri));

    }

}
