package task2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.util.Objects.isNull;

public class SearchingController {
    private Parameters parameters;
    private SearchByParam searchByParam;

    public SearchingController(Parameters parameters) {
        this.parameters = parameters;
    }

    public List<String> doChain(List<String> list) {
        if (parameters == null) {
            if (isNull(list)) {
                list = new ArrayList<>();
            }
            if ((parameters.getSizeMore()) != null
                    && (parameters.getSizeLess()) != null) {
                SearchByParam newSearchByParam = null;
                searchByParam = new SearchBySizeChange(newSearchByParam);
            }
            if ((parameters.getDateMore()) != null
                    && (parameters.getDateLess()) != null) {
                SearchByParam newSearchByParam = searchByParam;
                searchByParam = new SearchByDateChange(newSearchByParam);
            }
            if ((parameters.getExt()) != null) {
                SearchByParam newSearchByParam = searchByParam;
                searchByParam = new SearchByExtension(newSearchByParam);
            }
            if ((parameters.getName()) != null) {
                SearchByParam newSearchByParam = searchByParam;
                searchByParam = new SearchByName(newSearchByParam);
            }
            if(searchByParam != null){
                return searchByParam.search(parameters, list);
            }
        }
        return List.of("Вы не ввели ни одного параметра!");
    }
}
