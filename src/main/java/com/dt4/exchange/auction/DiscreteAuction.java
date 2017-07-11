package com.dt4.exchange.auction;

import com.dt4.exchange.auction.model.Money;
import com.dt4.exchange.auction.model.Order;
import com.dt4.exchange.auction.model.Orders;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

import static java.util.stream.Collectors.toCollection;

public class DiscreteAuction {
    public static Result apply(Orders orders) {
        Queue<Order> purchases = orders.getPurchases().stream()
                .sorted(Comparator.comparing(Order::getPriceInKopeck).reversed())
                .collect(toCollection(LinkedList::new));
        Queue<Order> sales = orders.getSales().stream()
                .sorted(Comparator.comparing(Order::getPriceInKopeck).reversed())
                .collect(toCollection(LinkedList::new));

        Result result = new Result();

        while (!purchases.isEmpty() && !sales.isEmpty()) {
            Order purchase = purchases.poll();

            while (purchase.getAmount() > 0 && !sales.isEmpty()) {
                Order sale = sales.peek();
                if (purchase.getPriceInKopeck() < sale.getPriceInKopeck()) {
                    sales.remove();
                    continue;
                }

                int dealAmount = Math.min(purchase.getAmount(), sale.getAmount());

                purchase.decreaseAmount(dealAmount);
                sale.decreaseAmount(dealAmount);
                result.addDeal(dealAmount, purchase.getPriceInKopeck());

                if (sale.getAmount() == 0) {
                    sales.remove();
                }
            }
        }

        return result;
    }

    public static class Result {
        private int amount;
        private int totalSumInKopeck;

        public Result(int amount, int totalSumInKopeck) {
            this.amount = amount;
            this.totalSumInKopeck = totalSumInKopeck;
        }

        public Result() {
            amount = 0;
            totalSumInKopeck = 0;
        }

        public void addDeal(int dealAmount, int priceInKopeck) {
            amount += dealAmount;
            totalSumInKopeck += priceInKopeck * dealAmount;
        }

        public int getAmount() {
            return amount;
        }

        public int getTotalSumInKopeck() {
            return totalSumInKopeck;
        }

        @Override
        public String toString() {
            if (amount == 0) {
                return "0 n/a";
            } else {
                int ceilingAvgPriceInKopeck = (totalSumInKopeck + amount - 1) / amount;
                int roubles = ceilingAvgPriceInKopeck / Money.ROUBLE_IN_KOPECKS;
                int kopecks = ceilingAvgPriceInKopeck % Money.ROUBLE_IN_KOPECKS;

                return String.valueOf(amount) + " " + roubles + "." + kopecks;
            }
        }
    }
}
