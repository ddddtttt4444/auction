package com.dt4.exchange.auction;

import com.dt4.exchange.auction.model.Money;
import com.dt4.exchange.auction.model.Order;
import com.dt4.exchange.auction.model.Orders;

import java.io.*;

public class OrdersParser {
    public static Orders parse(InputStream inputStream) {

        int lineNumber = 0;
        Orders orders = new Orders();

        try (BufferedReader input = new BufferedReader(new InputStreamReader(inputStream))) {
            String orderLine;
            while ((orderLine = input.readLine()) != null && orderLine.length() != 0) {
                Order order = parseLine(orderLine);
                if (order.isPurchase()) {
                    orders.addPurchase(order);
                } else {
                    orders.addSale(order);
                }
                lineNumber++;
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Error occurred when parse input. Line number: " + lineNumber, e);
        }

        return orders;
    }

    public static Order parseLine(String orderLine) {
        String[] splitOrder = orderLine.split(" ");
        if (splitOrder.length != 3) {
            throw new IllegalArgumentException("Invalid line: " + orderLine);
        }

        return new Order(
                parsePurchaseFlag(splitOrder[0]),
                parseAmount(splitOrder[1]),
                parsePrice(splitOrder[2])
        );
    }

    public static boolean parsePurchaseFlag(String purchaseFlag) {
        if (!purchaseFlag.equals("B") && !purchaseFlag.equals("S")) {
            throw new IllegalArgumentException("Invalid purchaseFlag: " + purchaseFlag);
        }
        return purchaseFlag.equals("B");
    }

    public static int parseAmount(String amountAsString) {
        int amount = Integer.parseInt(amountAsString);

        if (amount < 0) {
            throw new IllegalArgumentException("Invalid amount: " + amount);
        }

        return amount;
    }

    public static int parsePrice(String price) {
        String[] splitPrice = price.split("\\.");
        if (splitPrice.length != 2) {
            throw new IllegalArgumentException("Invalid price: " + price);
        }

        int roubles = Integer.parseInt(splitPrice[0]);
        if (roubles < 0) {
            throw new IllegalArgumentException("Invalid price: " + price);
        }

        int kopecks = Integer.parseInt(splitPrice[1]);
        if (kopecks < 0 || 100 < kopecks) {
            throw new IllegalArgumentException("Invalid price: " + price);
        }

        return roubles * Money.ROUBLE_IN_KOPECKS + kopecks;
    }
}
