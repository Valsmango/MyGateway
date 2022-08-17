package com.example.in2out.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Component
@Data
@XmlRootElement(name = "APP_BODY")
@XmlAccessorType(XmlAccessType.FIELD)
public class TestRequestEntity {
    private static final long serialVersionUID = 1L;

    @XmlElement(name = "NAM")
    private String NAM;
    @XmlElement(name = "LT_CRDT_ID")
    private String LT_CRDT_ID;
    @XmlElement(name = "TP")
    private String TP;
}