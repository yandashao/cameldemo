/*
package com.cetc.cameldemo;

import groovy.util.logging.Slf4j;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

*/
/**
 * description:
 *
 * @author yanxf@winning.com.cn
 * @date 2019/06/28 13:54
 *//*

@RestController
@Slf4j
public class TestController {

  @ResponseBody
  @RequestMapping("/query")
  public Map map(@RequestParam String tableName) {
    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    HashMap hashMap = new LinkedHashMap();
    System.out.println("query");
    hashMap.put("code", 200);
    hashMap.put("rows", 3);
    return hashMap;
  }


  @ResponseBody
  @RequestMapping("/bb")
  public Map mapss() {

    System.out.println("===========================================================");
    HashMap hashMap = new LinkedHashMap();
    hashMap.put("code", 500);
    hashMap.put("bbbbb", null);
    return hashMap;
  }
}
*/
