package com.lims.model;

import java.io.Serializable;

public class ProductLine implements Serializable {
    private static final long serialVersionUID = 1L;

    private int productLineId;
    private int brandId;
    private String lineName;

    // Joined fields
    private String brandName;

    public ProductLine() {}

    public ProductLine(int productLineId, int brandId, String lineName) {
        this.productLineId = productLineId;
        this.brandId = brandId;
        this.lineName = lineName;
    }

    public int getProductLineId() {
        return productLineId;
    }

    public void setProductLineId(int productLineId) {
        this.productLineId = productLineId;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}

