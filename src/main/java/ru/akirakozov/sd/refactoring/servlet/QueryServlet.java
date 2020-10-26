package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.dao.Product;
import ru.akirakozov.sd.refactoring.html.ResponseBuilder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        ResponseBuilder responseBuilder = new ResponseBuilder(response);
        if ("max".equals(command)) {
            try {
                try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                    Statement stmt = c.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1");
                    responseBuilder.addHeader("Product with max price: ");

                    List<Product> products = new ArrayList<>();
                    while (rs.next()) {
                        Product product = new Product(rs.getString("name"), rs.getInt("price"));
                        products.add(product);
                    }

                    for (Product product: products) {
                        responseBuilder.addLine(product.getName() + "\t" + product.getPrice());
                    }
                    rs.close();
                    stmt.close();
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if ("min".equals(command)) {
            try {
                try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                    Statement stmt = c.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1");
                    responseBuilder.addHeader("Product with min price: ");

                    List<Product> products = new ArrayList<>();
                    while (rs.next()) {
                        Product product = new Product(rs.getString("name"), rs.getInt("price"));
                        products.add(product);
                    }

                    for (Product product: products) {
                        responseBuilder.addLine(product.getName() + "\t" + product.getPrice());
                    }

                    rs.close();
                    stmt.close();
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if ("sum".equals(command)) {
            try {
                try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                    Statement stmt = c.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT SUM(price) FROM PRODUCT");
                    responseBuilder.addText("Summary price: ");

                    if (rs.next()) {
                        responseBuilder.addText(String.valueOf(rs.getInt(1)));
                    }
                    rs.close();
                    stmt.close();
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if ("count".equals(command)) {
            try {
                try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                    Statement stmt = c.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM PRODUCT");
                    responseBuilder.addText("Number of products: ");

                    if (rs.next()) {
                        responseBuilder.addText(String.valueOf(rs.getInt(1)));
                    }
                    rs.close();
                    stmt.close();
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            response.getWriter().println("Unknown command: " + command);
        }
        responseBuilder.build();
    }

}