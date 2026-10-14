package com.lims.model;

import java.io.Serializable;

public class InventoryCheckDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    private int checkDetailId;
    private int checkId;
    private int inventoryId;
    private String systemStatus;
    private String actualStatus;
    private String discrepancyReason;

    // Joined fields
    private String serialNumber;
    private String productCode;
    private String productName;
    private String location;

    public InventoryCheckDetail() {}

    public InventoryCheckDetail(int checkDetailId, int checkId, int inventoryId, String systemStatus, String actualStatus, String discrepancyReason) {
        this.checkDetailId = checkDetailId;
        this.checkId = checkId;
        this.inventoryId = inventoryId;
        this.systemStatus = systemStatus;
        this.actualStatus = actualStatus;
        this.discrepancyReason = discrepancyReason;
    }

    public int getCheckDetailId() {
        return checkDetailId;
    }

    public void setCheckDetailId(int checkDetailId) {
        this.checkDetailId = checkDetailId;
    }

    public int getCheckId() {
        return checkId;
    }

    public void setCheckId(int checkId) {
        this.checkId = checkId;
    }

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getSystemStatus() {
        return systemStatus;
    }

    public void setSystemStatus(String systemStatus) {
        this.systemStatus = systemStatus;
    }

    public String getActualStatus() {
        return actualStatus;
    }

    public void setActualStatus(String actualStatus) {
        this.actualStatus = actualStatus;
    }

    public String getDiscrepancyReason() {
        return discrepancyReason;
    }

    public void setDiscrepancyReason(String discrepancyReason) {
        this.discrepancyReason = discrepancyReason;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

