package com.example.kafkaopensearchproject.service;

import com.example.kafkaopensearchproject.domain.ProducerPreference;
import com.example.kafkaopensearchproject.service.createKafkaProperties.CreateProperties;
import com.launchdarkly.eventsource.EventSource;

public interface ManageProducer {
    int StartProducer();
    int StopProducer();
    EventSource createEvent(CreateProperties properties, ProducerPreference producerPreference);

}
