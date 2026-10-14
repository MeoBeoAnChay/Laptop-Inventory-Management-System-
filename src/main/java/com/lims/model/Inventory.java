package com.lims.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Inventory implements Serializable {
    private static final long serialVersionUID = 1L;

    private int inventoryId;
    private int productId;
    private String serialNumber;
    private String location;
    private String status; // IN_STOCK, RESERVED, EXPORTED, DAMAGED, RETURNED
    private Timestamp updatedAt;

    // Joined fields
    private String productCode;
    private String productName;
    private String brandName;
    private String lineName;
    private java.math.BigDecimal unitPrice;

    public Inventory() {}

    public Inventory(int inventoryId, int productId, String serialNumber, String location, String status, Timestamp updatedAt) {
        this.inventoryId = inventoryId;
        this.productId = productId;
        this.serialNumber = serialNumber;
        this.location = location;
        this.status = status;
        this.updatedAt = updatedAt;
    }

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public java.math.BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(java.math.BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}

