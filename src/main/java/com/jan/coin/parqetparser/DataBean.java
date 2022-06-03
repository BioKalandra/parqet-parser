package com.jan.coin.parqetparser;

import java.io.Serializable;

import lombok.Data;

/**
 * @author janhe
 *
 */
@Data
public class DataBean implements Serializable {
    
    private String datetime;
    private String price;
    private String shares;
    private String amount;
    private String tax;
    private String fee;
    private String type;
    private String assettype;
    private String identifier;
    private String currency;
    
}
