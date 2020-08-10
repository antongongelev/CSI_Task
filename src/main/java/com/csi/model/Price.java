package com.csi.model;

import java.time.LocalDateTime;
import java.util.Comparator;

public class Price implements Comparator<Price> {

    private long id;
    private String productCode;
    private int number;
    private int department;
    private LocalDateTime begin;
    private LocalDateTime end;
    long value;

    public Price() {
    }

    public Price(long id, String productCode, int number, int department, LocalDateTime begin, LocalDateTime end, long value) {
        this.id = id;
        this.productCode = productCode;
        this.number = number;
        this.department = department;
        this.begin = begin;
        this.end = end;
        this.value = value;
    }

    @Override
    public int compare(Price price1, Price price2) {
        if (price1.begin.isAfter(price2.begin)) {
            return 1;
        }
        if (price1.begin.isBefore(price2.begin)) {
            return -1;
        }
        return 0;
    }

    public boolean compete(Price otherPrice) {
        return this.productCode.equals(otherPrice.productCode) &&
                this.number == otherPrice.number &&
                this.department == otherPrice.department;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getDepartment() {
        return department;
    }

    public void setDepartment(int department) {
        this.department = department;
    }

    public LocalDateTime getBegin() {
        return begin;
    }

    public void setBegin(LocalDateTime begin) {
        this.begin = begin;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Price{" +
                "id=" + id +
                ", productCode='" + productCode + '\'' +
                ", number=" + number +
                ", department=" + department +
                ", begin=" + begin +
                ", end=" + end +
                ", value=" + value +
                '}';
    }
}
