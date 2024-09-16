
package org.sceylan.kafka;

import java.util.Properties;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerWithKeys {
    // create logger
    private static final Logger log = LoggerFactory.getLogger(ProducerWithKeys.class.getSimpleName());
    // create producer properties

    public static void run() {
        // set producer properties
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());

        properties.setProperty("batch.size", "400");

        // create producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < 10; i++) {
                String topic = "sc";
                String key = "id_ "+i;
                String value = "hello "+i;
                // create a producer record
                ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(topic, key,value);
                // send data
                producer.send(producerRecord, new Callback() {
    
                    @Override
                    public void onCompletion(RecordMetadata metadata, Exception e) {
                        if (e == null) {
                            log.info("received new metadata\n" +
                                    "Key: " + key + "\n" +
                                    "Partition: " + metadata.partition() + "\n"                             
    
                            );
                        }
                    }
    
                });
            }
        }
        

        // flush and close producer
        producer.flush();
        producer.close();
    }

}
