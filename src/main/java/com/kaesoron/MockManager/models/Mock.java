package com.kaesoron.MockManager.models;

import jakarta.persistence.*;

import java.util.Calendar;

@Entity
@Table(name = "mocks", indexes = {
        @Index(name = "mockPathIndex", columnList = "mockPath"),
        @Index(name = "mockIdIndex", columnList = "mockId"),
        @Index(name = "mockDateIndex", columnList = "mockDate"),
        @Index(name = "mockNameIndex", columnList = "mockName"),
})
public class Mock {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long mockId;
    @Column
    private String mockName;
    @Column
    private Calendar mockDate;
    @Column
    private String mockPath;
    @Column
    private String mockMethod;
    @Column
    private String mockResponse;
    @Column
    private int mockTimeout;

    public String getMockName() {
        return mockName;
    }

    public void setMockName(String mockName) {
        this.mockName = mockName;
    }

    public Calendar getMockDate() {
        return mockDate;
    }

    public void setMockDate(Calendar mockDate) {
        this.mockDate = mockDate;
    }

    public String getMockPath() {
        return mockPath;
    }

    public void setMockPath(String mockPath) {
        this.mockPath = mockPath;
    }

    public String getMockMethod() {
        return mockMethod;
    }

    public void setMockMethod(String mockMethod) {
        this.mockMethod = mockMethod;
    }

    public String getMockResponse() {
        return mockResponse;
    }

    public void setMockResponse(String mockResponse) {
        this.mockResponse = mockResponse;
    }

    public int getMockTimeout() {
        return mockTimeout;
    }

    public void setMockTimeout(int mockTimeout) {
        this.mockTimeout = mockTimeout;
    }
}
