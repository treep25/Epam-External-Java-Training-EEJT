package task2;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class SearchingController {
    private Parameters parameters;
    private Map<Predicate<Parameters>, Function<SearchByParam, SearchByParam>> chainMapSearching = new LinkedHashMap<>();


    public SearchingController(Parameters parameters) {
        this.parameters = parameters;
    }

    private void generateChainMapSearching() {
        chainMapSearching.put(parameters -> parameters.sizeMore() != null && parameters.sizeLess() != null, SearchBySizeChange::new);
        chainMapSearching.put(parameters -> parameters.dateMore() != null && parameters.dateLess() != null, SearchByDateChange::new);
        chainMapSearching.put(parameters -> parameters.ext() != null, SearchByExtension::new);
        chainMapSearching.put(parameters -> parameters.name() != null, SearchByName::new);
    }

    public List<String> doChain() {
        List<String> paramList = new ArrayList<>();
        SearchByParam searchByParam = null;
        if (parameters != null) {
            generateChainMapSearching();
            for (Map.Entry<Predicate<Parameters>, Function<SearchByParam, SearchByParam>> entry : chainMapSearching.entrySet()) {
                if (entry.getKey().test(parameters)) {
                    searchByParam = entry.getValue().apply(searchByParam);
                }
            }
            if (searchByParam != null) {
                return searchByParam.search(parameters, paramList);
            }
        }
        return List.of("Вы не ввели ни одного параметра!");
    }
}
