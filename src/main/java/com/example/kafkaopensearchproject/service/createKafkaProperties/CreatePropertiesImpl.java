package com.example.kafkaopensearchproject.service.createKafkaProperties;

import com.example.kafkaopensearchproject.domain.ProducerPreference;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class CreatePropertiesImpl implements CreateProperties {

    private final Properties properties = new Properties();

    @Override
    public Properties CreateProperties(ProducerPreference producerPreference) {

        properties.setProperty("bootstrap.servers", producerPreference.getBrokerServer());
        properties.setProperty("security.protocol", producerPreference.getSecurityProto());
        properties.setProperty("sasl.jaas.config", producerPreference.getSaslJaasConfig());
        properties.setProperty("sasl.mechanism", producerPreference.getSaslMechanism());

        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());

        return properties;
    }

}
