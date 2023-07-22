package com.example.kafkaopensearchproject.domain;

import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@RequiredArgsConstructor
public class ProducerPreference {
    /*
    kafka cluster 접근 정보
     */
    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String brokerServer;

    @Value("${spring.kafka.producer.securityProto}")
    private String securityProto;

    @Value("${spring.kafka.producer.saslJaasConfig}")
    private String saslJaasConfig;

    @Value("${spring.kafka.producer.saslMechanism}")
    private String saslMechanism;



}
