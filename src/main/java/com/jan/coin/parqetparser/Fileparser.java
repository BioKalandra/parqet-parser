package com.jan.coin.parqetparser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

/**
 * @author janhe
 *
 */
@Service
public class Fileparser {
    
    public void parseMails() {
        try (Stream<Path> walk = Files.walk(Paths.get("C:\\"))) {
            System.err.println("elements : [" + walk.count() + "]");
            walk.map(x -> x.getFileName()).forEach(System.err::println);
            walk.forEach(System.err::println);
        } catch (
        
        IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
