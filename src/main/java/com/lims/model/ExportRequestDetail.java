package com.lims.model;

import java.io.Serializable;

public class ExportRequestDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    private int exportRequestDetailId;
    private int exportRequestId;
    private int productId;
    private int quantity;

    // Joined fields
    private String productCode;
    private String productName;

    public ExportRequestDetail() {}

    public ExportRequestDetail(int exportRequestDetailId, int exportRequestId, int productId, int quantity) {
        this.exportRequestDetailId = exportRequestDetailId;
        this.exportRequestId = exportRequestId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getExportRequestDetailId() {
        return exportRequestDetailId;
    }

    public void setExportRequestDetailId(int exportRequestDetailId) {
        this.exportRequestDetailId = exportRequestDetailId;
    }

    public int getExportRequestId() {
        return exportRequestId;
    }

    public void setExportRequestId(int exportRequestId) {
        this.exportRequestId = exportRequestId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

