package com.lims.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ExportReceipt implements Serializable {
    private static final long serialVersionUID = 1L;

    private int exportReceiptId;
    private int exportRequestId;
    private int userId; // Changed from warehouseStaffID to UserID
    private Timestamp exportDate;
    private String status; // DRAFT, COMPLETED
    private String note;

    // Joined fields
    private String userName;
    private String customerName;
    private List<ExportReceiptDetail> details = new ArrayList<>();

    public ExportReceipt() {}

    public int getExportReceiptId() {
        return exportReceiptId;
    }

    public void setExportReceiptId(int exportReceiptId) {
        this.exportReceiptId = exportReceiptId;
    }

    public int getExportRequestId() {
        return exportRequestId;
    }

    public void setExportRequestId(int exportRequestId) {
        this.exportRequestId = exportRequestId;
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

    public Timestamp getExportDate() {
        return exportDate;
    }

    public void setExportDate(Timestamp exportDate) {
        this.exportDate = exportDate;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<ExportReceiptDetail> getDetails() {
        return details;
    }

    public void setDetails(List<ExportReceiptDetail> details) {
        this.details = details;
    }
}
