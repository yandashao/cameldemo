package com.cetc.cameldemo;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Properties;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
public class CameldemoApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(CameldemoApplication.class, args);

     /*   CamelContext camelContext = new DefaultCamelContext();
        camelContext.start();
        camelContext.addRoutes(new JDBCRoute());
        camelContext.addRoutes(new FileRoute());
        camelContext.addRoutes(new StartRoute());
        camelContext.addRoutes(new TestRoute());

        synchronized (CameldemoApplication.class) {
            CameldemoApplication.class.wait();
        }
*/
    }


}
