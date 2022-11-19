package task1;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FileView {

    private String fileName;
    private StringBuilder stringBuilder;

    public FileView(String fileName) {
        this.fileName = "EEJT-000A/" + fileName;
        stringBuilder = new StringBuilder();
    }

    public String readFile() {
        Path path = Paths.get(fileName);
        try (Stream<String> stream = Files.lines(path, StandardCharsets.UTF_8)) {
            stream.forEach(stringBuilder::append);
        } catch (IOException e) {
            e.printStackTrace();
        }//TODO big files
        return stringBuilder.toString();
    }
}
