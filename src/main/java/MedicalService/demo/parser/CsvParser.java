package MedicalService.demo.parser;

import java.io.InputStream;
import java.io.Writer;
import java.util.List;

public abstract class CsvParser<T> {
    Class<T> clazz;

    public abstract List<T> parse(InputStream input);

    public abstract void beanToCSV(List<T> applications, Writer writer);
}
