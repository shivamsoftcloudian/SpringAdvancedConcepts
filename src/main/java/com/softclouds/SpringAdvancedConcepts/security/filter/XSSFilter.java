package com.softclouds.SpringAdvancedConcepts.security.filter;

import com.softclouds.SpringAdvancedConcepts.security.util.XssRequestWrapper;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

@WebFilter("/*")
public class XSSFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        filterChain.doFilter(new XssRequestWrapper((HttpServletRequest) servletRequest), servletResponse);
    }
}
