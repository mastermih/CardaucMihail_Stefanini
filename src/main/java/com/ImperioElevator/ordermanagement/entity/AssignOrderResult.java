package com.ImperioElevator.ordermanagement.entity;

public class AssignOrderResult {
    private final Long userId;
    private final Long result;

    // Constructor
    public AssignOrderResult(Long userId, Long result) {
        this.userId = userId;
        this.result = result;
    }

    // Getter for userId
    public Long getUserId() {
        return userId;
    }

    // Getter for result
    public Long getResult() {
        return result;
    }
}
