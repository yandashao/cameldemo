package helloword;

/**
 * description:
 *
 * @author yanxf@winning.com.cn
 * @date 2019/06/28 9:19
 */

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.common.HttpMessage;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.model.MulticastDefinition;
import org.apache.camel.model.RouteDefinition;

/**
 * 郑重其事的写下 helloworld for Apache Camel
 */
public class APPHelloWorld01 extends RouteBuilder {

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
    camelContext.addRoutes(new APPHelloWorld01());


    // 为了保证主线程不退出
    synchronized (APPHelloWorld01.class) {
      APPHelloWorld01.class.wait();
    }
  }

  @Override
  public void configure() throws Exception {
    String [] uris = {"http://localhost:8080/cjfw-service/query?tableName=aa", "http://localhost:8080/cjfw-service/bb"};

    //格式化路径
    formatterToUri(uris);

    RouteDefinition rd = from("jetty:http://127.0.0.1:8282/doHelloWorld");
    rd.process(new HttpProcessor());

    MulticastDefinition md = rd.multicast() //设置多路并发
        .parallelProcessing()  //设置并行处理
        .aggregationStrategy(new MulticastStrategy());//设置多路并行消息聚合策略
    md.to(uris).end();
  }

  /**
   * 这个处理器用来完成输入的json格式的转换
   *
   * @author yinwenjie
   */
  public class HttpProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
      // 因为很明确消息格式是http的，所以才使用这个类
      // 否则还是建议使用org.apache.camel.Message这个抽象接口
      HttpMessage message = (HttpMessage) exchange.getIn();
      InputStream bodyStream = (InputStream) message.getBody();
      String inputContext = this.analysisMessage(bodyStream);
      bodyStream.close();

      // 存入到exchange的out区域
      if (exchange.getPattern() == ExchangePattern.InOut) {
        Message outMessage = exchange.getOut();
        outMessage.setBody(inputContext + " || out");
      }
    }

    /**
     * 从stream中分析字符串内容
     *
     * @param bodyStream
     * @return
     */
    private String analysisMessage(InputStream bodyStream) throws IOException {
      ByteArrayOutputStream outStream = new ByteArrayOutputStream();
      byte[] contextBytes = new byte[4096];
      int realLen;
      while ((realLen = bodyStream.read(contextBytes, 0, 4096)) != -1) {
        outStream.write(contextBytes, 0, realLen);
      }

      // 返回从Stream中读取的字串
      try {
        return new String(outStream.toByteArray(), "UTF-8");
      } finally {
        outStream.close();
      }
    }
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