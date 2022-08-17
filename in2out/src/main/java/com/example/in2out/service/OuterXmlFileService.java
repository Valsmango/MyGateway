package com.example.in2out.service;

import com.example.in2out.entity.OuterXmlInfoCache;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
@Component
public interface OuterXmlFileService {
    public OuterXmlInfoCache getOuterXml(String host, int port) throws SQLException;
    public void insertOuterXml(OuterXmlInfoCache outerXmlInfoCache) throws SQLException;
    public int getCount() throws SQLException;
}
