package com.example.in2out.service.impl;

import com.example.in2out.entity.OuterXmlInfoCache;
import com.example.in2out.mapper.OuterXmlMapper;
import com.example.in2out.mapper.impl.OuterXmlMapperImpl;
import com.example.in2out.service.OuterXmlFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
@Component
public class OuterXmlFileServiceImpl implements OuterXmlFileService {
//    @Autowired
    OuterXmlMapper outerXmlMapper = new OuterXmlMapperImpl();

    @Override
    public OuterXmlInfoCache getOuterXml(String host, int port) throws SQLException {
        return outerXmlMapper.getOuterXml(host, port);
    }

    @Override
    public void insertOuterXml(OuterXmlInfoCache outerXmlInfoCache) throws SQLException {
        outerXmlMapper.insertOuterXml(outerXmlInfoCache);
    }

    @Override
    public int getCount() throws SQLException {
        return outerXmlMapper.getCount();
    }
}
