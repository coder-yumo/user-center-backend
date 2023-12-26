package com.yupi.usercenterbackend.model.domain.request;

import lombok.Data;

@Data
public class CurrentUserRequest {
    private String userAccount;
    private String uuid;
}
