package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.dao.Product;
import ru.akirakozov.sd.refactoring.db.DataManager;
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
        DataManager dataManager = new DataManager();
        if ("max".equals(command)) {
            Product product = dataManager.getMax();
            responseBuilder.addLine(product.getName() + "\t" + product.getPrice());
        } else if ("min".equals(command)) {
            Product product = dataManager.getMin();
            responseBuilder.addLine(product.getName() + "\t" + product.getPrice());
        } else if ("sum".equals(command)) {
            int sum = dataManager.getSum();
            responseBuilder.addText(String.valueOf(sum));
        } else if ("count".equals(command)) {
            int count = dataManager.getCount();
            responseBuilder.addText(String.valueOf(count));

        } else {
            response.getWriter().println("Unknown command: " + command);
        }
        responseBuilder.build();
    }

}