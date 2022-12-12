package task1;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileViewTest {

    @Test
    void readFile() {
        FileView fileView = new FileView("test.txt");
        List<String> listResult = new ArrayList<>();
        listResult.add("1");
        List<String> list = fileView.readFile();
        assertEquals(listResult,list);
    }
    @Test
    void readFileError() {
        FileView fileView = new FileView("teqweqweqwest.t22xt");
        List <String> list1 = new ArrayList<>();
        list1.add("Данного файла не существует: ");
        list1.add("EEJT-000A\\teqweqweqwest.t22xt (Не удается найти указанный файл)");
        List<String> result = fileView.readFile();
        assertEquals(list1,result);
    }
}