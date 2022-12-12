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

    public FileView(String fileName) {
        this.fileName = "EEJT-000A/" + fileName;
    }

    public List<String> readFile() {
        File file = new File(fileName);
        List<String> list = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            while (bufferedReader.ready()) {
                list.add(bufferedReader.readLine());
            }
        } catch (IOException ex) {
            return List.of("Данного файла не существует: ", ex.getMessage());
        }
        return list;
    }
}
