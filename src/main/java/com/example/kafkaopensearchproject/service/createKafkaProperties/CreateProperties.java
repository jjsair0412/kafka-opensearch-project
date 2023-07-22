package com.example.kafkaopensearchproject.service.createKafkaProperties;

import com.example.kafkaopensearchproject.domain.ProducerPreference;

import java.util.Properties;

public interface CreateProperties {
    Properties CreateProperties(ProducerPreference producerPreference); // kafka properties 설정 메서드
}
