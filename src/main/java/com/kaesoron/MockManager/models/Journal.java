package com.kaesoron.MockManager.models;

import enums.Actions;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Calendar;

import static java.time.LocalDateTime.now;

@Entity
@Table(name = "journal", indexes = {
        @Index(name = "journalDateTimeIndex", columnList = "journalDateTime"),
        @Index(name = "journalMockMethodIndex", columnList = "journalMockMethod"),
        @Index(name = "journalMockNameIndex", columnList = "journalMockName")
})
public class Journal {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long journalScriptId;
    @Column
    private Actions action;
    @Column
    private Calendar journalDateTime;
    @Column
    private String journalMockName;
    @Column
    private String journalMockMethod;
    @Column
    private String journalMockRequest;
    @Column
    private String journalMockResponse;

    public Journal(Mock mock, Actions action) {
        this.setJournalDateTime(Calendar.getInstance());
        this.action = action;
        this.setJournalMockMethod(mock.getMockMethod());
        this.setJournalMockName(mock.getMockName());
        this.setJournalMockResponse(mock.getMockResponse());
    }

    public Actions getAction() {
        return action;
    }

    public void setAction(Actions action) {
        this.action = action;
    }

    public Calendar getJournalDateTime() {
        return journalDateTime;
    }

    public void setJournalDateTime(Calendar journalDateTime) {
        this.journalDateTime = journalDateTime;
    }

    public String getJournalMockName() {
        return journalMockName;
    }

    public void setJournalMockName(String journalMockName) {
        this.journalMockName = journalMockName;
    }

    public String getJournalMockMethod() {
        return journalMockMethod;
    }

    public void setJournalMockMethod(String journalMockMethod) {
        this.journalMockMethod = journalMockMethod;
    }

    public String getJournalMockRequest() {
        return journalMockRequest;
    }

    public void setJournalMockRequest(String journalMockRequest) {
        this.journalMockRequest = journalMockRequest;
    }

    public String getJournalMockResponse() {
        return journalMockResponse;
    }

    public void setJournalMockResponse(String journalMockResponse) {
        this.journalMockResponse = journalMockResponse;
    }
}
