package task2;

import java.util.List;

public interface SearchByParam {
    static final String dir = "EEJT-000A";

    List<String> search(Parameters parameters, List<String> paramList);

    List<String> searchFileWhenListWithParamEmpty(Parameters parameters, List<String> paramList);

    List<String> searchFileWhenListWithParamNotEmpty(Parameters parameters, List<String> paramList);

}
