package org.sceylan.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class Consumer {

    private static final Logger log = LoggerFactory.getLogger(Consumer.class.getSimpleName());

    public static void run() {
        String groupId = "sc-java-app";
        String topic = "sc";
        // create consumer properties
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");// properties.setProperty("key","value");
        log.info("sceylan log");
        // set consumer properties
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", StringDeserializer.class.getName());
        properties.setProperty("group.id", groupId);
        properties.setProperty("auto.offset.reset", "earliest");
       
        // create consumer
        KafkaConsumer<String,String> consumer = 
            new KafkaConsumer<>(properties);

        //subscribe topic
        consumer.subscribe(Arrays.asList(topic));

        //poll for data
        while (true) {
            log.info("poling");
            ConsumerRecords<String,String> records = 
                consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String,String> r:records) {
                log.info("Key" +r.key()+",Value:"+r.value());
                log.info("Partition" +r.partition()+",Offset:"+r.offset());
            }
        }
       
    }

}
 