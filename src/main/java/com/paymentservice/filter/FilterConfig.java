package com.paymentservice.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<RelationIdFilter> relationIdFilter() {
        FilterRegistrationBean<RelationIdFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RelationIdFilter());
        registrationBean.addUrlPatterns("/invoices/*"); // Указываем, на какие пути фильтр будет применяться
        return registrationBean;
    }
}