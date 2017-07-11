package com.dt4.exchange.auction;

import com.dt4.exchange.auction.model.Orders;

import java.io.IOException;

public class Runner {
    public static void main(String[] args) throws IOException {
        Orders orders = OrdersParser.parse(System.in);
        DiscreteAuction.Result result = DiscreteAuction.apply(orders);
        System.out.println(result);
    }
}
