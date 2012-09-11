package cyclist.model.proxy;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.puremvc.java.multicore.patterns.proxy.Proxy;

import com.csvreader.CsvReader;

public class WeatherProxy extends Proxy {

	private String _filename;
	private CsvReader _reader;
	
	public WeatherProxy(String name) {
		super(name);
	}
	
	public void setDataSource(String name) {
		_filename = name;
		try {
			_reader = new CsvReader(name);
			_reader.readHeaders();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
