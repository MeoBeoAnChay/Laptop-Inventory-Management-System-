package com.lims.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class InventoryCheck implements Serializable {
    private static final long serialVersionUID = 1L;

    private int checkId;
    private int createdBy;
    private Timestamp checkDate;
    private String status; // IN_PROGRESS, COMPLETED, CANCELLED
    private String note;

    // Joined fields
    private String creatorName;
    private List<InventoryCheckDetail> details = new ArrayList<>();

    public InventoryCheck() {}

    public int getCheckId() {
        return checkId;
    }

    public void setCheckId(int checkId) {
        this.checkId = checkId;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Timestamp checkDate) {
        this.checkDate = checkDate;
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

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public List<InventoryCheckDetail> getDetails() {
        return details;
    }

    public void setDetails(List<InventoryCheckDetail> details) {
        this.details = details;
    }
}

