package com.yinno.ajax.action;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;

@WebServlet("/find")
public class AutoFind extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder sb =new StringBuilder();
        resp.setContentType("text/html;charset=UTF-8");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/t_ajax?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC","root","abc123");
            if (req.getParameter("type")!=null)
            {
                String result = req.getParameter("result");
                ps = conn.prepareStatement("select result from t_result where result=?");
                ps.setString(1,result);
                rs=ps.executeQuery();
                if (rs.next())
                {
                    resp.getWriter().print("找到结果了");
                }else{
                    resp.getWriter().print("没有找到结果");
                }
                return;
            } else {
                ps = conn.prepareStatement("select result from t_result where result like ?");
                ps.setString(1,req.getParameter("result")+"%");
                rs = ps.executeQuery();
                sb.append("[");
                while(rs.next()){
                    //sb="[{\"result\":result},{},{}]"
                    String result = rs.getString("result");
                    sb.append("{\"result\":\""+result+"\"},");
                }
                String json = sb.subSequence(0,sb.length()-1)+"]";
                resp.getWriter().print(json);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs!=null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps!=null){
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null)
            {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }

    }
}
