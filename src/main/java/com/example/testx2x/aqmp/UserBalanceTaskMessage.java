package com.example.testx2x.aqmp;

import lombok.Data;


/**
 *
 */
@Data
public class UserBalanceTaskMessage {

    //todo 这里可以完善金额、时间戳

    private String userId;
    private double balanceChange;
    private int version;

}
