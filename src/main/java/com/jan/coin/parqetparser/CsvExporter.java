package com.jan.coin.parqetparser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * @author janhe
 *
 */
@Service
public class CsvExporter {
    
    private static String DELIMITER = ";";
    
    public void exportToCsv(List<DataBean> data) throws FileNotFoundException {
        List<String> content = prepareData(data);
        writeToFile(content);
    }
    
    private void writeToFile(List<String> content) throws FileNotFoundException {
        File csvOutputFile = new File("export.csv");
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            content.forEach(pw::println);
        }
        
    }
    
    private List<String> prepareData(List<DataBean> data) {
        List<String> content = new ArrayList<>();
        content.add("datetime;price;shares;amount;tax;fee;type;assettype;identifier;currency");
        for (DataBean bean : data) {
            content.add(bean.outPutCsv(DELIMITER));
        }
        return content;
    }
    
}
