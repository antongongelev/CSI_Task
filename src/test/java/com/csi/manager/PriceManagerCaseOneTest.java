package com.csi.manager;

import com.csi.PriceManager;
import com.csi.model.Price;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class PriceManagerCaseOneTest {

    private static ArrayList<Price> getOldPrices() {
        ArrayList<Price> oldPrices = new ArrayList<>();
        oldPrices.add(new Price(1, "1", 1, 1,
                LocalDateTime.of(2020, 1, 1, 1, 1),
                LocalDateTime.of(2020, 12, 1, 1, 1), 1));
        return oldPrices;
    }

    private static ArrayList<Price> getNewPrices() {
        ArrayList<Price> newPrices = new ArrayList<>();
        newPrices.add(new Price(2, "1", 1, 1,
                LocalDateTime.of(2020, 3, 1, 1, 1),
                LocalDateTime.of(2020, 7, 1, 1, 1), 2));
        return newPrices;
    }

    private static ArrayList<Price> getExpected() {
        ArrayList<Price> expected = new ArrayList<>();
        expected.add(new Price(1, "1", 1, 1,
                LocalDateTime.of(2020, 1, 1, 1, 1),
                LocalDateTime.of(2020, 3, 1, 1, 1), 1));
        expected.add(new Price(2, "1", 1, 1,
                LocalDateTime.of(2020, 3, 1, 1, 1),
                LocalDateTime.of(2020, 7, 1, 1, 1), 2));
        expected.add(new Price(1, "1", 1, 1,
                LocalDateTime.of(2020, 7, 1, 1, 1),
                LocalDateTime.of(2020, 12, 1, 1, 1), 1));
        return expected;
    }


    /**
     * old prices:
     * ________________
     * new prices:
     *       ****
     * result:
     * ______****______
     */
    @Test
    public void testCaseOne() {
        PriceManager priceManager = new PriceManager();
        ArrayList<Price> result = priceManager.getUnitedPrices(getOldPrices(), getNewPrices());
        Assert.assertEquals(getExpected().toString(), result.toString());
    }
}
