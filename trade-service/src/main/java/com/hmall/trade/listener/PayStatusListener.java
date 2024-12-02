package com.hmall.trade.listener;

import cn.hutool.core.bean.BeanUtil;
import com.hmall.trade.domain.po.Order;
import com.hmall.trade.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PayStatusListener {

    private final IOrderService orderService;

    //exchangeName : pay.topic
    //queueName : trade.pay.success.queue
    //key : pay.success

    //此时异步调用开启了另一个线程
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("trade.pay.success.queue"),
            exchange = @Exchange("pay.topic"),
            key = {"pay.success"}
    ))
    //这里应该保证消息的幂等性(指的是不论多少次操作产生的效果都是一样的)
    //这里可能有一种极端情况:就是消费者接收到消息之后服务宕机,会导致没有返回ack确认
    //消息队列消息重新发送,消费者进行订单状态修改,但是在服务宕机状态时,用户修改订单状态这里可能导致订单状态异常
    //保证消息的幂等性的
    public void listenPaySuccess(Long orderId){
        Order order = orderService.getById(orderId);
        //这里类似于乐观锁,只要状态发生改变就不会继续消费相同的信息
        if(BeanUtil.isEmpty(order) || order.getStatus() != 1){
            return ;
        }
        orderService.markOrderPaySuccess(orderId);
    }
}
