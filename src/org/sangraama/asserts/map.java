package org.sangraama.asserts;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

@WebServlet("/org/sangraama/asserts/maps")
public class map extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("application/json;charset=utf-8");
    PrintWriter out = response.getWriter();
    // out.println("Hello gihan");
    System.out.println(request.getParameter("name"));

    Gson gson = new Gson();
    int[] ints = { 1, 2, 3, 4, 5 };
    String[] strings = { "abc", "def", "ghi" };
    PlayerData p = new PlayerData(0,0);
    p.setX(1);
    p.setY(2);
    // out.print(gson.toJson(ints));
    out.print(gson.toJson(p));
    out.close();
    System.out.println("Executed the doGet method");
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    resp.setContentType("application/json;charset=utf-8");
    PrintWriter out = resp.getWriter();
    // out.println("Hello gihan");
    System.out.println(req.getParameter("name"));

    Gson gson = new Gson();
    int[] ints = { 1, 2, 3, 4, 5 };
    String[] strings = { "abc", "def", "ghi" };
    String str = "{ \"name\": \"chanu\"} ";
    // out.print(gson.toJson(ints));
    out.print(gson.toJson(str));
    out.close();
    System.out.println("Executed the doGet method");
  }

}
