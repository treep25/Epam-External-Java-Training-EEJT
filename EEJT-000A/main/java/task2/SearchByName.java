package task2;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class SearchByName implements SearchByParam {

    private SearchByParam searchByParam;

    public SearchByName(SearchByParam searchByParam) {
        this.searchByParam = searchByParam;
    }

    private boolean hasNextChain(SearchByParam searchByParam){
        return searchByParam != null;
    }

    @Override
    public List<String> search(Parameters parameters, List<String> list) {
        File file = new File(dir);
        if (file.exists()) {
            List<File> fileList = List.of(Objects.requireNonNull(file.listFiles(
                    (x1, x2) -> x2.startsWith(parameters.getName()))));
            if (!fileList.isEmpty()) {
                for (File f :
                        fileList) {
                    list.add(dir + File.separator + f.getName());
                }
            } else {
                list.add(dir + ": " + "Данная папка не содержит файлов таких конфигураций");
            }
        } else {
            list.add(dir + ": " + "Данная папка вероятно не сущетсвует");
        }
        if(hasNextChain(searchByParam)){
            return searchByParam.search(parameters,list);
        }
        return list;
    }
}

