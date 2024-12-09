package com.paymentservice.config;

import com.paymentservice.filter.RelationIdFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<RelationIdFilter> relationIdFilter() {
        FilterRegistrationBean<RelationIdFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RelationIdFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("RelationIdFilter");
        registrationBean.setOrder(1);
        return registrationBean;
    }

}
