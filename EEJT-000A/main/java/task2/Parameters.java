package task2;

import java.util.Objects;

public record Parameters(String name, String ext, String dateLess, String dateMore, String sizeLess, String sizeMore) {
    public Parameters {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Parameters that)) {
            return false;
        }
        return Objects.equals(getName(), that.getName()) && Objects.equals(ext, that.ext) && Objects.equals(sizeLess, that.sizeLess) && Objects.equals(sizeMore, that.sizeMore) && Objects.equals(dateLess, that.dateLess) && Objects.equals(dateMore, that.dateMore);
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (ext != null ? ext.hashCode() : 0);
        result = 31 * result + (sizeLess != null ? sizeLess.hashCode() : 0);
        result = 31 * result + (sizeMore != null ? sizeMore.hashCode() : 0);
        result = 31 * result + (dateLess != null ? dateLess.hashCode() : 0);
        result = 31 * result + (dateMore != null ? dateMore.hashCode() : 0);
        return result;
    }

    public String getName() {
        return name;
    }

    @Override
    public String ext() {
        return ext;
    }

    @Override
    public String sizeLess() {
        return sizeLess;
    }

    @Override
    public String sizeMore() {
        return sizeMore;
    }

    @Override
    public String dateLess() {
        return dateLess;
    }

    @Override
    public String dateMore() {
        return dateMore;
    }

}
