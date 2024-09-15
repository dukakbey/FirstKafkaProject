package org.sceylan.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class Producer {

    private static final Logger log = LoggerFactory.getLogger(Producer.class.getSimpleName());

    public static void run()
    {
        //create producer properties
        Properties properties = new Properties();
        //properties.setProperty("key","value");
        properties.setProperty("bootstrap.servers","127.0.0.1:9092");


        //create producer
        //send data
        //flush and close the producer
    }

}
