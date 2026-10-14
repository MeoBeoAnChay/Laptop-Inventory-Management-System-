package com.lims.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ImportReceipt implements Serializable {
    private static final long serialVersionUID = 1L;

    private int importReceiptId;
    private int importRequestId;
    private Integer handoverId;
    private int userId; // Changed from warehouseStaffID to UserID
    private Timestamp receiptDate;
    private String status; // DRAFT, COMPLETED
    private String note;

    // Joined fields
    private String userName;
    private String supplierName;
    private List<ImportReceiptDetail> details = new ArrayList<>();

    public ImportReceipt() {}

    public int getImportReceiptId() {
        return importReceiptId;
    }

    public void setImportReceiptId(int importReceiptId) {
        this.importReceiptId = importReceiptId;
    }

    public int getImportRequestId() {
        return importRequestId;
    }

    public void setImportRequestId(int importRequestId) {
        this.importRequestId = importRequestId;
    }

    public Integer getHandoverId() {
        return handoverId;
    }

    public void setHandoverId(Integer handoverId) {
        this.handoverId = handoverId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    // Compatibility alias
    public int getWarehouseStaffID() {
        return userId;
    }

    public void setWarehouseStaffID(int warehouseStaffID) {
        this.userId = warehouseStaffID;
    }

    public Timestamp getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Timestamp receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getWarehouseStaffName() {
        return userName;
    }

    public void setWarehouseStaffName(String warehouseStaffName) {
        this.userName = warehouseStaffName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public List<ImportReceiptDetail> getDetails() {
        return details;
    }

    public void setDetails(List<ImportReceiptDetail> details) {
        this.details = details;
    }
}
