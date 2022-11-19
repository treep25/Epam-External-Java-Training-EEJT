package task2;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SearchingControllerTest {

    @Test
    void doChainEmptyTest() {
        SearchingController searchingController = new SearchingController(new Parameters());
        List<String> list = new ArrayList<>();
        list.add("Вы не ввели ни одного параметра!");
        assertEquals(list,searchingController.doChain(new ArrayList<>()));
    }
}