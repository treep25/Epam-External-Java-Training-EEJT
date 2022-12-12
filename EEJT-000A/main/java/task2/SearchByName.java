package task2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SearchByName implements SearchByParam {

    private SearchByParam searchByParam;

    public SearchByName(SearchByParam searchByParam) {
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

    @Override
    public List<String> searchFileWhenListWithParamEmpty(Parameters parameters, List<String> paramList) {
        File file = new File(dir);
        List<File> fileList = List.of(file.listFiles());
        if (!fileList.isEmpty()) {
            for (File f : fileList) {
                if (!f.isDirectory()) {
                    fileListMain.add(f);
                } else {
                    searchingThroughTheDirectory(f);
                }
            }
            fileListMain = fileListMain.stream().filter(x1 -> x1.getName().startsWith(parameters.getName())).collect(Collectors.toList());
            if (fileListMain.isEmpty()) {
                paramList.add(dir + ": " + "Данная папка не содержит файлов таких конфигураций");
                return paramList;
            }
            for (File f : fileListMain) {
                paramList.add(f.getAbsolutePath());
            }
        } else {
            paramList.add(dir + ": " + "Данная папка не содержит файлов таких конфигураций");
        }
        return paramList;
    }


    @Override
    public List<String> searchFileWhenListWithParamNotEmpty(Parameters parameters, List<String> paramList) {
        return null;
    }

    @Override
    public List<String> search(Parameters parameters, List<String> paramList) {
        paramList = searchFileWhenListWithParamEmpty(parameters, paramList);
        if (hasNextChain(searchByParam)) {
            return searchByParam.search(parameters, paramList);
        }
        return paramList;
    }
}

