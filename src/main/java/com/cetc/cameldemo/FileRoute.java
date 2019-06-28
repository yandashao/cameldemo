package com.cetc.cameldemo;

import org.apache.camel.builder.RouteBuilder;

public class FileRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:FileRoute")
                .to("log:FileRoute?showExchangeId=true&showProperties=ture&showBody=false");
    }
}
