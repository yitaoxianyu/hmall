package com.hmall.getway.filter;


import cn.hutool.core.text.AntPathMatcher;
import com.hmall.common.exception.UnauthorizedException;
import com.hmall.common.utils.CollUtils;
import com.hmall.getway.config.AuthProperties;
import com.hmall.getway.utils.JwtTool;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@EnableConfigurationProperties(AuthProperties.class)
@RequiredArgsConstructor
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private final JwtTool jwtTool;

    private final AuthProperties authProperties;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if(isExclude(request.getPath().toString())){
            return chain.filter(exchange);
        }

        String token = null;
        List<String> list = request.getHeaders().get("Authorization");
        if (!CollUtils.isEmpty(list)) {
            token = list.get(0);
        }

        Long userId = null;
        try{
            userId = jwtTool.parseToken(token);
        }catch (UnauthorizedException unauthorizedException){
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        String userInfo = userId.toString();

        return chain.filter(exchange.mutate().request(builder -> builder.header("user-info",userInfo)).build());
    }

    private boolean isExclude(String antPath){
        for (String excludePath : authProperties.getExcludePaths()) {
            if(antPathMatcher.match(excludePath,antPath)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
