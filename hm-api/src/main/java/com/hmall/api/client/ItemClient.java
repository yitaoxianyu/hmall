package com.hmall.api.client;



import com.hmall.api.dto.ItemDTO;
import com.hmall.api.dto.OrderDetailDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@FeignClient(value = "item-service")
public interface ItemClient {

    @GetMapping("/items")
    List<ItemDTO> queryItemByIds(@RequestParam(value = "ids")Collection<Long> ids);

    //扣减库存
    @PutMapping("/items/stock/deduct")
    void deductStock(@RequestBody Collection<OrderDetailDTO> items);

    @PutMapping("/items/stock/restore")
    void restoreStocks(List<OrderDetailDTO> orderDetailDTOS);
}
