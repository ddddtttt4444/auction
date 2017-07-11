package com.dt4.exchange.auction;


import com.dt4.exchange.auction.model.Order;
import com.dt4.exchange.auction.model.Orders;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.testng.Assert.assertEquals;

public class OrdersParserTest {
    @Test
    public void testParse() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(
                "B 1000 10.20\nS 900 10.40".getBytes()
        );

        Orders orders = OrdersParser.parse(inputStream);
        assertEquals(orders.getPurchases().size(), 1);
        assertEquals(orders.getSales().size(), 1);

        Order purchase = orders.getPurchases().get(0);
        assertEquals(purchase.isPurchase(), true);
        assertEquals(purchase.getAmount(), 1000);
        assertEquals(purchase.getPriceInKopeck(), 1020);

        Order sale = orders.getSales().get(0);
        assertEquals(sale.isPurchase(), false);
        assertEquals(sale.getAmount(), 900);
        assertEquals(sale.getPriceInKopeck(), 1040);
    }

    @Test
    public void testParseLine() throws Exception {
        Order order = OrdersParser.parseLine("B 1000 10.20");

        assertEquals(order.isPurchase(), true);
        assertEquals(order.getAmount(), 1000);
        assertEquals(order.getPriceInKopeck(), 1020);
    }

    @DataProvider
    public static Object[][] invalidData() {
        return new Object[][]{
                {"B 1000 10.20 0"},
                {"B 1000"},
                {"B 1000 10"},
                {"B 1000a 10.20"},
                {"B 1000 10a.20"},
                {"B 1000 10.20a"},
                {"A 1000 10.20"},
                {"B 1000 10.200"},
                {"B -1000 10.20"},
                {"B 1000 -10.20"}
        };
    }

    @Test(dataProvider = "invalidData", expectedExceptions = RuntimeException.class)
    public void testParseInvalidLine(String line) throws Exception {
        OrdersParser.parseLine(line);
    }

    @DataProvider
    public static Object[][] purchaseFlagExamples() {
        return new Object[][]{
                {"B", true},
                {"S", false}
        };
    }

    @Test(dataProvider = "purchaseFlagExamples")
    public void testParsePurchaseFlag(String purchaseFlag, boolean expectedPurchaseFlag) throws Exception {
        assertEquals(OrdersParser.parsePurchaseFlag(purchaseFlag), expectedPurchaseFlag);
    }

    @Test
    public void testParseAmount() throws Exception {
        assertEquals(OrdersParser.parseAmount("1000"), 1000);
    }

    @DataProvider
    public static Object[][] priceExamples() {
        return new Object[][]{
                {"10.20", 1020},
                {"0.20", 20},
                {"10.00", 1000}
        };
    }

    @Test(dataProvider = "priceExamples")
    public void testParsePrice(String price, int expectedPrice) throws Exception {
        assertEquals(OrdersParser.parsePrice(price), expectedPrice);
    }

}