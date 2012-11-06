package cyclist.view.tool.view;

import java.util.List;
import java.util.Map;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.GridPaneBuilder;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.TextBuilder;
import cyclist.model.filter.Param;
import cyclist.model.vo.Facility;
import cyclist.model.vo.MaterialFlow;
import cyclist.view.component.View;

public class TimelineView extends View {

	private ObjectProperty<ObservableList<Facility>> _itemsProperty = new SimpleObjectProperty<>();
	private LineChart<Number, Number> _chart;
	
	public TimelineView() {
		super();
		init();
	}
	
	public ObjectProperty<ObservableList<Facility>> itemsProperty() {
		return _itemsProperty;
	}
	
	public ObservableList<Facility> getItems() {
		return _itemsProperty.get();
	}
	
	public void setItems(ObservableList<Facility> list) {
		_itemsProperty.set(list);
	}
	
	
	private void init() {
		GridPane grid;
		
		_chart = createChart();
		
		VBox vbox = 
			VBoxBuilder.create()
				.spacing(5)
				.children(
						_chart,
						grid = GridPaneBuilder.create()
							.hgap(5)
							.vgap(5)
							.padding(new Insets(0, 0, 0, 0))
							.build()
						
						)
				.build();
		
		VBox.setVgrow(_chart, Priority.ALWAYS);
		grid.getColumnConstraints().add(new ColumnConstraints(50));
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setHgrow(Priority.ALWAYS);
		grid.getColumnConstraints().add(col2);
		
		// filter areas
		grid.add(TextBuilder.create().text("Filters").style("-fx-font-weight: bold").build(), 0, 0);
		addFilterArea(grid, 1, "General:", "general", Param.GENERAL);
		addFilterArea(grid, 2, "Source:", "src", Param.SRC_DEST);
		addFilterArea(grid, 3, "Dest:", "dest", Param.SRC_DEST);
		
		setContent(vbox);
		
		_itemsProperty.addListener(new ChangeListener<ObservableList<Facility>>() {

			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void changed(
					ObservableValue<? extends ObservableList<Facility>> observable,
					ObservableList<Facility> oldValue,
					ObservableList<Facility> newValue) 
			{			
//		        _chart.setData(FXCollections.observableArrayList());
		        if (oldValue == null) setWaiting(false);
				if (newValue == null) return;
				
				setChartItems(newValue);
			}
		});
	}
	
	
	private void setChartItems(ObservableList<Facility> list) {
		XYChart.Series<Number, Number> series = new XYChart.Series<>();
		
		series.setName("Facilities");
		for (Number n : list) {
			series.getData().add(new XYChart.Data<><Number, Number>())
		}
	}
	
	private LineChart<Number, Number> createChart() {
		final NumberAxis xAxis = new NumberAxis();
		final NumberAxis yAxis = new NumberAxis();
		
		xAxis.setLabel("Year");
		xAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(xAxis) {
				@Override
			public String toString(Number object) {
			    return String.format("%4.0f", object);
			}
		});
		xAxis.forceZeroInRangeProperty().set(false);
		yAxis.setLabel("Number of Facilities");
		
		LineChart<Number, Number> chart = new LineChart<Number, Number>(xAxis, yAxis);
		chart.setTitle("Timeline");
		chart.setCreateSymbols(false);
		chart.setLegendSide(Side.RIGHT);
		chart.setLegendVisible(false);
		
		return chart;
	}

}
