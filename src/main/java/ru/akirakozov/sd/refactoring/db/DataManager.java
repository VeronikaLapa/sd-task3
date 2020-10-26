package ru.akirakozov.sd.refactoring.db;

import ru.akirakozov.sd.refactoring.dao.Product;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    static final String DB_URL = "jdbc:sqlite:test.db";

    private enum RequestType {
        PRODUCTS,
        NUM
    }
    private static Object getRequest(String sql, RequestType requestType) {
        try {
            try (Connection c = DriverManager.getConnection(DB_URL)) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                Object result = null;
                switch (requestType) {
                    case PRODUCTS: {
                        List<Product> products = new ArrayList<>();
                        while (rs.next()) {
                            Product product = new Product(rs.getString("name"), rs.getInt("price"));
                            products.add(product);
                        }
                        result = products;
                        break;
                    }
                    case NUM: {
                        result = rs.getInt(1);
                    }
                }

                rs.close();
                stmt.close();

                return result;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static  void updateRequest(String sql) {
        try {
            try (Connection c = DriverManager.getConnection(DB_URL)) {
                Statement stmt = c.createStatement();
                stmt.executeUpdate(sql);
                stmt.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public List<Product> getAll() {
        Object result = getRequest("SELECT * FROM PRODUCT", RequestType.PRODUCTS);
        if (result instanceof List) {
            return (List<Product>) result;
        } else {
            return null;
        }
    }

    public Product getMin() {
        Object result = getRequest("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1", RequestType.PRODUCTS);
        if (result instanceof List) {
            return (Product) ((List) result).get(0);
        } else {
            return null;
        }
    }
    public Product getMax() {
        Object result = getRequest("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1", RequestType.PRODUCTS);
        if (result instanceof List) {
            return (Product) ((List) result).get(0);
        } else {
            return null;
        }
    }
    public int getSum() {
        Object result = getRequest("SELECT SUM(price) FROM PRODUCT", RequestType.NUM);
        if (result instanceof Integer) {
            return ((Integer) result);
        } else {
            return -1;
        }
    }
    public int getCount() {
        Object result = getRequest("SELECT COUNT(*) FROM PRODUCT", RequestType.NUM);
        if (result instanceof Integer) {
            return ((Integer) result);
        } else {
            return -1;
        }
    }
    public void addProduct(Product product) {
        String sql = "INSERT INTO PRODUCT " +
                "(NAME, PRICE) VALUES (\"" + product.getName() + "\"," + product.getPrice() + ")";
        updateRequest(sql);
    }
}
