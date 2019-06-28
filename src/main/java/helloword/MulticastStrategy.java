package helloword;

import com.alibaba.fastjson.JSON;
import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import java.util.HashMap;
import java.util.Map;

/**
 * 多路并发的消息聚合策略， 多个路由分发时，需要进行消息聚合
 *
 * {
 *     id: 返回
 * }
 * @作者： caopengfei
 * @时间： 2019/5/15
 */
public class MulticastStrategy implements AggregationStrategy {

  @Override
  public Exchange aggregate(Exchange exchange1, Exchange exchange2) {

    Map<String,Object> result = new HashMap<String,Object>();
    if (exchange1 == null) {
      String body1 = exchange2.getIn().getBody(String.class);
      result.put(exchange2.getExchangeId(),body1);
      exchange2.getIn().setBody(JSON.toJSONString(result));
      return exchange2;
    } else {
      String body1 = exchange1.getIn().getBody(String.class);
      Map<String,Object> result2 = JSON.parseObject(body1,Map.class);
      String body2 = exchange2.getIn().getBody(String.class);
      result2.put(exchange2.getExchangeId(),body2);
      String merged = JSON.toJSONString(result2);
      exchange1.getIn().setBody(merged);
      return exchange1;
    }
  }
}
