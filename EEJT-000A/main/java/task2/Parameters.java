package task2;

public class Parameters {
    private String name;
    private String ext;
    private String sizeLess;
    private String sizeMore;
    private String dateLess;
    private String dateMore;

    public Parameters() {
    }

    public String getName() {
        return name;
    }

    public Parameters setName(String name) {
        this.name = name;
        return this;
    }

    public String getExt() {
        return ext;
    }

    public Parameters setExt(String ext) {
        this.ext = ext;
        return this;
    }

    public String getSizeLess() {
        return sizeLess;
    }

    public Parameters setSizeLess(String sizeLess) {
        this.sizeLess = sizeLess;
        return this;
    }

    public String getSizeMore() {
        return sizeMore;
    }

    public Parameters setSizeMore(String sizeMore) {
        this.sizeMore = sizeMore;
        return this;
    }

    public String getDateLess() {
        return dateLess;
    }

    public Parameters setDateLess(String dateLess) {
        this.dateLess = dateLess;
        return this;
    }

    public String getDateMore() {
        return dateMore;
    }

    public Parameters setDateMore(String dateMore) {
        this.dateMore = dateMore;
        return this;
    }
}
