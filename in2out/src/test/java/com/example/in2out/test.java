package com.example.in2out;

import com.example.in2out.entity.OuterXmlInfoCache;
import com.example.in2out.mapper.OuterXmlMapper;
import com.example.in2out.service.OuterXmlFileService;
import com.example.in2out.service.impl.OuterXmlFileServiceImpl;
//import org.apache.ibatis.session.SqlSession;
//import org.apache.ibatis.session.SqlSessionFactory;
import com.example.in2out.utils.XmlUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Map;

public class test {
//    @Autowired
    OuterXmlFileService outerXmlFileService = new OuterXmlFileServiceImpl();

    @Autowired
    DataSource dataSource;

    @Test
    void test() {
        String test = "{\"APP_BODY\":{“MAN”:\"1111\",\"LT_CRDT_ID\":\"2222\",\"TP\":\"3333\"}}";
        System.out.println(test);
    }

    @Test
    void select() throws SQLException {
        System.out.println(outerXmlFileService.getOuterXml("127.0.0.1", 1111));
    }

    @Test
    void insert() throws SQLException {
//        outerXmlFileService.insertOuterXml(cache);
//        OuterXmlInfoCache outerXml1 = outerXmlFileService.getOuterXml("localhost", 1234);
//        System.out.println(outerXml1.getInfo());
        OuterXmlInfoCache cache = new OuterXmlInfoCache();
        cache.setHost("127.0.0.1");
        cache.setPort(7777);
        cache.setInfo("该死的数据库！！！");
        outerXmlFileService.insertOuterXml(cache);
    }

    @Test
    void testConnection() throws SQLException, ClassNotFoundException {
        int count = outerXmlFileService.getCount();
        System.out.println(count);
//        SqlSession sqlSession = getSqlSession();



        // 尝试用jdbc连接数据库

//        Class.forName("com.mysql.cj.jdbc.Driver");
//        String url = "jdbc:mysql://localhost:3306/gateway-test?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
//        String username = "root";
//        String password = "123456";
//        Connection connection = DriverManager.getConnection(url, username, password);
//        Statement statement = connection.createStatement();  // 创建Statement对象
//        String sql = "select count(*) from t_info";  // 语句sql
//        ResultSet result = statement.executeQuery(sql);  // 获得返回的结果集ResultSet
//        System.out.println(result.next());
//        System.out.println(result.getRow());


    }

    @Test
    public void xmlTransTest(){
        String str = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><SERVICE>THIS IS A MESSAGE FROM OUTER SERVER</SERVICE>";
        XmlUtils xmlUtils = new XmlUtils();
        Map<String, Object> map = xmlUtils.multilayerXmlToMap(str);
        for (Map.Entry<String, Object> entry: map.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

}
