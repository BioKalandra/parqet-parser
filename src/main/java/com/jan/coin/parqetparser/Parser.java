package com.jan.coin.parqetparser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * @author janhe
 *
 */
@Service
@Slf4j
public class Parser {
    
    public List<Path> findMails() {
        try (Stream<Path> walk = Files.walk(Paths.get(".\\import"))) {
            List<Path> emails = walk.filter(files -> Files.isRegularFile(files)).collect(Collectors.toList());
            if (emails.isEmpty()) {
                throw new NoSuchFileException("no regular files (mails found)");
            } else {
                log.info("found [{}] mails", emails.size());
                return emails;
            }
            
        } catch (NoSuchFileException e) {
            log.error("no files/folder found .. are there any mails in folder 'import'?", e);
        } catch (IOException e) {
            log.error("error parsing", e);
        }
        return Collections.emptyList();
    }
    
    public DataBean parseMail(Path mail) throws IOException {
        String read = loadFile(mail);
        return parseContentOfMail(read);
    }
    
    private String loadFile(Path mail) throws IOException {
        return Files.readAllLines(mail).stream().collect(Collectors.joining());
    }
    
    private DataBean parseContentOfMail(String content) {
        return new DataBean();
    }
    
}
