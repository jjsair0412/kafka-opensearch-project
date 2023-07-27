package com.example.kafkaopensearchproject.service.consumer;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.opensearch.client.RestHighLevelClient;

public interface ManageConsumer {
    int StartConsumer();
    int StopConsumer();
    RestHighLevelClient createOpenSearchClient();

    KafkaConsumer<String, String> createKafkaConsumer();
}
