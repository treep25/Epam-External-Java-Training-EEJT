package task2;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SearchByExtension implements SearchByParam {
    private SearchByParam searchByParam;

    public SearchByExtension(SearchByParam searchByParam) {
        this.searchByParam = searchByParam;
    }
    private boolean hasNextChain(SearchByParam searchByParam){
        return searchByParam != null;
    }


    @Override
    public List<String> search(Parameters parameters, List<String> list) {
        if(list.isEmpty()){
            File file = new File(dir);
            if (file.exists()) {
                List <File> fileList = List.of(Objects.requireNonNull(file.listFiles(
                        (x1,x2)->x2.toLowerCase().endsWith(parameters.getExt()))));
                if(!fileList.isEmpty()){
                    for (File f : fileList){
                        list.add(dir+File.separator+f.getName());
                    }
                }else{
                    list.add(dir + ": " + "Данная папка не содержит файлов с расширением"+ parameters.getExt());
                }
            } else {
                list.add(dir + ": " + "Данная папка вероятно не сущетсвует");
            }
        }else{
            list = list.stream().filter(x1->x1.endsWith(parameters.getExt().toLowerCase())).collect(Collectors.toList());
        }
        if(hasNextChain(searchByParam)){
            return searchByParam.search(parameters,list);
        }
        return list;
    }
}
