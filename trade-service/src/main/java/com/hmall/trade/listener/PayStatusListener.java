package com.hmall.trade.listener;

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
    public void listenPaySuccess(Long orderId){
        orderService.markOrderPaySuccess(orderId);
    }
}
