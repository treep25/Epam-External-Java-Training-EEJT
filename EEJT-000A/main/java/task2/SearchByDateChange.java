package task2;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class SearchByDateChange implements SearchByParam {

    private SearchByParam searchByParam;
    private final String Patt = "yyyy-MM-dd HH:mm:ss";

    public SearchByDateChange(SearchByParam searchByParam) {
        this.searchByParam = searchByParam;
    }
    private boolean isNextChain (SearchByParam searchByParam){
        return searchByParam != null;
    }
    private boolean isFileMatches(File file , Parameters parameters){
        return file.lastModified() <= Timestamp.valueOf(parameters.getDateMore()).getTime()
                && file.lastModified() >= Timestamp.valueOf(parameters.getDateLess()).getTime() ;
    }

    @Override
    public List<String> search(Parameters parameters, List<String> list) {
        if (list.isEmpty()) {
            File file = new File(dir);
            if (file.exists()) {
                List<File> fileList = List.of(Objects.requireNonNull(file.listFiles()));
                if (!fileList.isEmpty()) {
                    fileList =fileList.stream().filter(x1 -> isFileMatches(x1,parameters)).collect(Collectors.toList());
                    for (File f :
                            fileList) {
                        Date date = new Date(f.lastModified());
                        SimpleDateFormat sd = new SimpleDateFormat(Patt);
                        list.add(dir + File.separator + f.getName() + " (" + sd.format(date) + ")");
                    }
                } else {
                    list.add(dir + ": " + "Данная папка не содержит файлов таких конфигураций");
                }
            } else {
                list.add(dir + ": " + "Данная папка вероятно не сущетсвует");
            }
        } else {
            List<String> list1 = new ArrayList<>();
            list.forEach(x1 -> {
                File file = new File(x1);
                if (file.exists()) {
                    if (isFileMatches(file,parameters)) {
                        Date date = new Date(file.lastModified());
                        SimpleDateFormat sd = new SimpleDateFormat(Patt);
                        list1.add(dir + File.separator + file.getName() + " (" + sd.format(date) + ")" );
                    }
                }
            });
            list = list1;
        }
        if(isNextChain(searchByParam)){
            return searchByParam.search(parameters,list);
        }
        return list;
    }
}

