package com.fiuba.tdp.linkup.util;

public class Globals {
    private static String serverAddress = "http://192.168.0.125:8080";
//    private static String serverAddress = "http://192.168.1.112:8080";
//    private static String serverAddress = "http://192.168.43.137:8080";
//    private static String serverAddress = "http://192.168.43.255:8080";
//    private static String serverAddress = "http://192.168.43.212:8080";
//    private static String serverAddress = "http://10.0.2.2:8080/";

    public static String getServerAddress() {
        return serverAddress;
    }
}
