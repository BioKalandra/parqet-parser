package com.jan.coin.parqetparser;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * @author janhe
 *
 */
@Service
@Slf4j
public class ProgramStarter {
    
    @Autowired
    private Parser fileParser;
    
    @Autowired
    private CsvExporter exporter;
    
    public void startProcess() throws Exception {
        List<Path> findMails = fileParser.findMails();
        List<DataBean> data = new ArrayList<>();
        for (Path mail : findMails) {
            DataBean bean = fileParser.parseMail(mail);
            data.add(bean);
            log.debug(bean.toString());
        }
        exporter.exportToCsv(data);
    }
}
