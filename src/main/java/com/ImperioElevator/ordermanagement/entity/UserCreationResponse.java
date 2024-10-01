package com.ImperioElevator.ordermanagement.entity;

public class UserCreationResponse {
    private Long userId;
    private String message;

        public UserCreationResponse(Long userId, String message) {
            this.userId = userId;
            this.message = message;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }


