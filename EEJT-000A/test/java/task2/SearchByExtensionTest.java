package task2;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static task2.SearchByParam.dir;

class SearchByExtensionTest {
    private SearchByParam searchByParam = null;

    @Test
    void searchConfig() {
        SearchByParam searchByExtension = new SearchByExtension(searchByParam);
        Parameters par = new Parameters(null, ".treeep", null, null, null, null);
        List<String> list = List.of(dir + ": " + "Данная папка вероятно не сущетсвует");
        assertEquals(list, searchByExtension.search(par, new ArrayList<>()));
    }

    @Test
    void searchWhenListIsEmptyTest() {
        SearchByParam searchByExtension = new SearchByExtension(searchByParam);
        Parameters par = new Parameters(null, ".txt", null, null, null, null);
        List<String> list = List.of("EEJT-000A\\ANSI.txt", "EEJT-000A\\test.txt", "EEJT-000A\\UTF8.txt");
        assertEquals(list, searchByExtension.search(par, new ArrayList<>()));
    }

    @Test
    void searchWhenListIsNotEmptyTest() {
        SearchByParam searchByExtension = new SearchByExtension(new SearchBySizeChange(null));
        Parameters par = new Parameters(null, ".txt", null, null, "1", "10");
        List<String> list = List.of("EEJT-000A\\test.txt(1)");
        assertEquals(list, searchByExtension.search(par, new ArrayList<>()));
    }
}