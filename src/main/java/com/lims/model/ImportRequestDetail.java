package com.lims.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class ImportRequestDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    private int importRequestDetailId;
    private int importRequestId;
    private int productId;
    private int quantity;
    private BigDecimal unitPrice;

    // Joined fields
    private String productCode;
    private String productName;

    public ImportRequestDetail() {}

    public ImportRequestDetail(int importRequestDetailId, int importRequestId, int productId, int quantity, BigDecimal unitPrice) {
        this.importRequestDetailId = importRequestDetailId;
        this.importRequestId = importRequestId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public int getImportRequestDetailId() {
        return importRequestDetailId;
    }

    public void setImportRequestDetailId(int importRequestDetailId) {
        this.importRequestDetailId = importRequestDetailId;
    }

    public int getImportRequestId() {
        return importRequestId;
    }

    public void setImportRequestId(int importRequestId) {
        this.importRequestId = importRequestId;
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

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getSubTotal() {
        if (unitPrice == null) return BigDecimal.ZERO;
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
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

