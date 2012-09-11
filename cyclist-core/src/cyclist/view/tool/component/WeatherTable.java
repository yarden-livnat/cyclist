package cyclist.view.tool.component;

import javafx.scene.control.TableView;
import javafx.scene.control.TableViewBuilder;
import cyclist.model.vo.Weather;
import cyclist.view.component.View;

public class WeatherTable extends View {
	
	private TableView<Weather> _table;
	
	public WeatherTable() {
		super();
		init();
	}
	
	private void init() {
		_table = TableViewBuilder.<Weather>create()
				.columnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY)
				.build();
	}

}
