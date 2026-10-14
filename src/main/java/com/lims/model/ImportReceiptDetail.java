package com.lims.model;

import java.io.Serializable;

public class ImportReceiptDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    private int importReceiptDetailId;
    private int importReceiptId;
    private int productId;
    private String serialNumber;
    private String location;
    private String condition; // BRAND_NEW, DEFECTIVE, SCRATCHED

    // Joined fields
    private String productCode;
    private String productName;

    public ImportReceiptDetail() {}

    public ImportReceiptDetail(int importReceiptDetailId, int importReceiptId, int productId, String serialNumber, String location, String condition) {
        this.importReceiptDetailId = importReceiptDetailId;
        this.importReceiptId = importReceiptId;
        this.productId = productId;
        this.serialNumber = serialNumber;
        this.location = location;
        this.condition = condition;
    }

    public int getImportReceiptDetailId() {
        return importReceiptDetailId;
    }

    public void setImportReceiptDetailId(int importReceiptDetailId) {
        this.importReceiptDetailId = importReceiptDetailId;
    }

    public int getImportReceiptId() {
        return importReceiptId;
    }

    public void setImportReceiptId(int importReceiptId) {
        this.importReceiptId = importReceiptId;
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

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
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
}

