package ru.akirakozov.sd.refactoring.html;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseBuilder {
    private HttpServletResponse response;

    public ResponseBuilder(HttpServletResponse response) throws IOException {
        this.response = response;
        response.setContentType("text/html");
        response.getWriter().println("<html><body>");
    }

    public void addHeader(String header) throws IOException {
        response.getWriter().println("<h1>" + header + "</h1>");
    }

    public void addLine(String text) throws IOException {
        response.getWriter().println(text + "</br>");
    }
    public void addText(String text) throws IOException {
        response.getWriter().println(text);
    }
    private void end() throws IOException {
        response.getWriter().println("</body></html>");
    }

    public void build() throws IOException {

        this.end();
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);

    }

}
