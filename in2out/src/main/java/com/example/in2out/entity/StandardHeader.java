package com.example.in2out.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StandardHeader {
    int id;
    String sysID;
    String serviceID;
    String url;
    int lbWay;
    String datdType;
    String protocol;
}
