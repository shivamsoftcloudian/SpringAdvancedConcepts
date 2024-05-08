package com.softclouds.SpringAdvancedConcepts.security.util;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

@Slf4j
public class XssSanitize {

    public static final String XSS_REMOVER_CACHE = "XssRemover_Cache_";
    private static final Pattern SCRIPT_CONTENT = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
    private static final Pattern SRC_CONTENT = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    private static final Pattern SRC_CONTENT_2 = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    private static final Pattern SCRIPT_TAG_CLOSE = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
    private static final Pattern SCRIPT_TAG = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    private static final Pattern EVAL_EXPRESSION = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    private static final Pattern EXPRESSION_EXPRESSION = Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    private static final Pattern JAVASCRIPT_EXPRESSION = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
    private static final Pattern VBSCRIPT_EXPRESSION = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
    private static final Pattern ONLOAD_EXPRESSION = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    private static final Pattern HTML_TAG = Pattern.compile("<[^>]*>");

    public static String sanitizeInput(String value) {
        String originalValue = value;
        if (value == null || value.isEmpty()) {
            return null;
        }

        String inputValue;

//        value = Encode.forHtml(value);

        // Avoid null characters
        value = value.replaceAll("", "");

        inputValue = value;

        // Avoid anything between HTML tags
        value = HTML_TAG.matcher(value).replaceAll("");

        // Avoid anything between script tags
        value = SCRIPT_CONTENT.matcher(value).replaceAll("");

        // Avoid anything in a src='...' type of expression
        value = SRC_CONTENT.matcher(value).replaceAll("");
        value = SRC_CONTENT_2.matcher(value).replaceAll("");

        // Remove any lonesome </script> tag
        value = SCRIPT_TAG_CLOSE.matcher(value).replaceAll("");

        // Remove any lonesome <script ...> tag
        value = SCRIPT_TAG.matcher(value).replaceAll("");

        // Avoid eval(...) expressions
        value = EVAL_EXPRESSION.matcher(value).replaceAll("");

        // Avoid expression(...) expressions
        value = EXPRESSION_EXPRESSION.matcher(value).replaceAll("");

        // Avoid javascript:... expressions
        value = JAVASCRIPT_EXPRESSION.matcher(value).replaceAll("");

        // Avoid vbscript:... expressions
        value = VBSCRIPT_EXPRESSION.matcher(value).replaceAll("");

        // Avoid onload= expressions
        value = ONLOAD_EXPRESSION.matcher(value).replaceAll("");


        if (!inputValue.equals(value)) {
            log.warn("Potential XSS attack prevented!! input: {}, modified to: {}", inputValue, value);
            throw new IllegalArgumentException("Potential XSS attack prevented!! input: " + inputValue + " modified to: " + value);
        } else {
            return originalValue;
        }
    }


}
