package com.example.kafkaopensearchproject.service;

import com.example.kafkaopensearchproject.domain.ProducerPreference;
import com.example.kafkaopensearchproject.service.createKafkaProperties.CreateProperties;
import com.example.kafkaopensearchproject.service.createKafkaProperties.CreatePropertiesImpl;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProducerCreateTest {


    @Autowired
    private ProducerPreference producerPreference;

    @Test
    public void producerTest(){
        CreateProperties properties = new CreatePropertiesImpl();

        KafkaProducer<String,String> producer = new KafkaProducer<>(properties.CreateProperties(producerPreference));

        ProducerRecord<String, String> producerRecord = new ProducerRecord<>("boot_test","hello message from spring boot project !!");

        producer.send(producerRecord);

        producer.flush();

        producer.close();

    }

}
