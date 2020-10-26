package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.html.ResponseBuilder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {


    private enum QueryType {
        MAX("max"),
        MIN("min"),
        SUM("sum"),
        COUNT("count");

        private String command;

        QueryType(String command) {
            this.command = command;
        }

        public static QueryType fromString(String command) {
            for (QueryType qt : QueryType.values()) {
                if (qt.command.equalsIgnoreCase(command)) {
                    return qt;
                }
            }
            return null;
        }

        public String getSql() {
            switch (this) {
                case SUM:
                    return "SELECT SUM(price) FROM PRODUCT";
                case MAX:
                    return "SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1";
                case MIN:
                    return "SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1";
                case COUNT:
                    return "SELECT COUNT(*) FROM PRODUCT";
            }
            return null;
        }

        public String getInfo() {
            switch (this) {
                case SUM:
                    return "Summary price: ";
                case MAX:
                    return "<h1>Product with max price: </h1>";
                case MIN:
                    return "<h1>Product with min price: </h1>";
                case COUNT:
                    return "Number of products: ";
            }
            return null;
        }

        public void writeResponse(HttpServletResponse response, ResultSet rs) throws SQLException, IOException {
            switch (this) {
                case MAX:
                case MIN: {
                    String name = rs.getString("name");
                    int price = rs.getInt("price");
                    response.getWriter().println(name + "\t" + price + "</br>");
                    break;
                }
                case SUM:
                case COUNT: {
                    response.getWriter().println(rs.getInt(1));
                    break;
                }

            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");
        QueryType queryType = QueryType.fromString(command);
        ResponseBuilder responseBuilder = new ResponseBuilder(response);
        if (queryType != null) {
            try {
                try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                    Statement stmt = c.createStatement();
                    ResultSet rs = stmt.executeQuery(queryType.getSql());
                    responseBuilder.addText(queryType.getInfo());

                    while (rs.next()) {
                        queryType.writeResponse(response, rs);
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
