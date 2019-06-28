package com.cetc.cameldemo;

import java.util.Map;
import org.apache.camel.builder.RouteBuilder;

/**
 * description:
 *
 * @author yanxf@winning.com.cn
 * @date 2019/06/28 11:26
 */
public class TestRoute extends RouteBuilder {

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
        .to("http://localhost:8080/cjfw-service/query?tableName=aa")
        .when(header("sss").isEqualTo("456"))
        .to("http://localhost:8080/cjfw-service/bb")
        .end()
    ;

  }
}
