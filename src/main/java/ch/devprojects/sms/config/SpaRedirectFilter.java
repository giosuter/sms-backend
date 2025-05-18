package ch.devprojects.sms.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SpaRedirectFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();

        // Only rewrite for HTML routes, not API or static resources
        if (path.startsWith("/sms") &&
            !path.startsWith("/sms-backend") &&
            !path.contains(".") &&
            !path.equals("/sms/")) {
            
            req.getRequestDispatcher("/sms/index.html").forward(req, res);
        } else {
            chain.doFilter(request, response);
        }
    }
}