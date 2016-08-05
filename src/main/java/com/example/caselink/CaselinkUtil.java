package com.example.caselink;

public class CaselinkUtil {

	static final CaselinkClient client;
    static {
        client = CaselinkClientImpl.create("http://10.66.4.102:8888/caselink/");
    }

    public static CaselinkClient setUp() {
        return client;
    }
}
