package com.example.demospringsecurityform.common;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.GenericFilterBean;

@Slf4j
public class LoggingFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        var stopWatch = new StopWatch();

        stopWatch.start(((HttpServletRequest) request).getRequestURI());
        chain.doFilter(request, response);
        stopWatch.stop();

        log.info(stopWatch.prettyPrint());
    }
}
