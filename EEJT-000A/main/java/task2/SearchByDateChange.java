package task2;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class SearchByDateChange implements SearchByParam {

    private SearchByParam searchByParam;
    private final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public SearchByDateChange(SearchByParam searchByParam) {
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

    private boolean isFileMatches(File file, Parameters parameters) {
        return file.lastModified() <= Timestamp.valueOf(parameters.dateMore()).getTime()
                && file.lastModified() >= Timestamp.valueOf(parameters.dateLess()).getTime();
    }

    List<File> fileListMain = new ArrayList<>();

    private void searchingThroughTheDirectory(File file) {
        if (file.isDirectory()) {
            List<File> fileList = List.of(file.listFiles());
            if (!fileList.isEmpty()) {
                for (File f : fileList) {
                    if (!f.isDirectory()) {
                        fileListMain.add(f);
                    } else {
                        searchingThroughTheDirectory(f);
                    }
                }
            }

        }
    }

    private List<String> isTheFileFit(List<File> fileList, Parameters parameters) {
        List<String> paramList = new ArrayList<>();
        if (!fileList.isEmpty()) {
            for (File f : fileList) {
                if (!f.isDirectory()) {
                    fileListMain.add(f);
                } else {
                    searchingThroughTheDirectory(f);
                }
            }
            fileListMain = fileListMain.stream().filter(x1 -> isFileMatches(x1, parameters)).collect(Collectors.toList());
            if (fileListMain.isEmpty()) {
                paramList.add(dir + ": " + "Данная папка не содержит файлов таких конфигураций");
                return paramList;
            }
            for (File f : fileListMain) {
                Date date = new Date(f.lastModified());
                SimpleDateFormat sd = new SimpleDateFormat(DATE_PATTERN);
                paramList.add(f.getAbsolutePath() + " (" + sd.format(date) + ")");
            }
        } else {
            paramList.add(dir + ": " + "Данная папка не содержит файлов таких конфигураций");
        }
        return paramList;
    }

    @Override
    public List<String> searchFileWhenListWithParamEmpty(Parameters parameters, List<String> paramList) {
        File file = new File(dir);
        List<File> fileList = List.of(file.listFiles());

        return isTheFileFit(fileList, parameters);
    }

    @Override
    public List<String> searchFileWhenListWithParamNotEmpty(Parameters parameters, List<String> paramList) {
        List<String> newParamList = new ArrayList<>();
        paramList.forEach(x1 -> {
            File file = new File(x1);
            if (file.exists()) {
                if (isFileMatches(file, parameters)) {
                    Date date = new Date(file.lastModified());
                    SimpleDateFormat sd = new SimpleDateFormat(DATE_PATTERN);
                    newParamList.add(file.getAbsolutePath() + " (" + sd.format(date) + ")");
                }
            }
        });
        return newParamList;
    }


    @Override
    public List<String> search(Parameters parameters, List<String> paramList) {
        if (paramList.isEmpty()) {
            paramList = searchFileWhenListWithParamEmpty(parameters, new ArrayList<>());
        } else {
            paramList = searchFileWhenListWithParamNotEmpty(parameters, paramList);
        }
        if (hasNextChain(searchByParam)) {
            return searchByParam.search(parameters, paramList);
        }
        return paramList;
    }
}

