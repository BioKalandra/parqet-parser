package com.jan.coin.parqetparser;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author janhe
 *
 */
@Service
public class Parser {
    
    @Autowired
    private Fileparser fileParser;
    
    public void startProcess() throws IOException {
        List<Path> findMails = fileParser.findMails();
        for (Path mail : findMails) {
            DataBean bean = fileParser.parseMail(mail);
        }
    }
}
