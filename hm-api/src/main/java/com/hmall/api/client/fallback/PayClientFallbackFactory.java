package com.hmall.api.client.fallback;

import com.hmall.api.client.PayClient;
import org.springframework.cloud.openfeign.FallbackFactory;

public class PayClientFallbackFactory implements FallbackFactory<PayClient> {
    @Override
    public PayClient create(Throwable cause) {
        return null;
    }
}
