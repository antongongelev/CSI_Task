package com.csi;

import com.csi.model.Price;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class PriceManager {

    public ArrayList<Price> getUnitedPrices(ArrayList<Price> oldPrices, ArrayList<Price> newPrices) {
        ArrayList<Price> result = new ArrayList<>();
        //find all prices with same code, number and department and split them on two buckets
        ArrayList<ArrayList<Price>> oldPriceBuckets = getPriceBuckets(oldPrices);
        ArrayList<ArrayList<Price>> newPriceBuckets = getPriceBuckets(newPrices);
        //split buckets on related and unrelated buckets
        ArrayList<ArrayList<Price>> unrelatedOldPriceBuckets = getUnrelatedPriceBuckets(oldPriceBuckets, newPriceBuckets);
        ArrayList<ArrayList<Price>> unrelatedNewPriceBuckets = getUnrelatedPriceBuckets(newPriceBuckets, oldPriceBuckets);
        ArrayList<ArrayList<Price>> relatedPriceBuckets = getRelatedPriceBuckets(oldPriceBuckets, newPriceBuckets);
        //merge prices with same value
        mergeEqualPrices(relatedPriceBuckets);
        //collect buckets in one list
        populateListWithBuckets(result, relatedPriceBuckets);
        populateListWithBuckets(result, unrelatedOldPriceBuckets);
        populateListWithBuckets(result, unrelatedNewPriceBuckets);
        return result;
    }


    private void mergeEqualPrices(ArrayList<ArrayList<Price>> priceBuckets) {
        for (ArrayList<Price> priceBucket : priceBuckets) {
            priceBucket.sort(new Price());
            modifyBucket(priceBucket);
        }
    }

    private void modifyBucket(ArrayList<Price> priceBucket) {
        for (int i = priceBucket.size() - 1; i > 0; i--) {
            if (priceBucket.get(i).getValue() == priceBucket.get(i - 1).getValue()) {
                priceBucket.get(i - 1).setEnd(priceBucket.get(i).getEnd());
                priceBucket.remove(i);
            }
        }
    }


    private void populateListWithBuckets(ArrayList<Price> result, ArrayList<ArrayList<Price>> priceBuckets) {
        for (ArrayList<Price> bucket : priceBuckets) {
            result.addAll(bucket);
        }
    }

    private ArrayList<ArrayList<Price>> getUnrelatedPriceBuckets(ArrayList<ArrayList<Price>> first,
                                                                 ArrayList<ArrayList<Price>> second) {
        ArrayList<ArrayList<Price>> unrelatedPriceBuckets = new ArrayList<>();
        outerLoop:
        for (ArrayList<Price> firstPriceBucket : first) {
            for (ArrayList<Price> secondPriceBucket : second) {
                if (areBucketsRelated(firstPriceBucket, secondPriceBucket)) {
                    continue outerLoop;
                }
            }
            unrelatedPriceBuckets.add(firstPriceBucket);
        }
        return unrelatedPriceBuckets;
    }

    private ArrayList<ArrayList<Price>> getRelatedPriceBuckets(ArrayList<ArrayList<Price>> first,
                                                               ArrayList<ArrayList<Price>> second) {
        ArrayList<ArrayList<Price>> relatedPriceBuckets = new ArrayList<>();
        for (ArrayList<Price> firstPriceBucket : first) {
            for (ArrayList<Price> secondPriceBucket : second) {
                if (areBucketsRelated(firstPriceBucket, secondPriceBucket)) {
                    relatedPriceBuckets.add(mergePriceBuckets(firstPriceBucket, secondPriceBucket));
                }
            }
        }
        return relatedPriceBuckets;
    }

    private ArrayList<Price> mergePriceBuckets(ArrayList<Price> oldPriceBucket, ArrayList<Price> newPriceBucket) {
        ArrayList<Price> mergedBucket = new ArrayList<>(oldPriceBucket);
        for (Price newPrice : newPriceBucket) {
            mergePrice(newPrice, mergedBucket);
        }
        return mergedBucket;
    }

    private void mergePrice(Price newPrice, ArrayList<Price> mergedBucket) {
        try {
            Price overarchingPrice = mergedBucket.stream()
                    .filter(price -> isPriceInRange(newPrice, price))
                    .findFirst()
                    .get();

            mergedBucket.remove(overarchingPrice);
            mergedBucket.addAll(getChoppedPrices(newPrice, overarchingPrice));
        } catch (NoSuchElementException e) {
            mergedBucket.removeIf(price -> isPriceOverarching(newPrice, price));
            mergedBucket.forEach(price -> modifyPrice(price, newPrice));
            mergedBucket.add(newPrice);
        }
    }

    private ArrayList<Price> getChoppedPrices(Price newPrice, Price price) {
        ArrayList<Price> result = new ArrayList<>();
        result.add(new Price(price.getId(), price.getProductCode(), price.getNumber(), price.getDepartment(), price.getBegin(), newPrice.getBegin(), price.getValue()));
        result.add(new Price(price.getId(), price.getProductCode(), price.getNumber(), price.getDepartment(), newPrice.getEnd(), price.getEnd(), price.getValue()));
        result.add(newPrice);
        return result;
    }

    private boolean isPriceInRange(Price first, Price second) {
        return first.getBegin().isAfter(second.getBegin()) && first.getEnd().isBefore(second.getEnd());
    }

    private void modifyPrice(Price price, Price newPrice) {
        if (newPrice.getBegin().isAfter(price.getBegin()) && newPrice.getBegin().isBefore(price.getEnd())) {
            price.setEnd(newPrice.getBegin());
        } else if (newPrice.getEnd().isAfter(price.getBegin()) && newPrice.getEnd().isBefore(price.getEnd())) {
            price.setBegin(newPrice.getEnd());
        }
    }

    private boolean isPriceOverarching(Price first, Price second) {
        return (first.getBegin().isBefore(second.getBegin()) && first.getEnd().isAfter(second.getEnd())) ||
                (first.getBegin().isEqual(second.getBegin()) && first.getEnd().isAfter(second.getEnd())) ||
                (first.getBegin().isBefore(second.getBegin()) && first.getEnd().equals(second.getEnd())) ||
                (first.getBegin().isEqual(second.getBegin()) && first.getEnd().isEqual(second.getEnd()));
    }


    private boolean areBucketsRelated(List<Price> first, List<Price> second) {
        return !first.isEmpty() && !second.isEmpty() && first.get(0).compete(second.get(0));
    }

    private ArrayList<ArrayList<Price>> getPriceBuckets(ArrayList<Price> prices) {
        ArrayList<ArrayList<Price>> priceBuckets = new ArrayList<>();
        outerLoop:
        for (Price price : prices) {
            if (priceBuckets.isEmpty()) {
                addNewBucketWithPrice(priceBuckets, price);
                continue;
            }
            for (ArrayList<Price> bucket : priceBuckets) {
                if (price.compete(bucket.get(0))) {
                    bucket.add(price);
                    continue outerLoop;
                }
            }
            addNewBucketWithPrice(priceBuckets, price);
        }
        return priceBuckets;
    }

    private void addNewBucketWithPrice(ArrayList<ArrayList<Price>> priceBuckets, Price price) {
        ArrayList<Price> newBucket = new ArrayList<>();
        newBucket.add(price);
        priceBuckets.add(newBucket);
    }
}
