package org.infastructure.rest;

public class ApiPath {

    public static final String ROOT = "/api";

    public static class Root {
        public static final String PRODUCTS = ROOT + "/products";
        public static final String ORDERS = ROOT + "/orders";

        public static final String CARTS = ROOT + "/carts";
        public static final String CUSTOMERS = ROOT + "/customers";
    }
}
