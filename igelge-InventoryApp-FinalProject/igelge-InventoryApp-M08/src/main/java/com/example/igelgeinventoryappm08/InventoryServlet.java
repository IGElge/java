/**
 * NAME: Isabella Elge
 * CLASS: INFO 1531
 * ASSIGNMENT: Final Project
 * DATE: 11/17/2025
 * RESOURCES: I utilized the coding example version of this file
 *
 * PURPOSE: Carries out the different actions a user can take, as well as specifies which users can do what
 * when it comes to those actions
 *
 * To test this below are logins seeded in the database
 * admin - admin123
 * manager - manager123
 * user - user123
 */
package com.example.igelgeinventoryappm08;

//many many more imports
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

//starts with a /inventory route
@WebServlet("/inventory")
@MultipartConfig
//declares the public class
public class InventoryServlet extends jakarta.servlet.http.HttpServlet {
    private final Database db = new Database();
//sets the options
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //gets the action from the URL
        String action = req.getParameter("action");
        if (action == null) action = "list";

        try {
            //choses what to do based on the previously discovered action
            switch (action) {
                case "view":
                    showView(req, resp);
                    break;
                case "create":
                    showCreate(req, resp);
                    break;
                case "delete":
                    doDeleteItem(req, resp);
                    break;
                case "filter":
                    doFilter(req, resp);
                    break;
                case "search":
                    doSearch(req, resp);
                    break;
                default:
                    listItems(req, resp);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Database error: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //gets actions for post actions
        String action = req.getParameter("action");

        try {
            //runs each of these items based on the previously discovered action
            if ("create".equals(action)) {
                createItem(req, resp);
            } else if ("updateInventory".equals(action)) {
                updateInventory(req, resp);
            } else if ("updateItem".equals(action)) {
                updateItem(req, resp);
            } else {
                resp.sendRedirect(req.getContextPath() + "/inventory");
            }
        } catch (SQLException e) {
            throw new ServletException("Database error during POST: " + e.getMessage(), e);
        }
    }

    private void listItems(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        //loads all items from the db
        Map<String, Item> items = db.getAllItems();
        //sends the data to the corresponding jsp
        req.setAttribute("items", items.values());
        req.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(req, resp);
    }

    private void showView(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        //gets the item id from the url
        String id = req.getParameter("itemID");
//if no id it returns null
        if (id == null || id.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/inventory");
            return;
        }
//if item it pulls it from the db
        Item item = db.getItemById(id);
        if (item == null) {
            resp.sendRedirect(req.getContextPath() + "/inventory");
            return;
        }
//finally sends the item to the jsp
        req.setAttribute("item", item);
        req.getRequestDispatcher("/WEB-INF/jsp/view.jsp").forward(req, resp);
    }

    private void showCreate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //a form to create a new item
        req.getRequestDispatcher("/WEB-INF/jsp/create.jsp").forward(req, resp);
    }

    private void createItem(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, ServletException {
        //generates the ID for the new item
        String id = UUID.randomUUID().toString();
        //read the user input fields
        String name = req.getParameter("name");
        String manufacturer = req.getParameter("manufacturer");

        if (name == null || manufacturer == null) {
            throw new ServletException("Missing required fields when creating item.");
        }

        double price = Double.parseDouble(req.getParameter("price"));
        int inventory = Integer.parseInt(req.getParameter("inventory"));
        ItemType type = ItemType.valueOf(req.getParameter("type"));

        //builds a new item
        Item item = new Item(id, name, manufacturer, price, inventory, type);
//handles the image upload
        Part imagePart = req.getPart("imageFile");
        if (imagePart != null && imagePart.getSize() > 0) {
            Image img = processImage(imagePart);
            item.setImage(img);
        }
//saves the item to the db and shows the new item
        db.createItem(item);
        resp.sendRedirect(req.getContextPath() + "/inventory?action=view&itemID=" + id);
    }
//processes the image
    private Image processImage(Part file) throws IOException {
        try (InputStream in = file.getInputStream(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buf = new byte[4096];
            int r;

            while ((r = in.read(buf)) != -1) {
                out.write(buf, 0, r);
            }

            Image img = new Image();
            img.setName(file.getSubmittedFileName());
            img.setContents(out.toByteArray());
            return img;
        }
    }

    private void doDeleteItem(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
       //ensures the user is logged in
        HttpSession s = req.getSession(false);
        User u = (s == null) ? null : (User) s.getAttribute("user");
        //if the user is not logged it, it redirects
        if (u == null) {
            resp.sendRedirect(req.getContextPath() + "/inventory");
            return;
        }
//ensures only managers and admins can delete
        if (u.getRole() != UserType.MANAGER && u.getRole() != UserType.ADMIN) {
            resp.sendRedirect(req.getContextPath() + "/inventory");
            return;
        }
//deletes the item after role has been verified
        String id = req.getParameter("itemID");
        if (id != null) {
            db.deleteItem(id);
        }

        resp.sendRedirect(req.getContextPath() + "/inventory");
    }

    private void doSearch(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        //gets a search from the user
        String q = req.getParameter("query");
        //search action by name
        Map<String, Item> found = db.searchItemsByName(q == null ? "" : q);

        req.setAttribute("items", found.values());
        req.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(req, resp);
    }

    private void doFilter(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
       //get searchable type
        String typeStr = req.getParameter("type");
//if the type isnt found it redirects
        if (typeStr == null || typeStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/inventory");
            return;
        }
//filters by type
        ItemType type = ItemType.valueOf(typeStr);
        Map<String, Item> found = db.filterItemsByType(type);

        req.setAttribute("items", found.values());
        req.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(req, resp);
    }

    private void updateInventory(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        //gets the item id for the item user wants to update
        String id = req.getParameter("itemID");
        if (id != null) {
            int newInventory = Integer.parseInt(req.getParameter("inventory"));
            Item item = db.getItemById(id);

            if (item != null) {
                item.setInventory(newInventory);
                db.updateItem(item);
            }
        }

        resp.sendRedirect(req.getContextPath() + "/inventory?action=view&itemID=" + id);
    }

    private void updateItem(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, ServletException {
        HttpSession s = req.getSession(false);
        User u = (s == null) ? null : (User) s.getAttribute("user");

        // checks if the user is a manager of admin
        if (u == null || (u.getRole() != UserType.MANAGER && u.getRole() != UserType.ADMIN)) {
            resp.sendRedirect(req.getContextPath() + "/inventory");
            return;
        }
//gets the item by id
        String id = req.getParameter("itemID");
        Item existing = db.getItemById(id);

        if (existing == null) {
            resp.sendRedirect(req.getContextPath() + "/inventory");
            return;
        }
//change via their attributes
        existing.setName(req.getParameter("name"));
        existing.setManufacturer(req.getParameter("manufacturer"));
        existing.setPrice(Double.parseDouble(req.getParameter("price")));
        existing.setInventory(Integer.parseInt(req.getParameter("inventory")));
        existing.setType(ItemType.valueOf(req.getParameter("type")));

        Part imagePart = req.getPart("imageFile");
        if (imagePart != null && imagePart.getSize() > 0) {
            Image img = processImage(imagePart);
            existing.setImage(img);
        }
//redirects to view item
        db.updateItem(existing);
        resp.sendRedirect(req.getContextPath() + "/inventory?action=view&itemID=" + id);
    }
}
