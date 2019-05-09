package com.yy.order.web;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.yy.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;
    //	超时降级
//	@HystrixCommand(
//				commandKey = "createOrder",
//				commandProperties = {
//						@HystrixProperty(name="execution.timeout.enabled", value="true"),
//						@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="3000"),
//				},
//				fallbackMethod = "createOrderFallbackMethod4Timeout"   //降级调用的方法名
//			)
//


    //	限流策略：线程池方式
	@HystrixCommand(
				commandKey = "createOrder",
				commandProperties = {
						@HystrixProperty(name="execution.isolation.strategy", value="THREAD")
				},
				threadPoolKey = "createOrderThreadPool",
				threadPoolProperties = {
						@HystrixProperty(name="coreSize", value="10"),  //核心线程数，初始化时，允许有十个线程的连接
						@HystrixProperty(name="maxQueueSize", value="20000"), //最大队列数
						@HystrixProperty(name="queueSizeRejectionThreshold", value="30") // 这个是可以动态调节的
				},                                            // 如果发了41个消息后，十个线程的最初任务还没完成，就调用失败方法
				fallbackMethod="createOrderFallbackMethod4Thread"
			)
    @RequestMapping("/createOrder")
    public String createOrder(@RequestParam("cityId")String cityId,
                              @RequestParam("platformId")String platformId,
                              @RequestParam("userId")String userId,
                              @RequestParam("supplierId")String supplierId,
                              @RequestParam("goodsId")String goodsId) throws Exception{
        return orderService.createOrder(cityId, platformId, userId, supplierId, goodsId) ? "下单成功!" : "下单失败!";
    }
    public String createOrderFallbackMethod4Thread(@RequestParam("cityId")String cityId,
                                                   @RequestParam("platformId")String platformId,
                                                   @RequestParam("userId")String userId,
                                                   @RequestParam("suppliedId")String suppliedId,
                                                   @RequestParam("goodsId")String goodsId) throws Exception {
        System.err.println("-------线程池限流降级策略执行------------");
        return "hysrtix threadpool !";
    }



    //降级的方法一定要与原来的方法一模一样（返回值，函数参数，），除了方法名
    public String createOrderFallbackMethod4Timeout(@RequestParam("cityId")String cityId,
                                                    @RequestParam("platformId")String platformId,
                                                    @RequestParam("userId")String userId,
                                                    @RequestParam("suppliedId")String suppliedId,
                                                    @RequestParam("goodsId")String goodsId) throws Exception {
        System.err.println("-------超时降级策略执行------------");
        return "hysrtix timeout !";
    }



    public String createOrderFallbackMethod4semaphore(@RequestParam("cityId")String cityId,
                                                      @RequestParam("platformId")String platformId,
                                                      @RequestParam("userId")String userId,
                                                      @RequestParam("suppliedId")String suppliedId,
                                                      @RequestParam("goodsId")String goodsId) throws Exception {
        System.err.println("-------信号量限流降级策略执行------------");
        return "hysrtix semaphore !";
    }
}
