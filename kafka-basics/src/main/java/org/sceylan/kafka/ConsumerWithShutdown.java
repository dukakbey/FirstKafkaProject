package org.sceylan.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerWithShutdown {

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
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        // get the referance to main thread
        final Thread mainThread = Thread.currentThread();

        // adding shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                log.info("detected a shutdown, let's exit by calling consumer.wakeup()... ");
                consumer.wakeup();

                // join the main thread to allow the execution of the code in the main thread
                try {
                    mainThread.join();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        });
        try {

            // subscribe topic
            consumer.subscribe(Arrays.asList(topic));

            // poll for data
            while (true) {
                log.info("poling");
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> r : records) {
                    log.info("Key" + r.key() + ",Value:" + r.value());
                    log.info("Partition" + r.partition() + ",Offset:" + r.offset());
                }
            }

        } catch (WakeupException e) {
            log.info("consumer is starting to shutdown");
        }
        catch(Exception e)
        {
            log.info("unexpected exception in the consumer ",e);
        }
        finally
        {
            consumer.close();
            log.info("the consumer is now gracefully shutdown.")
        }

    }

}
