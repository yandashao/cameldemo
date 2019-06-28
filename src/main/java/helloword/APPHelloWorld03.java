package helloword;

/**
 * description:
 *
 * @author yanxf@winning.com.cn
 * @date 2019/06/28 14:02
 */

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.common.HttpMessage;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.model.RouteDefinition;

/**
 * 郑重其事的写下 helloworld for Apache Camel
 */
public class APPHelloWorld03 extends RouteBuilder {

  /**
   * 为什么我们先启动一个Camel服务 再使用addRoutes添加编排好的路由呢？
   * 这是为了告诉各位读者，Apache Camel支持动态加载/卸载编排的路由 这很重要，因为后续设计的Broker需要依赖这种能力
   *
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {

    // 这是camel上下文对象，整个路由的驱动全靠它了。
    ModelCamelContext camelContext = new DefaultCamelContext();
    // 启动route
    camelContext.start();
    // 将我们编排的一个完整消息路由过程，加入到上下文中
    camelContext.addRoutes(new APPHelloWorld03());


    // 为了保证主线程不退出
    synchronized (APPHelloWorld03.class) {
      APPHelloWorld03.class.wait();
    }
  }

  @Override
  public void configure() throws Exception {
    String [] uris = {"http://localhost:8080/cjfw-service/query", "http://localhost:8080/cjfw-service/bb"};

    //格式化路径
    formatterToUri(uris);
    from("jetty:http://localhost:5555/jettyDemo")
        .process(exchange -> {
              System.out.println("进入jetty11..., parms:" + exchange.getIn()
                  .getHeader("sss"));
              exchange.getIn().setHeader("lalala", "ssss");
              Map<String, Object> headers = exchange.getIn().getHeaders();
              System.out.println("header:" + headers.toString());
            }
        )
        .log("jetty log1")
        .setBody(simple("Hello, world! ${date:now:yyyy-MM-dd'T'HH:mm:ssZ}  ${in.headers.sss} ${in.headers.lalala}"))
        .choice()
        .when(header("sss").isEqualTo("123"))
        .to(uris[0])
        .when(header("sss").isEqualTo("456"))
        .to(uris[1])
        .end()
    ;

  }



  protected void formatterToUri(String [] uris){
    for (int i =0;i<uris.length;i++){
      String uri = uris[i];
      if(uri.startsWith("http")){
        uris[i]=  uris[i]+"?bridgeEndpoint=true&httpClient.soTimeout=10000";
      }
    }
  }
}