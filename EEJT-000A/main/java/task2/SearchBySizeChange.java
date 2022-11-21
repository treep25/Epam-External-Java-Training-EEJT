package task2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SearchBySizeChange implements SearchByParam {
    private SearchByParam searchByParam;

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

    private boolean hasNextChain(SearchByParam searchByParam) {
        return searchByParam != null;
    }

    public SearchBySizeChange(SearchByParam searchByParam) {
        this.searchByParam = searchByParam;
    }

    @Override
    public List<String> search(Parameters parameters, List<String> list) {
        if (list.isEmpty()) {
            File file = new File(dir);
            if (file.exists()) {
                List<File> fileList = List.of(Objects.requireNonNull(file.listFiles()));
                if (!fileList.isEmpty()) {
                    fileList = fileList.stream().filter(x1 -> isFileMatches(x1, parameters)).collect(Collectors.toList());
                    for (File f :
                            fileList) {
                        list.add(dir + File.separator + f.getName() + " (" + f.length() + ")");
                    }
                } else {
                    list.add(dir + ": " + "Данная папка не содержит файлов таких конфигураций");
                }
            } else {
                list.add(dir + ": " + "Данная папка вероятно не сущетсвует");
            }
        } else {
            List<String> list1 = new ArrayList<>();
            for (String res :
                    list) {
                File file = new File(getFileDir(res.toCharArray()));
                if (file.exists()) {
                    if (isFileMatches(file, parameters)) {
                        list1.add(res + "(" + file.length() + ")");
                    }
                }
            }
            list = list1;
        }
        if (hasNextChain(searchByParam)) {
            return searchByParam.search(parameters, list);
        }
        return list;
    }
}

