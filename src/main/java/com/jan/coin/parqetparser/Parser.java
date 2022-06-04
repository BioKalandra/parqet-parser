package com.jan.coin.parqetparser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
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
    
    public DataBean parseMail(Path mail) throws IOException, ParseEmailException, ParseException {
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
    
    private DataBean parseContentOfMail(String mail) throws ParseEmailException, ParseException {
        final String sharePatternString = "Your Bitcoin purchase for (\\d\\.\\d{8}) BTC";
        final String transactionIdPatternString = "#000000;\\\">(6\\d\\w{3,24})</div>";
        final String datePatternString = "\\>(\\d\\d\\:\\d\\d\\sCE\\S?T, \\d\\d?\\s\\w{3,10}\\s\\d{4})<\\/div>";
        
        String btc = parsePattern(mail, sharePatternString);
        String transactionId = parsePattern(mail, transactionIdPatternString);
        String dateString = parsePattern(mail, datePatternString);
        String preparedDate = prepareDate(dateString);
        
        DataBean dataBean = new DataBean();
        dataBean.setShares(btc);
        dataBean.setTransactionId(transactionId);
        dataBean.setDatetime(preparedDate);
        
        return dataBean;
    }
    
    private String prepareDate(String dateString) throws ParseException {
        LocalDateTime date = parseString(dateString);
        DateTimeFormatter isoInstant = DateTimeFormatter.ISO_INSTANT;
        
        return isoInstant.format(date);
    }
    
    private LocalDateTime parseString(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm z, dd MMMM y").withLocale(Locale.ENGLISH);
        return LocalDateTime.parse(dateString, formatter);
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
