package com.lims.model;

import java.io.Serializable;

public class ExportReceiptDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    private int exportReceiptDetailId;
    private int exportReceiptId;
    private int inventoryId;
    private int productId;
    private String serialNumber;

    // Joined fields
    private String productCode;
    private String productName;

    public ExportReceiptDetail() {}

    public ExportReceiptDetail(int exportReceiptDetailId, int exportReceiptId, int inventoryId, int productId, String serialNumber) {
        this.exportReceiptDetailId = exportReceiptDetailId;
        this.exportReceiptId = exportReceiptId;
        this.inventoryId = inventoryId;
        this.productId = productId;
        this.serialNumber = serialNumber;
    }

    public int getExportReceiptDetailId() {
        return exportReceiptDetailId;
    }

    public void setExportReceiptDetailId(int exportReceiptDetailId) {
        this.exportReceiptDetailId = exportReceiptDetailId;
    }

    public int getExportReceiptId() {
        return exportReceiptId;
    }

    public void setExportReceiptId(int exportReceiptId) {
        this.exportReceiptId = exportReceiptId;
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

