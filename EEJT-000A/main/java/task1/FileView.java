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

    public List<String> readFile() {
        File file = new File(fileName);
        List<String> list = new ArrayList<>();
        try (FileReader fr = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fr)) {
            String line = bufferedReader.readLine();
            while (line != null) {
                list.add(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            return List.of("Данного файла не существует");
        }
        return list;
    }
}
