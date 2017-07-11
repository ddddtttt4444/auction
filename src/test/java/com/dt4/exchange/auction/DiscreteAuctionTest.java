package com.dt4.exchange.auction;

import com.dt4.exchange.auction.model.Order;
import com.dt4.exchange.auction.model.Orders;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class DiscreteAuctionTest {
    @Test
    public void testApply() throws Exception {
        Orders orders = new Orders();
        orders.addSale(new Order(false, 150, 1530));
        orders.addPurchase(new Order(true, 100, 1530));
        orders.addPurchase(new Order(true, 100, 1540));

        DiscreteAuction.Result result = DiscreteAuction.apply(orders);

        assertEquals(result.getAmount(), 150);
        assertEquals(result.getTotalSumInKopeck(), 230500); // 1540 * 100 + (150 - 100) * 1530
    }

    @Test
    public void testApplyNA() throws Exception {
        Orders orders = new Orders();
        orders.addSale(new Order(false, 150, 2530));
        orders.addPurchase(new Order(true, 100, 1530));
        orders.addPurchase(new Order(true, 100, 1540));

        DiscreteAuction.Result result = DiscreteAuction.apply(orders);

        assertEquals(result.getAmount(), 0);
        assertEquals(result.getTotalSumInKopeck(), 0);
    }

    @DataProvider
    public static Object[][] results() {
        return new Object[][]{
                {new DiscreteAuction.Result(0, 0), "0 n/a"},
                {new DiscreteAuction.Result(100, 153050), "100 15.31"},
        };
    }

    @Test(dataProvider = "results")
    public void testResultToString(DiscreteAuction.Result result, String expected) {
        assertEquals(result.toString(), expected);
    }
}