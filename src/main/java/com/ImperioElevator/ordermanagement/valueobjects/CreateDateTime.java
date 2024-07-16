package com.ImperioElevator.ordermanagement.valueobjects;

import java.time.LocalDateTime;

public class CreateDateTime {
    private LocalDateTime createDateTime;

    public CreateDateTime(LocalDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(LocalDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    @Override
    public String toString() {
        return "CreateDateTime{" +
                "createDateTime=" + createDateTime +
                '}';
    }
}