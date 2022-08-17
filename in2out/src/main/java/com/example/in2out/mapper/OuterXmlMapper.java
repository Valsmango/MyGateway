package com.example.in2out.mapper;

import com.example.in2out.entity.OuterXmlInfoCache;
//import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

//@Mapper
@Component
public interface OuterXmlMapper {
    public OuterXmlInfoCache getOuterXml(String host, int port) throws SQLException;

    public void insertOuterXml(OuterXmlInfoCache outerXmlInfoCache) throws SQLException;

    public int getCount() throws SQLException;
}
