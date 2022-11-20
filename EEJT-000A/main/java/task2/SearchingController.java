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
            if (parameters.getSizeMore() != null
                    && parameters.getSizeLess() != null) {
                searchByParam = new SearchBySizeChange(searchByParam);
            }
            if ((parameters.getDateMore()) != null
                    && (parameters.getDateLess()) != null) {
                searchByParam = new SearchByDateChange(searchByParam);
            }
            if ((parameters.getExt()) != null) {
                searchByParam = new SearchByExtension(searchByParam);
            }
            if ((parameters.getName()) != null) {
                searchByParam = new SearchByName(searchByParam);
            }
            if(searchByParam != null){
                return searchByParam.search(parameters, list);
            }
        }
        return List.of("Вы не ввели ни одного параметра!");
    }
}
