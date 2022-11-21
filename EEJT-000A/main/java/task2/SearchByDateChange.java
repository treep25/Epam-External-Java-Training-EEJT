package task2;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class SearchByDateChange implements SearchByParam {

    private SearchByParam searchByParam;
    private final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public SearchByDateChange(SearchByParam searchByParam) {
        this.searchByParam = searchByParam;
    }

    private boolean hasNextChain(SearchByParam searchByParam) {
        return searchByParam != null;
    }

    private boolean isFileMatches(File file, Parameters parameters) {
        return file.lastModified() <= Timestamp.valueOf(parameters.getDateMore()).getTime()
                && file.lastModified() >= Timestamp.valueOf(parameters.getDateLess()).getTime();
    }

    private List<String> isTheFileFit(List<File> fileList, Parameters parameters) {
        List<String> list = new ArrayList<>();
        fileList = fileList.stream().filter(x1 -> isFileMatches(x1, parameters)).collect(Collectors.toList());
        for (File f :
                fileList) {
            Date date = new Date(f.lastModified());
            SimpleDateFormat sd = new SimpleDateFormat(DATE_PATTERN);
            list.add(dir + File.separator + f.getName() + " (" + sd.format(date) + ")");
        }
        return list;
    }

    @Override
    public List<String> search(Parameters parameters, List<String> list) {
        if (list.isEmpty()) {
            File file = new File(dir);
            if (file.exists()) {
                List<File> fileList = List.of(Objects.requireNonNull(file.listFiles()));
                if (!fileList.isEmpty()) {
                    list = isTheFileFit(fileList, parameters);
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
                    if (isFileMatches(file, parameters)) {
                        Date date = new Date(file.lastModified());
                        SimpleDateFormat sd = new SimpleDateFormat(DATE_PATTERN);
                        list1.add(dir + File.separator + file.getName() + " (" + sd.format(date) + ")");
                    }
                }
            });
            list = list1;
        }
        if (hasNextChain(searchByParam)) {
            return searchByParam.search(parameters, list);
        }
        return list;
    }
}

