    package com.hmall.api.config;


    import com.hmall.api.client.fallback.ItemClientFallbackFactory;
    import com.hmall.common.utils.UserContext;
    import feign.RequestInterceptor;
    import feign.RequestTemplate;
    import org.springframework.context.annotation.Bean;

    public class DefaultFeignConfig {
        //这个可以对某一个feign生效或者可以直接添加到启动类中对整个模块生效
        @Bean
        public RequestInterceptor userInfoInterceptor(){
            return new RequestInterceptor() {
                @Override
                public void apply(RequestTemplate requestTemplate) {
                    Long userId = UserContext.getUser();
                    if(userId == null){
                        return ;
                    }
                    requestTemplate.header("user-info",userId.toString());
                }
            };
        }

        @Bean
        public ItemClientFallbackFactory itemClientFallbackFactory(){
            return new ItemClientFallbackFactory();
        }
    }
