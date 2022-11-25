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
        List<String> list = List.of(dir + ": " + "Данная папка не содержит файлов таких конфигураций");
        assertEquals(list, searchByExtension.search(par, new ArrayList<>()));
    }

    @Test
    void searchWhenListIsEmptyTest() {
        SearchByParam searchByExtension = new SearchByExtension(searchByParam);
        Parameters par = new Parameters(null, ".txt", null, null, null, null);
        List<String> list = List.of("C:\\Users\\User\\IdeaProjects\\Epam-External-Java-Training-EEJT\\EEJT-000A\\ANSI.txt",
                "C:\\Users\\User\\IdeaProjects\\Epam-External-Java-Training-EEJT\\EEJT-000A\\test.txt",
                "C:\\Users\\User\\IdeaProjects\\Epam-External-Java-Training-EEJT\\EEJT-000A\\UTF8.txt"
        );
        assertEquals(list, searchByExtension.search(par, new ArrayList<>()));
    }

    @Test
    void searchWhenListIsNotEmptyTest() {
        SearchByParam searchByExtension = new SearchByExtension(new SearchBySizeChange(null));
        Parameters par = new Parameters(null, ".txt", null, null, "1", "10");
        List<String> list = List.of("C:\\Users\\User\\IdeaProjects\\Epam-External-Java-Training-EEJT\\EEJT-000A\\test.txt (1) bytes");
        assertEquals(list, searchByExtension.search(par, new ArrayList<>()));
    }
}