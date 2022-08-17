package com.example.in2out.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OuterXmlInfoCache {
    int id;
    String host;
    int port;
    String info;
}
