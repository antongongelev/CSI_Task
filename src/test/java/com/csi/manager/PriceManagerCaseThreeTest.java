package com.csi.manager;

import com.csi.PriceManager;
import com.csi.model.Price;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class PriceManagerCaseThreeTest {
    private static ArrayList<Price> getOldPrices() {
        ArrayList<Price> oldPrices = new ArrayList<>();
        oldPrices.add(new Price(1, "1", 1, 1,
                LocalDateTime.of(2020, 1, 1, 1, 1),
                LocalDateTime.of(2020, 4, 1, 1, 1), 1));
        oldPrices.add(new Price(2, "1", 1, 1,
                LocalDateTime.of(2020, 4, 1, 1, 1),
                LocalDateTime.of(2020, 8, 1, 1, 1), 2));
        oldPrices.add(new Price(3, "1", 1, 1,
                LocalDateTime.of(2020, 8, 1, 1, 1),
                LocalDateTime.of(2020, 12, 1, 1, 1), 3));
        return oldPrices;
    }

    private static ArrayList<Price> getNewPrices() {
        ArrayList<Price> newPrices = new ArrayList<>();
        newPrices.add(new Price(4, "1", 1, 1,
                LocalDateTime.of(2020, 2, 1, 1, 1),
                LocalDateTime.of(2020, 6, 1, 1, 1), 1));
        newPrices.add(new Price(5, "1", 1, 1,
                LocalDateTime.of(2020, 6, 1, 1, 1),
                LocalDateTime.of(2020, 10, 1, 1, 1), 4));
        return newPrices;
    }

    private static ArrayList<Price> getExpected() {
        ArrayList<Price> expected = new ArrayList<>();
        expected.add(new Price(1, "1", 1, 1,
                LocalDateTime.of(2020, 1, 1, 1, 1),
                LocalDateTime.of(2020, 6, 1, 1, 1), 1));
        expected.add(new Price(5, "1", 1, 1,
                LocalDateTime.of(2020, 6, 1, 1, 1),
                LocalDateTime.of(2020, 10, 1, 1, 1), 4));
        expected.add(new Price(3, "1", 1, 1,
                LocalDateTime.of(2020, 10, 1, 1, 1),
                LocalDateTime.of(2020, 12, 1, 1, 1), 3));
        return expected;
    }


    /**
     * old prices:
     * ....._____,,,,,
     * new prices:
     *   .....*****
     * result:
     * .......*****,,,
     */
    @Test
    public void testCaseThree() {
        PriceManager priceManager = new PriceManager();
        ArrayList<Price> result = priceManager.getUnitedPrices(getOldPrices(), getNewPrices());
        Assert.assertEquals(getExpected().toString(), result.toString());
    }
}
