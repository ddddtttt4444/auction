package com.dt4.exchange.auction.model;

import java.util.ArrayList;
import java.util.List;

public class Orders {
    private final List<Order> purchases = new ArrayList<>();
    private final List<Order> sales = new ArrayList<>();

    public void addPurchase(Order purchase) {
        purchases.add(purchase);
    }

    public void addSale(Order sale) {
        sales.add(sale);
    }

    public List<Order> getPurchases() {
        return purchases;
    }

    public List<Order> getSales() {
        return sales;
    }
}
