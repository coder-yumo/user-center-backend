package com.yupi.usercenterbackend.model.request;

import java.io.Serializable;
import lombok.Data;

/**
 *
 *用户注册请求参数
 *
 * @author linli
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 3649964100878893385L;

    private String userAccount;

    private String userPassword;

}
