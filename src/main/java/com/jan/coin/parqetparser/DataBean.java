package com.jan.coin.parqetparser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author janhe
 *
 */
@Data
@AllArgsConstructor
public class DataBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String transactionId;
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
    
    public String outPutCsv(final String delimiter) {
        List<String> stringList = new ArrayList<>();
        stringList.add(datetime);
        stringList.add(price);
        stringList.add(shares);
        stringList.add(amount);
        stringList.add(tax);
        stringList.add(fee);
        stringList.add(type);
        stringList.add(assettype);
        stringList.add(identifier);
        stringList.add(currency);
        return stringList.stream().collect(Collectors.joining(delimiter));
        
    }
}
