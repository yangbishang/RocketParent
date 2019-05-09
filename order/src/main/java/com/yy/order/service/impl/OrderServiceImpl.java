package com.yy.order.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;

import com.yy.order.constants.OrderStatus;
import com.yy.order.entity.Order;
import com.yy.order.mapper.OrderMapper;
import com.yy.order.service.OrderService;
import com.yy.store.service.api.StoreServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderServiceImpl implements OrderService{

	@Autowired
	private OrderMapper orderMapper;
	
    @Reference(version = "1.0.0",
              application = "${dubbo.application.id}",
              interfaceName = "com.bfxy.store.service.StoreServiceApi",
              check = false,
              timeout = 3000,
              retries = 0
    )
	private StoreServiceApi storeServiceApi;

	
	@Override
	public boolean createOrder(String cityId, String platformId, String userId, String supplierId, String goodsId) {
		boolean flag = true;
		try {
			Order order = new Order();
			order.setOrderId(UUID.randomUUID().toString().substring(0, 32)); //订单号
			order.setOrderType("1");   //订单的类型
			order.setCityId(cityId);
			order.setPlatformId(platformId);
			order.setUserId(userId);
			order.setSupplierId(supplierId);
			order.setGoodsId(goodsId);
			order.setOrderStatus(OrderStatus.ORDER_CREATED.getValue());   //订单的状态
			order.setRemark("");
			
			Date currentTime = new Date();
			order.setCreateBy("admin");    //创建人
			order.setCreateTime(currentTime);  //时间
			order.setUpdateBy("admin");
			order.setUpdateTime(currentTime);

			// 乐观锁更新
			// 查询当前的版本号
			int currentVersion = storeServiceApi.selectVersion(supplierId, goodsId);
			// 更新库存
			int updateRetCount = storeServiceApi.updateStoreCountByVersion(currentVersion, supplierId, goodsId, "admin", currentTime);
			
			if(updateRetCount == 1) {
				// DOTO:	如果出现SQL异常入库失败,那么要对"库存的数量"和"版本号"进行回滚操作
				orderMapper.insertSelective(order);  //入库
			}
			else if (updateRetCount == 0){    //	没有更新成功 1 高并发时乐观锁生效   2 库存不足
				flag = false;	//	下单标识失败
				int currentStoreCount = storeServiceApi.selectStoreCount(supplierId, goodsId); //查询库存
				if(currentStoreCount == 0) {
					//{flag:false , messageCode: 003 , message: 当前库存不足}  -----返回给客户(实际操作)
					System.err.println("-----当前库存不足...");
				} else {
					//{flag:false , messageCode: 004 , message: 乐观锁生效}  -----再来一次（实际操作）
					System.err.println("-----乐观锁生效...");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 	具体捕获的异常是什么异常
			flag = false;
		}
		
		return flag;
	}

//	public static final String PKG_TOPIC = "pkg_topic";
//
//	public static final String PKG_TAGS = "pkg";
//	@Override
//	public void sendOrderlyMessage4Pkg(String userId, String orderId) {
//		List<Message> messageList = new ArrayList<>();
//
//		Map<String, Object> param1 = new HashMap<String, Object>();
//		param1.put("userId", userId);
//		param1.put("orderId", orderId);
//		param1.put("text", "创建包裹操作---1");
//
//		String key1 = UUID.randomUUID().toString() + "$" +System.currentTimeMillis();
//		Message message1 = new Message(PKG_TOPIC, PKG_TAGS, key1, FastJsonConvertUtil.convertObjectToJSON(param1).getBytes());
//
//		messageList.add(message1);
//
//
//		Map<String, Object> param2 = new HashMap<String, Object>();
//		param2.put("userId", userId);
//		param2.put("orderId", orderId);
//		param2.put("text", "发送物流通知操作---2");
//
//		String key2 = UUID.randomUUID().toString() + "$" +System.currentTimeMillis();
//		Message message2 = new Message(PKG_TOPIC, PKG_TAGS, key2, FastJsonConvertUtil.convertObjectToJSON(param2).getBytes());
//
//		messageList.add(message2);
//
//		//	顺序消息投递 是应该按照 供应商ID 与topic 和 messagequeueId 进行绑定对应的
//		//  supplier_id
//
//		Order order = orderMapper.selectByPrimaryKey(orderId);
//		int messageQueueNumber = Integer.parseInt(order.getSupplierId());
//
//		//对应的顺序消息的生产者 把messageList 发出去
//		orderlyProducer.sendOrderlyMessages(messageList, messageQueueNumber);
//	}

	
}
