package com.lims.model;

import java.io.Serializable;

public class Brand implements Serializable {
    private static final long serialVersionUID = 1L;

    private int brandId;
    private String brandName;
    private String status; // ACTIVE, INACTIVE

    public Brand() {}

    public Brand(int brandId, String brandName, String status) {
        this.brandId = brandId;
        this.brandName = brandName;
        this.status = status;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

