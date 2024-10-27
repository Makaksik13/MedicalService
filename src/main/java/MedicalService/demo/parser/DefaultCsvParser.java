package MedicalService.demo.parser;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.MappingStrategy;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.BOMInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class DefaultCsvParser<T> extends CsvParser<T> {

    public DefaultCsvParser(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public List<T> parse(InputStream input) {
        MappingStrategy<T> mappingStrategy = this.getMappingStrategy();

        try (Reader reader = new InputStreamReader(new BOMInputStream(input), StandardCharsets.UTF_8)) {
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                    .withFilter(stringValues -> stringValues != null &&
                            stringValues[2] != null &&
                            stringValues[2].length() >= 3)
                    .withType(this.clazz)
                    .withSeparator(',')
                    .withMappingStrategy(mappingStrategy)
                    .build();

            return csvToBean.parse();
        } catch (IOException e){
            log.error("Exception was caught", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void beanToCSV(List<T> applications, Writer writer){
        try (writer) {
            var builder = new StatefulBeanToCsvBuilder<T>(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator(',')
                    .build();

            builder.write(applications);
        } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException | IOException e) {
            log.error("Exception was caught", e);
            throw new RuntimeException(e);
        }
    }

    protected MappingStrategy<T> getMappingStrategy() {
        MappingStrategy<T> result = new ColumnPositionMappingStrategy<>();
        result.setType(this.clazz);
        return result;
    }
}
