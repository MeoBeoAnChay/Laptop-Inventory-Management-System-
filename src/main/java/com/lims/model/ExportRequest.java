package com.lims.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ExportRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private int exportRequestId;
    private int customerId;
    private int createdBy;
    private Timestamp requestDate;
    private Integer approvedBy;
    private Timestamp approvedDate;
    private String status; // PENDING, APPROVED, REJECTED, COMPLETED, CANCELLED
    private String cancelReason;

    // Joined fields
    private String customerName;
    private String customerPhone;
    private String creatorName;
    private String approverName;
    private List<ExportRequestDetail> details = new ArrayList<>();

    public ExportRequest() {}

    public int getExportRequestId() {
        return exportRequestId;
    }

    public void setExportRequestId(int exportRequestId) {
        this.exportRequestId = exportRequestId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Timestamp requestDate) {
        this.requestDate = requestDate;
    }

    public Integer getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Integer approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Timestamp getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Timestamp approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getApproverName() {
        return approverName;
    }

    public void setApproverName(String approverName) {
        this.approverName = approverName;
    }

    public List<ExportRequestDetail> getDetails() {
        return details;
    }

    public void setDetails(List<ExportRequestDetail> details) {
        this.details = details;
    }
}

