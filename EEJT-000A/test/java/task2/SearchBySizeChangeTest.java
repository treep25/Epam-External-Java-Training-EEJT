package task2;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static task2.SearchByParam.dir;

class SearchBySizeChangeTest {
    private SearchByParam searchByParam = null;

    @Test
    void searchConfig() {
        SearchByParam searchBySizeChange = new SearchBySizeChange(searchByParam);
        Parameters par = new Parameters(null, null, null, null, "10000000", "1000000000");
        List<String> list = List.of(dir + ": " + "Данная папка не содержит файлов таких конфигураций");
        assertEquals(list, searchBySizeChange.search(par, new ArrayList<>()));
    }

    @Test
    void searchWhenListIsEmptyTest() {
        SearchByParam searchBySizeChange = new SearchBySizeChange(searchByParam);
        Parameters par = new Parameters(null, null, null, null, "848444", "848449");
        List<String> list = List.of("C:\\Users\\User\\IdeaProjects\\Epam-External-Java-Training-EEJT\\EEJT-000A\\ANSI.txt 848444bytes");
        assertEquals(list, searchBySizeChange.search(par, new ArrayList<>()));
    }

}