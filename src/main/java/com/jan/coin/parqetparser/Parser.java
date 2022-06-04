package com.jan.coin.parqetparser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
            List<Path> emails = walk.filter(Files::isRegularFile).toList();
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
    
    public DataBean parseMail(Path mail) throws IOException, ParseEmailException {
        String read = loadFile(mail);
        return parseContentOfMail(read);
    }
    
    private String loadFile(Path mail) throws IOException {
        return Files.readAllLines(mail).stream().map(this::manipulateMail).collect(Collectors.joining());
    }
    
    private String manipulateMail(String line) {
        if (line.trim().isBlank()) {
            return "";
        } else {
            if (line.charAt(line.length() - 1) == '=') {
                return line.substring(0, line.length() - 1).trim();
            } else {
                return line.trim();
            }
        }
    }
    
    private DataBean parseContentOfMail(String mail) throws ParseEmailException {
        final String btcString = "Your Bitcoin purchase for (\\d\\.\\d{8}) BTC";
        final String transActionId = "#000000;\\\">(6\\d\\w{3,24})</div>";
        String btc = parsePattern(mail, btcString);
        String transactionId = parsePattern(mail, transActionId);
        DataBean dataBean = new DataBean();
        dataBean.setShares(btc);
        dataBean.setTransactionId(transactionId);
        return dataBean;
    }
    
    private String parsePattern(String content, final String patternString) throws ParseEmailException {
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(content);
        boolean matchFound = matcher.find();
        if (matchFound) {
            if (matcher.groupCount() != 1) {
                throw new ParseEmailException("too many strings found");
            } else {
                return matcher.group(1);
            }
        } else {
            throw new ParseEmailException("too many strings found");
        }
    }
    
}
