package com.cetc.cameldemo;

import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class StartRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("jetty:http://localhost:5555/jettyDemo")
                .dynamicRouter().method(this, "doDirect")
                .process(exchange -> {
                            System.out.println("进入jetty..., parms:" + exchange.getIn()
                                    .getHeader("sss"));
                            exchange.getIn().setHeader("lalala", "ssss");
                    Map<String, Object> headers = exchange.getIn().getHeaders();
                    System.out.println("header:" + headers.toString());
                    }
                )
                .log("jetty log test ")
                .setBody(simple("Hello, world! ${date:now:yyyy-MM-dd'T'HH:mm:ssZ}  ${in.headers.sss} ${in.headers.lalala}"))
                .choice()
                .when(header("sss").isEqualTo("123"))
                .to("direct:JDBCRoute")
                .when(header("sss").isEqualTo("456"))
                .to("direct:FileRoute")
                .log("${body.length} test")
                .loopDoWhile(simple("${body.length} <= 50"))
                .log("loop while ++")
                .transform(body().append("A"))
                .end()
        ;


    }



    public String doDirect(@ExchangeProperties Map<String, Object> properties,@Headers Map<String, Object> test) {
        // 在Exchange的properties属性中，取出Dynamic Router的循环次数
        AtomicInteger time = (AtomicInteger) properties.get("time");
        test.put("testdodirect","111");
        if (time == null) {
            time = new AtomicInteger(0);
            properties.put("time", time);
        }
        //  LOGGER.info("这是Dynamic Router循环第：【" + time.incrementAndGet() + "】次执行！执行线程：" + Thread.currentThread().getName());
        System.out.println("=====这是Dynamic Router循环第：【" + time.incrementAndGet() + "】次执行！执行线程：" + Thread.currentThread().getName());
        // 第一次选择FileRoute
        if (time.get() == 1) {
            return "direct:FileRoute";
        }
        // 第二次选择JDBCRoute
        else if (time.get() == 2) {
            return "direct:JDBCRoute";
        }
        // 第三次选择一个Log4j-Endpoint执行
        else if (time.get() == 3) {
            return "log:StartRoute?showExchangeId=true&showProperties=ture&showBody=false";
        }
        // 其它情况返回null，终止 dynamicRouter的执行
        return null;
    }

}
