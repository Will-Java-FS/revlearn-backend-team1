package com.revlearn.team1.config;

import com.revlearn.team1.logging.LoggerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.lang.NonNull;

@Configuration
public class LoggerInterceptorConfig implements WebMvcConfigurer
{
    @Autowired
    LoggerInterceptor loggerInterceptor;

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry)
    {
        // Makes it so that the loggerInterceptor is used for all requests (URI's)
        registry.addInterceptor(loggerInterceptor);
    }
}
