package com.hmall.cart.listener;


import com.hmall.cart.service.ICartService;
import com.hmall.common.utils.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CreateOrderListener {

        private final ICartService cartService;


        @RabbitListener(bindings = @QueueBinding(
                value = @Queue(name = "clear.cart.queue",durable = "true",
                        arguments = @Argument(name = "x-queue-mode", value = "lazy")), //这里设置成了lazyQueue但是3.12之后会自动设置
                exchange = @Exchange(name = "trade.topic"),
                key = "order.create"
        ))
        public void clearCart(@Payload Set<Long> itemIds, @Header(value = "userId") Long userId){
                UserContext.setUser(userId);
                cartService.removeByItemIds(itemIds);
        }
}
