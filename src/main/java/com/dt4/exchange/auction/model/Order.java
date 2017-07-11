package com.dt4.exchange.auction.model;

public class Order {
    private final boolean isPurchase;
    private final int priceInKopeck;
    private int amount;

    public Order(boolean isPurchase, int amount, int priceInKopeck) {
        this.isPurchase = isPurchase;
        this.amount = amount;
        this.priceInKopeck = priceInKopeck;
    }

    public boolean isPurchase() {
        return isPurchase;
    }

    public int getAmount() {
        return amount;
    }

    public void decreaseAmount(int diff) {
        amount -= diff;
    }

    public int getPriceInKopeck() {
        return priceInKopeck;
    }
}
