package com.example.kafkaopensearchproject.service.consumer;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestHighLevelClient;

import java.net.URI;
import java.util.Properties;

public class ManageConsumerImpl implements ManageConsumer{
    @Override
    public int StartConsumer() {
        return 0;
    }

    @Override
    public int StopConsumer() {
        return 0;
    }

    @Override
    public RestHighLevelClient createOpenSearchClient() {
        String connString = "http://localhost:9200";

        // we build a URI from the connection string
        RestHighLevelClient restHighLevelClient;
        URI connUri = URI.create(connString);
        // extract login information if it exists
        String userInfo = connUri.getUserInfo();

        if (userInfo == null) {
            // REST client without security
            restHighLevelClient = new RestHighLevelClient(RestClient.builder(new HttpHost(connUri.getHost(), connUri.getPort(), "http")));

        } else {
            // REST client with security
            String[] auth = userInfo.split(":");

            CredentialsProvider cp = new BasicCredentialsProvider();
            cp.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(auth[0], auth[1]));

            restHighLevelClient = new RestHighLevelClient(
                    RestClient.builder(new HttpHost(connUri.getHost(), connUri.getPort(), connUri.getScheme()))
                            .setHttpClientConfigCallback(
                                    httpAsyncClientBuilder -> httpAsyncClientBuilder.setDefaultCredentialsProvider(cp)
                                            .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())));


        }

        return restHighLevelClient;
    }

    @Override
    public KafkaConsumer<String, String> createKafkaConsumer() {
        String bootstrapServers = "127.0.0.1:9092";
        String groupId = "consumer-opensearch-demo";

        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers); // bootstrap 서버 정보 . 브로커와 연결

        // consumer 설정 코드 생성
        // properties에 등록
        // Kafka cluster에서 pull한 데이터가 String이기 때문에 , 2진 바이트코드로 바뀐 String 데이터를 다시 StringDeserializer 로 String 화 시킵니다.
        // 당연한 말이지만 , 데이터가 어떤게 들어오느냐에 따라서 해당 값이 달라져야만 합니다.
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());

        // consumer group id 값을 지정해야합니다.
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG,groupId);

        // offset을 어디서부터 pull 할지 설정하는 부분
        // none/earliest/latest 세 옵션값 가능
        // none : 컨슈머 그룹이 설정되지 않으면 동작하지 않음..  application 설정 전 consumer group부터 설정해야 함
        // earliest : --from-beginning 옵션에 해당하는 옵션 . 처음부터 끝까지 다 poll.
        // latest : 가장 최신으로 cluster에 들어간 애를 poll .
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"latest");

        return new KafkaConsumer<>(properties);
    }
}
