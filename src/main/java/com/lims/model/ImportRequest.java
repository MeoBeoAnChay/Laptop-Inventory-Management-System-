package com.lims.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ImportRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private int importRequestId;
    private int supplierId;
    private int createdBy;
    private Timestamp requestDate;
    private Integer approvedBy;
    private Timestamp approvedDate;
    private String status; // PENDING, APPROVED, REJECTED, COMPLETED, CANCELLED
    private String cancelReason;

    // Joined fields
    private String supplierName;
    private String creatorName;
    private String approverName;
    private List<ImportRequestDetail> details = new ArrayList<>();

    public ImportRequest() {}

    public int getImportRequestId() {
        return importRequestId;
    }

    public void setImportRequestId(int importRequestId) {
        this.importRequestId = importRequestId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
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

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
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

    public List<ImportRequestDetail> getDetails() {
        return details;
    }

    public void setDetails(List<ImportRequestDetail> details) {
        this.details = details;
    }
}

