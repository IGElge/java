/***
 * NAME:Isabella Elge
 * CLASS: INFO 1531
 * ASSIGNMENT: Final Project
 * DATE: 11/17/2025
 * RESOURCES: Previous lecture, slides, and coding examples
 *
 * PURPOSE: This file creates the authentication filter for the application
 */
package com.example.igelgeinventoryappm08;

//an excessive amount of imports
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;
import java.util.Objects;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
//nothing to go here, just has to exist
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
//cast requests so that they can be used later in the code
        HttpServletRequest req  = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
//gets the path to my webapp
        String contextPath = req.getContextPath();
        //gets the login request
        String path = req.getRequestURI().substring(contextPath.length());

        // Actions that anyone (logged in or not) should be able to access
        boolean isPublicPath =
                path.equals("/index.jsp")
                        || path.equals("/login")
                        || path.equals("/login.jsp")
                        || path.startsWith("/images/")
                        || path.startsWith("/css/")
                        || path.startsWith("/js/")
                        // Allow listing/searching inventory without login
                        || (path.startsWith("/inventory") &&
                        ("list".equals(req.getParameter("action")) || "search".equals(req.getParameter("action"))));

        //if the path is declared above there is no authentication needed
        if (isPublicPath) {
            chain.doFilter(request, response);
            return;
        }
//try to get the current session and dont create one if it doesnt exist
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(contextPath + "/login.jsp");
            return;
        }

//when the user is authenticated the request can be processed
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        //no code needed here since I am not using this in my code :)
    }
}
