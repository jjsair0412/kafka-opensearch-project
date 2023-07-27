package com.example.kafkaopensearchproject.service;

import com.example.kafkaopensearchproject.domain.ProducerPreference;
import com.example.kafkaopensearchproject.domain.idStreamData;
import com.example.kafkaopensearchproject.service.createKafkaProperties.CreateProperties;
import com.example.kafkaopensearchproject.service.createKafkaProperties.CreatePropertiesImpl;
import com.example.kafkaopensearchproject.service.producer.handlers.WikimediaChangeHandler;
import com.example.kafkaopensearchproject.service.producer.ManageProducer;
import com.example.kafkaopensearchproject.service.producer.ManageProducerImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.EventSource;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class ProducerTests {


    @Autowired
    private ProducerPreference producerPreference;

    @Test
    public void producerConnectTest(){
        CreateProperties properties = new CreatePropertiesImpl();

        KafkaProducer<String,String> producer = new KafkaProducer<>(properties.CreateProperties(producerPreference));

        ProducerRecord<String, String> producerRecord = new ProducerRecord<>("boot_test","hello message from spring boot project !!");

        producer.send(producerRecord);

        producer.flush();

        producer.close();

    }

    @Test
    public void streamTest() throws IOException {
        String wikiStream = "https://stream.wikimedia.org/v2/stream/recentchange";
        ObjectMapper objectMapper = new ObjectMapper();


        try {
            URL url = new URL(wikiStream);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            Assertions.assertThat(conn.getResponseCode()).isEqualTo(200);

            if (conn.getResponseCode() == 200) {


                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                while (true) {
                    String[] split = reader.readLine().split("\n");

                    for (String line : split) {
                        if (line.startsWith("id: ")) {
                            String jsonStr = line.substring(4);


                            JsonNode jsonNode = objectMapper.readTree(jsonStr);


                            idStreamData[] dataArray = objectMapper.treeToValue(jsonNode, idStreamData[].class);

                            for (idStreamData data : dataArray) {
                                System.out.println("topic : " + data.getTopic());
                                System.out.println("Partition : " + data.getPartition());
                                System.out.println("Timestamp : " + data.getTopic());
                                System.out.println("Offset : " + data.getOffset());

                                System.out.println("done");
                            }

                        }
                    }


                    Thread.sleep(1000);
                }

            } else {
                System.out.println("response fail , code is : "+conn.getResponseCode());
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Test
    public void eventHandlerTest() throws InterruptedException {
        CreateProperties properties = new CreatePropertiesImpl();

        KafkaProducer<String,String> producer = new KafkaProducer<>(properties.CreateProperties(producerPreference));
        String topic = "wikimedia.recentchage.boot";

        EventHandler eventHandler = new WikimediaChangeHandler(producer, topic);
        String url = "https://stream.wikimedia.org/v2/stream/recentchange";

        EventSource.Builder builder = new EventSource.Builder(eventHandler, URI.create(url));
        EventSource eventSource = builder.build();

        eventSource.start();

        // limit 10 min
        TimeUnit.MINUTES.sleep(10);
    }

    @Test
    public void startProducer(){
        ManageProducer manageProducer = new ManageProducerImpl(new CreatePropertiesImpl(),producerPreference);
        int result = manageProducer.StartProducer();
        Assertions.assertThat(result).isEqualTo(1);
    }


}
