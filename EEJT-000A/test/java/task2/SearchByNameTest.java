package task2;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static task2.SearchByParam.dir;

class SearchByNameTest {
    private SearchByParam searchByParam = null;

    @Test
    void searchConfig() {
        SearchByParam SearchByName = new SearchByName(searchByParam);
        Parameters par = new Parameters();
        List<String> list = List.of(dir + ": " + "Данная папка не содержит файлов таких конфигураций");
        par.setName("P");
        assertEquals(list, SearchByName.search(par, new ArrayList<>()));
    }

    @Test
    void search() {
        SearchByParam searchByName = new SearchByName(searchByParam);
        Parameters par = new Parameters();
        par.setName("UTF8");
        assertEquals(List.of(dir+ File.separator+"UTF8"+".txt"),searchByName.search(par,new ArrayList<>()));
    }
}