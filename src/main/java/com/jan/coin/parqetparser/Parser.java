package com.jan.coin.parqetparser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.ZonedDateTime;
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
        final String sharePatternString = "Your Bitcoin purchase for (\\d\\.\\d{8}) BTC";
        final String transactionIdPatternString = "#000000;\\\">(6\\d\\w{3,24})</div>";
        final String datePatternString = "\\>(\\d\\d\\:\\d\\d\\sCE\\S?T, \\d\\d?\\s\\w{3,10}\\s\\d{4})<\\/div>";
        final String amountSpentPatternString = "(\\d{3,4}\\.\\d{2})\\sEUR(?! per BTC)";
        final String pricePatternString = "(\\d{3,7}\\.\\d{1,2})(?=\\sEUR\\s?per BTC)";
        final String feePatternString = "BTC \\/\\s(\\d{1,3}\\.\\d{1,2})\\s?EUR";
        
        String shares = parsePattern(mail, sharePatternString);
        String transactionId = parsePattern(mail, transactionIdPatternString);
        String dateString = parsePattern(mail, datePatternString);
        String amountSpent = parsePattern(mail, amountSpentPatternString);
        String price = parsePattern(mail, pricePatternString);
        String tradingFee = parsePattern(mail, feePatternString);
        String networkFee = parsePattern(mail, feePatternString, 2);
        String feesCombined = calculateFees(tradingFee, networkFee);
        
        String datetime = prepareDate(dateString);
        
        return new DataBean(transactionId, datetime, price, shares, amountSpent, "0", feesCombined, "Buy", "Crypto", "BTC", "EUR");
    }
    
    private String calculateFees(String tradingFee, String networkFee) {
        Double d1 = Double.valueOf(tradingFee);
        Double d2 = Double.valueOf(networkFee);
        Double result = d1 + d2;
        
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        formatSymbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#.##", formatSymbols);
        return df.format(result);
    }
    
    private String prepareDate(String dateString) {
        ZonedDateTime date = parseString(dateString);
        DateTimeFormatter isoInstant = DateTimeFormatter.ISO_INSTANT;
        return isoInstant.format(date);
    }
    
    private ZonedDateTime parseString(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm z, dd MMMM y").withLocale(Locale.ENGLISH);
        return ZonedDateTime.parse(dateString, formatter);
    }
    
    private String parsePattern(String content, final String patternString) throws ParseEmailException {
        return parsePattern(content, patternString, null);
    }
    
    private String parsePattern(String content, final String patternString, Integer group) throws ParseEmailException {
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(content);
        boolean matchFound = false;
        for (int matches = 1; matches <= (group != null ? group : 1); matches++) {
            matchFound = matcher.find();
        }
        if (matchFound) {
            if (matcher.groupCount() != 1) {
                throw new ParseEmailException("too many strings found");
            } else {
                return matcher.group(1);
            }
        } else {
            throw new ParseEmailException("no string found");
        }
    }
    
}
