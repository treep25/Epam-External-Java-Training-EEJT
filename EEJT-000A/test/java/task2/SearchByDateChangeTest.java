package task2;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static task2.SearchByParam.dir;

class SearchByDateChangeTest {
    private SearchByParam searchByParam = null;

    @Test
    void searchConfig() {
        SearchByParam searchByDate = new SearchByDateChange(searchByParam);
        Parameters par = new Parameters(null, null, "1356-11-22 21:08:40", "1800-11-22 21:08:40", null, null);
        List<String> list = List.of(dir + ": " + "Данная папка не содержит файлов таких конфигураций");
        assertEquals(list, searchByDate.search(par, new ArrayList<>()));
    }

    @Test
    void searchWhenListIsEmptyTest() {
        SearchByParam searchByDate = new SearchByExtension(new SearchByDateChange(null));
        Parameters par = new Parameters(null, ".txt", "2022-11-22 21:08:40", "2043-11-22 21:08:40", null, null);
        List<String> list = List.of(
                "C:\\Users\\User\\IdeaProjects\\Epam-External-Java-Training-EEJT\\EEJT-000A\\ANSI.txt (2022-11-25 12:54:33)",
                "C:\\Users\\User\\IdeaProjects\\Epam-External-Java-Training-EEJT\\EEJT-000A\\test.txt (2022-11-25 12:54:33)",
                "C:\\Users\\User\\IdeaProjects\\Epam-External-Java-Training-EEJT\\EEJT-000A\\UTF8.txt (2022-11-25 12:54:33)"
        );

        assertEquals(list, searchByDate.search(par, new ArrayList<>()));
    }

    @Test
    void searchWhenListIsNotEmptyTest() {
        SearchByParam searchByDate = new SearchByDateChange(new SearchBySizeChange(searchByParam));
        Parameters par = new Parameters(null, null, "1356-11-22 21:08:40", "2043-11-22 21:08:40", "1", "2");
        List<String> list = List.of("C:\\Users\\User\\IdeaProjects\\Epam-External-Java-Training-EEJT\\EEJT-000A\\test.txt (2022-11-25 12:54:33) (1) bytes");
        assertEquals(list, searchByDate.search(par, new ArrayList<>()));
    }

}