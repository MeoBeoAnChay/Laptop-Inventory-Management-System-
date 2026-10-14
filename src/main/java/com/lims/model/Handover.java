package com.lims.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Handover implements Serializable {
    private static final long serialVersionUID = 1L;

    private int handoverId;
    private int importRequestId;
    private String handoverBy;
    private Timestamp handoverDate;
    private int handoverQuantity;
    private String note;

    public Handover() {}

    public Handover(int handoverId, int importRequestId, String handoverBy, Timestamp handoverDate, int handoverQuantity, String note) {
        this.handoverId = handoverId;
        this.importRequestId = importRequestId;
        this.handoverBy = handoverBy;
        this.handoverDate = handoverDate;
        this.handoverQuantity = handoverQuantity;
        this.note = note;
    }

    public int getHandoverId() {
        return handoverId;
    }

    public void setHandoverId(int handoverId) {
        this.handoverId = handoverId;
    }

    public int getImportRequestId() {
        return importRequestId;
    }

    public void setImportRequestId(int importRequestId) {
        this.importRequestId = importRequestId;
    }

    public String getHandoverBy() {
        return handoverBy;
    }

    public void setHandoverBy(String handoverBy) {
        this.handoverBy = handoverBy;
    }

    public Timestamp getHandoverDate() {
        return handoverDate;
    }

    public void setHandoverDate(Timestamp handoverDate) {
        this.handoverDate = handoverDate;
    }

    public int getHandoverQuantity() {
        return handoverQuantity;
    }

    public void setHandoverQuantity(int handoverQuantity) {
        this.handoverQuantity = handoverQuantity;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

