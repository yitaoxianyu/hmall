package com.hmall.cart.listener;


import com.hmall.cart.service.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CreateOrderListener {

        private final ICartService cartService;


        @RabbitListener(bindings = @QueueBinding(
                value = @Queue(name = "clear.cart.queue",durable = "true"),
                exchange = @Exchange(name = "trade.topic"),
                key = "order.create"
        ))
        public void clearCart(Message message, List<Long> itemIds){
            cartService.removeByItemIds(itemIds);
        }
}
