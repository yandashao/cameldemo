package com.cetc.cameldemo;

import org.apache.camel.builder.RouteBuilder;

public class JDBCRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:JDBCRoute")
                .to("log:JDBCRoute?showExchangeId=true&showProperties=ture&showBody=false");
    }
}
