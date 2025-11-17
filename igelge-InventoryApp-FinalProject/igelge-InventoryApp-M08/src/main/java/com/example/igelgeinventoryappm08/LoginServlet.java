/**
 * NAME: Isabella Elge
 * CLASS: INFO 1531
 * ASSIGNMENT: Final Project
 * DATE: 11/17/2025
 * RESOURCES: I utilized my previous file with some changes to mirror the coding example
 *
 * PURPOSE: The login process for users
 */
package com.example.igelgeinventoryappm08;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

@WebServlet(urlPatterns = {"/login"})
public class LoginServlet extends jakarta.servlet.http.HttpServlet {
//database helper
    private final Database db = new Database();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //shows login page
        req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //gets username and password from the user
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try {
            //checks against the database to authenticate the user
            User found = db.authenticateUser(username, password);
            if (found != null) {
                //creates a session for the user (depends on the user type for its abilities
                HttpSession session = req.getSession();
                session.setAttribute("user", found);
                req.changeSessionId(); //ensures the session id changes for security purposes

                // creates a session cookie
                String sessionId = session.getId();
                db.createSession(found.getUsername(), sessionId);

                Cookie cookie = new Cookie("userSession", sessionId);
                cookie.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
                cookie.setMaxAge(60 * 60 * 24); //the cookie lasts for 1 day, another security thing
                resp.addCookie(cookie);

                //redirects to the home page
                resp.sendRedirect(req.getContextPath() + "/index.jsp");
                return;
            } else {
                //if the login failed itll show the login failed screen
                req.setAttribute("loginFailed", true);
                req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
            }
        } catch (SQLException e) {
            //mainly here for database issues
            throw new ServletException(e);
        }
    }
}
