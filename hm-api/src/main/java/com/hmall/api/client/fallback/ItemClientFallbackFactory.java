package com.hmall.api.client.fallback;


import com.hmall.api.client.ItemClient;
import com.hmall.api.dto.ItemDTO;
import com.hmall.api.dto.OrderDetailDTO;
import com.hmall.common.utils.CollUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.Collection;
import java.util.List;



//这里配置了feign调用时，超过流控规则设置的线程之后，会触发fallback熔断机制
@Slf4j
public class ItemClientFallbackFactory implements FallbackFactory<ItemClient> {
    @Override
    public ItemClient create(Throwable cause) {
        return new ItemClient() {
            @Override
            public List<ItemDTO> queryItemByIds(Collection<Long> ids) {
                log.error("查询商品失败");

                return CollUtils.emptyList();
            }

            @Override
            public void deductStock(Collection<OrderDetailDTO> items) {
                log.error("扣减商品库存失败");
                throw new RuntimeException(cause);
            }

            @Override
            public void restoreStocks(List<OrderDetailDTO> orderDetailDTOS) {
                log.error("恢复库存失败");
                throw new RuntimeException(cause);
            }
        };
    }
}
