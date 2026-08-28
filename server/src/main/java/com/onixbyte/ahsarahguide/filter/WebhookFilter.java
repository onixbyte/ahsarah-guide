package com.onixbyte.ahsarahguide.filter;

import com.onixbyte.ahsarahguide.wrapper.RepeatedlyReadRequestWrapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class WebhookFilter implements Filter {
    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) request;
        var wrappedRequest = new RepeatedlyReadRequestWrapper(httpRequest);
        chain.doFilter(wrappedRequest, response);
    }
}
