package task2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SearchByExtension implements SearchByParam {
    private SearchByParam searchByParam;

    public SearchByExtension(SearchByParam searchByParam) {
        try {
            if (!new File(dir).exists()) {
                throw new FileNotFoundException();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
        this.searchByParam = searchByParam;
    }

    private boolean hasNextChain(SearchByParam searchByParam) {
        return searchByParam != null;
    }

    @Override
    public List<String> searchFileWhenListWithParamEmpty(Parameters parameters, List<String> paramList) {
        File file = new File(dir);
        List<File> fileList = List.of(file.listFiles(
                (x1, x2) -> x2.toLowerCase().endsWith(parameters.ext())));
        if (!fileList.isEmpty()) {
            for (File f : fileList) {
                paramList.add(dir + File.separator + f.getName());
            }
        } else {
            paramList.add(dir + ": " + "Данная папка вероятно не сущетсвует");
        }
        return paramList;
    }

    @Override
    public List<String> searchFileWhenListWithParamNotEmpty(Parameters parameters, List<String> paramList) {
        return paramList.stream().filter(x1 -> x1.endsWith(parameters.ext().toLowerCase())).collect(Collectors.toList());
    }

    @Override
    public List<String> search(Parameters parameters, List<String> paramList) {
        if (paramList.isEmpty()) {
            paramList = searchFileWhenListWithParamEmpty(parameters, paramList);
        } else {
            paramList = searchFileWhenListWithParamNotEmpty(parameters, paramList);
        }
        if (hasNextChain(searchByParam)) {
            return searchByParam.search(parameters, paramList);
        }
        return paramList;
    }
}
