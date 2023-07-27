package com.example.kafkaopensearchproject.service.producer;

import com.example.kafkaopensearchproject.domain.ProducerPreference;
import com.example.kafkaopensearchproject.service.createKafkaProperties.CreateProperties;
import com.example.kafkaopensearchproject.service.producer.handlers.WikimediaChangeHandler;
import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.EventSource;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.concurrent.TimeUnit;


@RequiredArgsConstructor
@Service
public class ManageProducerImpl implements ManageProducer{

    private final CreateProperties properties;
    private final ProducerPreference producerPreference;

    @Value("${spring.kafka.producer.topicName}")
    private String topic;

    @Value("${spring.kafka.producer.url}")
    private String url;

    @Override
    public int StartProducer() {
        try {
            createEvent(properties,producerPreference).start();
            TimeUnit.MINUTES.sleep(10);
            return 1;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int StopProducer() {
        createEvent(properties,producerPreference).close();
        return 0;
    }

    @Override
    public EventSource createEvent(CreateProperties properties, ProducerPreference producerPreference){
        KafkaProducer<String,String> producer = new KafkaProducer<>(properties.CreateProperties(producerPreference));
        EventHandler eventHandler = new WikimediaChangeHandler(producer, this.topic);
        EventSource.Builder builder = new EventSource.Builder(eventHandler, URI.create(this.url));
        return builder.build();
    }
}
