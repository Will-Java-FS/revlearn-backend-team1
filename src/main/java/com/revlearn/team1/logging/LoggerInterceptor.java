package com.revlearn.team1.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoggerInterceptor implements HandlerInterceptor
{
    // Singular logger instance for the entire application
    Logger logger = LoggerFactory.getLogger(LoggerInterceptor.class);

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull Object handler) throws Exception
    {
        try
        {
            // Log the request method and URI
            logger.info("Request: " + request.getMethod() + " " + request.getRequestURI());
        }
        catch (Exception e)
        {
            logger.error("Error logging request: " + e.getMessage(), e);
        }

        return true;
    }

    @Override
    public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull Object handler, @Nullable ModelAndView modelAndView) throws Exception
    {
        // Further configuration could be added here if wanted
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull Object handler, @Nullable Exception ex) throws Exception
    {
        try
        {
            // Log the response status code
            logger.info("Response: " + response.getStatus());
        }
        catch (Exception e)
        {
            // Whatever error occurred
            logger.error("Error logging response: " + e.getMessage(), e);
        }
    }
}