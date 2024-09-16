package org.sceylan.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class Producer {

    private static final Logger log = LoggerFactory.getLogger(Producer.class.getSimpleName());

    public static void run() {
        // create producer properties
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");// properties.setProperty("key","value");
        log.info("sceylan log");
        // set producer properties
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());
        // create producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        // create a producer record
        ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>("sc_test_topic", "test 1");
        // send data
        for (int i = 0; i < 10; i++) {

            producer.send(producerRecord);
            // Delay for 500 milliseconds
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                log.error("Thread interrupted", e);
            }
        }

        // tell the producer to send all data and block until done --synchronous
        producer.flush();
        // flush and close producer
        producer.close();
    }

}
