package task2;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SearchBySizeChange implements SearchByParam {
    private SearchByParam searchByParam;

    public SearchBySizeChange(SearchByParam searchByParam) {
        try {
            if (!new File(dir).exists()) {
                throw new FileNotFoundException();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
        this.searchByParam = searchByParam;
    }

    private String getFileDir(char[] ar) {
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : ar) {
            if (c != ' ') {
                stringBuilder.append(c);
            } else {
                break;
            }
        }
        return stringBuilder.toString();
    }

    private boolean isFileMatches(File file, Parameters parameters) {
        return file.length() >= Integer.parseInt(parameters.getSizeLess())
                && file.length() <= Integer.parseInt(parameters.getSizeMore());
    }

    private List<String> isTheFileFit(List<File> fileList, Parameters parameters) {
        List<String> list = new ArrayList<>();
        fileList = fileList.stream().filter(x1 -> isFileMatches(x1, parameters)).collect(Collectors.toList());
        for (File f : fileList) {
            list.add(dir + File.separator + f.getName() + " (" + f.length() + ")");
        }
        return list;
    }

    private boolean hasNextChain(SearchByParam searchByParam) {
        return searchByParam != null;
    }

    @Override
    public List<String> searchFileWhenListWithParamEmpty(Parameters parameters, List<String> paramList) {
        File file = new File(dir);
        List<File> fileList = List.of(Objects.requireNonNull(file.listFiles()));
        if (!fileList.isEmpty()) {
            paramList = isTheFileFit(fileList, parameters);
        } else {
            paramList.add(dir + ": " + "Данная папка не содержит файлов таких конфигураций");
        }
        return paramList;
    }

    @Override
    public List<String> searchFileWhenListWithParamNotEmpty(Parameters parameters, List<String> paramList) {
        List<String> newParamList = new ArrayList<>();
        for (String res :
                paramList) {
            File file = new File(getFileDir(res.toCharArray()));
            if (file.exists()) {
                if (isFileMatches(file, parameters)) {
                    newParamList.add(res + "(" + file.length() + ")");
                }
            }
        }
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

