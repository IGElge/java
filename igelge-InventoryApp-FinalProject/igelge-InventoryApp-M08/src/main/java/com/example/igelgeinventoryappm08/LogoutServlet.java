/**
 * NAME: Isabella Elge
 * CLASS: INFO 1531
 * ASSIGNMENT: Final Project
 * DATE: 11/17/2025
 * RESOURCES: I utilized my previous file with some changes to mirror the coding example
 *
 * PURPOSE: The logout process for users
 */
package com.example.igelgeinventoryappm08;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(urlPatterns = {"/logout"})
public class LogoutServlet extends jakarta.servlet.http.HttpServlet {
//database helper
    private final Database db = new Database();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //gets the current session but doesnt make a new one
        HttpSession s = req.getSession(false);
        String sessionId = null;
       //invalidates the previously found session
        if (s != null) {
            sessionId = s.getId();
            s.invalidate();
        }
//removes the cookies from the browser
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("userSession".equals(c.getName())) {
                    c.setMaxAge(0);
                    c.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
                    resp.addCookie(c);
                }
            }
        }
//removes the session record from the database
        if (sessionId != null) {
            try {
                db.removeSession(sessionId);
            } catch (SQLException e) {
                //prints an error
                e.printStackTrace();
            }
        }
//redirects to the home page after loggin out
        resp.sendRedirect(req.getContextPath() + "/index.jsp");
    }
}
