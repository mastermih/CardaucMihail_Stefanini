package com.ImperioElevator.ordermanagement.valueobjects;

import java.time.LocalDateTime;

public class UpdateDateTime {
    private LocalDateTime updateDateTime;

    public UpdateDateTime(LocalDateTime updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public LocalDateTime getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(LocalDateTime updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    @Override
    public String toString() {
        return "UpdateDateTime{" +
                "updateDateTime=" + updateDateTime +
                '}';
    }
}
