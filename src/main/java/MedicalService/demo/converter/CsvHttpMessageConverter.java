package MedicalService.demo.converter;

import MedicalService.demo.parser.CsvParser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CsvHttpMessageConverter<T> extends AbstractGenericHttpMessageConverter<List<T>> {

    private final CsvParser<T> csvParser;

    public CsvHttpMessageConverter(MediaType supportedMediaType, CsvParser<T> csvParser) {
        super(supportedMediaType);
        this.csvParser = csvParser;
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return List.class.isAssignableFrom(clazz);
    }

    @Override
    protected void writeInternal(List<T> ts, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        HttpHeaders headers = outputMessage.getHeaders();
        MediaType contentType = headers.getContentType();
        Charset charset = null;
        if (contentType != null) {
            charset = contentType.getCharset();
        }

        charset = charset == null ? StandardCharsets.UTF_8 : charset;

        OutputStream body = outputMessage.getBody();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(body, charset);
        csvParser.beanToCSV(ts, outputStreamWriter);
    }

    @Override
    public List<T> read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {

        return this.csvParser.parse(inputMessage.getBody());
    }

    @Override
    protected List<T> readInternal(Class<? extends List<T>> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {

        return this.csvParser.parse(inputMessage.getBody());
    }

}
