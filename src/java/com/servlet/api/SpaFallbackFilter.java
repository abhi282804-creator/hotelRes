package com.servlet.api;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Serves the React entry point only for known client-side routes. API, JSP,
 * servlet, and static-resource requests always continue through Tomcat.
 */
@WebFilter("/*")
public class SpaFallbackFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
        // No configuration required.
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        if (("GET".equalsIgnoreCase(httpRequest.getMethod()) || "HEAD".equalsIgnoreCase(httpRequest.getMethod()))
                && isReactRoute(path)) {
            httpResponse.setCharacterEncoding("UTF-8");
            httpRequest.getRequestDispatcher("/index.html").forward(request, response);
            return;
        }
        chain.doFilter(request, response);
    }

    private boolean isReactRoute(String path) {
        if (path.startsWith("/api/")) {
            return false;
        }
        return "/login".equals(path)
                || "/signup".equals(path)
                || "/contact".equals(path)
                || "/reservations".equals(path)
                || "/reservation/confirm".equals(path)
                || "/payment".equals(path)
                || "/payment/status".equals(path)
                || path.matches("/hotels/[^/]+")
                || "/admin".equals(path)
                || path.startsWith("/admin/");
    }

    @Override
    public void destroy() {
        // Nothing to release.
    }
}
