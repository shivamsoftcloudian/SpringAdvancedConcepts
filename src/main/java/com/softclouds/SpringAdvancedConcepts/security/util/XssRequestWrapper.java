package com.softclouds.SpringAdvancedConcepts.security.util;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class XssRequestWrapper extends HttpServletRequestWrapper {

    public XssRequestWrapper(HttpServletRequest request) {
        super(request);
    }


    @Override
    public String getRequestURI() {
        String requestURI = super.getRequestURI();
        return XssSanitize.sanitizeInput(requestURI);

    }

    @Override
    public String getQueryString() {
        String queryString = super.getQueryString();
        return XssSanitize.sanitizeInput(queryString);
    }

    @Override
    public String getParameter(String name) {
        String parameter = super.getParameter(name);
        return XssSanitize.sanitizeInput(parameter);
    }

    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null || values.length == 0) {
            return null;
        }
        for (String value : values) {
            XssSanitize.sanitizeInput(value);
        }
        return values;
    }

    @Override
    public String getHeader(String name) {
        String header = super.getHeader(name);
        return XssSanitize.sanitizeInput(header);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        Enumeration<String> headers = super.getHeaders(name);
        String[] values = StringUtils.toStringArray(headers);
        List<String> strippedValues = new ArrayList<>();
        for (String value : values) {
            String strippedValue = XssSanitize.sanitizeInput(value);
            strippedValues.add(strippedValue);
        }
        return Collections.enumeration(strippedValues);

    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        ServletInputStream originalInputStream = super.getInputStream();
        String requestBody = new String(originalInputStream.readAllBytes());

        // Sanitize the JSON body
        String sanitizedBody = XssSanitize.sanitizeInput(requestBody);

        return new ServletInputStream() {

            private final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                    sanitizedBody.getBytes()
            );

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }

            @Override
            public boolean isFinished() {
                return byteArrayInputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
        };
    }

}
