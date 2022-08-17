package com.example.in2out.mapper.impl;

import com.example.in2out.entity.OuterXmlInfoCache;
import com.example.in2out.mapper.OuterXmlMapper;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class OuterXmlMapperImpl implements OuterXmlMapper {
    String url = "jdbc:mysql://localhost:3306/gateway-test?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
    String username = "root";
    String password = "123456";
    Statement statement;
    Connection connection;
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public OuterXmlInfoCache getOuterXml(String host, int port) throws SQLException {
        String sql = "select * from t_info where host = '" + host+ "' and port ='"+ port +"'";  // 语句sql
        ResultSet result = statement.executeQuery(sql);
        OuterXmlInfoCache cache = new OuterXmlInfoCache();
        cache.setHost(host);
        cache.setPort(port);
        while (result.next()) {
            cache.setId(result.getInt("ID"));
            cache.setInfo(result.getString("INFO"));
            break;
        }
        return cache;
    }

    @Override
    public void insertOuterXml(OuterXmlInfoCache outerXmlInfoCache) throws SQLException {
        int id = outerXmlInfoCache.getId();
        String host = outerXmlInfoCache.getHost();
        int port = outerXmlInfoCache.getPort();
        String info = outerXmlInfoCache.getInfo();
        String sql = "insert into t_info(HOST, PORT, INFO) values ('" + host + "'," + port + ",'" + info +"')";  // 语句sql
        statement.executeUpdate(sql);
    }

    @Override
    public int getCount() throws SQLException {
        String sql = "select count(*) from t_info";  // 语句sql
        ResultSet result = statement.executeQuery(sql);
        return result.getRow();
    }
}
