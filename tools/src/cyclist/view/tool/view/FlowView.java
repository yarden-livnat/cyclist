package cyclist.view.tool.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ToggleButtonBuilder;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.GridPaneBuilder;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;
import cyclist.Resources;
import cyclist.model.filter.Param;
import cyclist.model.vo.Details;
import cyclist.model.vo.FilterSet;
import cyclist.model.vo.MaterialFlow;
import cyclist.view.component.View;
import cyclist.view.tool.component.DetailsPane;
import cyclist.view.tool.component.FilterSetView;
import cyclist.view.tool.component.Legend;

public class FlowView extends View {

	public static final String TITLE = "Material Flow";
	
	private ObjectProperty<XYChart<?, ?>> _chartProperty = new SimpleObjectProperty<>();
	
	private List<FilterSet> _filters = new ArrayList<>();
	private DetailsPane _detailsPane;
	private HBox _chartParent;
	private Legend _legend;
	
	private ObjectProperty<Map<String, List<MaterialFlow>>> _itemsProperty = new SimpleObjectProperty<>();
	
	final private ObjectProperty<EventHandler<ActionEvent>> actionPropery = new SimpleObjectProperty<EventHandler<ActionEvent>>();
	 
	
	public ObjectProperty<XYChart<?,?>> chartProperty() {
		return _chartProperty;
	}
	
	public XYChart<?, ?> getChart() {
		return chartProperty().get();
	}
	
	public void setChart(XYChart<?,?> chart) {
		chartProperty().set(chart);
	}
	
	/**
	 * onActionProperty
	 */
	public ObjectProperty<EventHandler<ActionEvent>> onActionProperty() {
		return actionPropery;
	}
	
	/**
	 * getOnAction
	 * @return
	 */
	public EventHandler<ActionEvent> getOnAction() {
		return actionPropery.get();
	}
	
	/**
	 * setOnAction
	 * @param handler
	 */
	public void setOnAction(EventHandler<ActionEvent> handler) {
		actionPropery.set(handler);
	}	
	
	/**
	 * Constructor
	 */
	public FlowView() {
		super();
		prefWidth(400);
		prefHeight(400);
		init();
	}
	
	/**
	 * itemsProperty
	 * @return
	 */
	public ObjectProperty<Map<String, List<MaterialFlow>>> itemsProperty() {
		return _itemsProperty;
	}
	
	/**
	 * getFilters
	 * @return
	 */
	public List<FilterSet> getFilters() {
		return _filters;
	}
	
	/**
	 * getDetails
	 * @return
	 */
	public Details getDetails() {
		return _detailsPane.getDetails();
	}
	
	private void init() {
		initActions();
		
		GridPane grid; 

		_detailsPane = new DetailsPane();
		_legend= new Legend();
		_legend.chartProperty().bind(chartProperty());
		setChart(createLineChart());
		
		VBox vbox = VBoxBuilder.create()
						.children(
							_chartParent = HBoxBuilder.create()
											.children(
												VBoxBuilder.create()
													.spacing(5)
													.children(
														_detailsPane,
														_legend
												)
												.build(),
												getChart()
											)
											.build(),
							VBoxBuilder.create()
								.styleClass("chart-legend")
								.children(
									grid = GridPaneBuilder.create()
										.hgap(5)
										.vgap(5)
										.padding(new Insets(0, 0, 0, 0))
										.build()
										
								)
								.build())
						.build();
		
		HBox.setHgrow(getChart(), Priority.ALWAYS);
		VBox.setVgrow(_detailsPane, Priority.NEVER);
		grid.getColumnConstraints().add(new ColumnConstraints(50));
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setHgrow(Priority.ALWAYS);
		grid.getColumnConstraints().add(col2);
		
		// filter areas
		grid.add(TextBuilder.create().text("Filters").style("-fx-font-weight: bold").build(), 0, 0);
		addFilterArea(grid, 1, "General:", "general", Param.GENERAL);
		addFilterArea(grid, 2, "Source:", "src", Param.SRC_DEST);
		addFilterArea(grid, 3, "Dest:", "dest", Param.SRC_DEST);

		// details
		_detailsPane.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {	
				if (onActionProperty().get() != null)
					onActionProperty().get().handle(event);
			}		
		});
		setContent(vbox);
		
		_itemsProperty.addListener(new ChangeListener<Map<String, List<MaterialFlow>>>() {

			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void changed(
					ObservableValue<? extends Map<String, List<MaterialFlow>>> observable,
					Map<String, List<MaterialFlow>> oldValue,
					Map<String, List<MaterialFlow>> newValue) 
			{			
		        ((XYChart)getChart()).setData(FXCollections.observableArrayList());
		        if (oldValue == null) setWaiting(false);
				if (newValue == null) return;
				
				setChartItems(newValue);
			}
		});
	}

	private void setChartItems(Map<String, List<MaterialFlow>> map) {
		 getChart().setLegendVisible(false); //map != null && (map.keySet().size() > 1 || !map.keySet().contains("default")));
		 
		 String[] keys = map.keySet().toArray(new String [0]);
		 Arrays.sort(keys);
		 if (getChart() instanceof StackedBarChart) {
			 List<Integer> cat = new ArrayList<Integer>();
			 for (String key : keys) {
				 for (MaterialFlow item : map.get(key)) {
					 if (!cat.contains(item.time)) cat.add(item.time);
				 }
			 }
			 Integer[] array = cat.toArray(new Integer[0]);
			 Arrays.sort(array);
			 
			 List<String> categories = new ArrayList<String>();
			 for (Integer i: array) {
				 categories.add(i.toString());
			 }
			 
			 ((CategoryAxis)getChart().getXAxis()).setCategories(FXCollections.<String>observableArrayList(categories));
			 
			 for (String key : keys) {
				XYChart.Series<String, Number> series = new XYChart.Series<>();
				series.setName(key);
	
				for (MaterialFlow item : map.get(key)) {
					Integer i = item.time;
					series.getData().add(new XYChart.Data<String, Number>(i.toString(), item.quantity));
				}

				((StackedBarChart<String, Number>)getChart()).getData().add(series);
			}
		 } else {
			 for (String key : keys) {
				XYChart.Series<Number, Number> series = new XYChart.Series<>();
				series.setName(key);
	
				for (MaterialFlow item : map.get(key)) {
					series.getData().add(new XYChart.Data<Number, Number>(item.time, item.quantity));
				}
	
				((XYChart<Number, Number>) getChart()).getData().add(series);
			}
		 }
		 
		 Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				_legend.setSeries(getChart().getData());	
			}
		});		 
	}


	private XYChart<Number, Number> createLineChart() {
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
		yAxis.setLabel("ktMH Ejected");
		
		LineChart<Number, Number> chart = new LineChart<Number, Number>(xAxis, yAxis);
		chart.setTitle("Material Flow");
		chart.setCreateSymbols(false);
		chart.setLegendSide(Side.RIGHT);
		chart.setLegendVisible(false);

		return chart;
	}
	
	private XYChart<Number, Number> createStackedAreaChart() {
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
		yAxis.setLabel("ktMH Ejected");
		
		StackedAreaChart<Number, Number> chart = new StackedAreaChart<Number, Number>(xAxis, yAxis);
		chart.setTitle("Material Flow");
//		chart.setCreateSymbols(false);
		chart.setLegendSide(Side.RIGHT);
		chart.setLegendVisible(false);

		return chart;
	}
	
	private XYChart<String, Number> createStackedBarChart() {
		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		
		xAxis.setLabel("Year");
		yAxis.setLabel("ktMH Ejected");
		
		StackedBarChart<String, Number> chart = new StackedBarChart<String, Number>(xAxis, yAxis);
		chart.setTitle("Material Flow");
		chart.setLegendSide(Side.RIGHT);
		chart.setLegendVisible(false);
		chart.setCategoryGap(2);
		return chart;
	}
	
	/*
	 * FilterAreas
	 */
	private void addFilterArea(GridPane grid, int row, String title, String type, EnumSet<Param.Type> acceptableValues) {
		Text text = TextBuilder.create().text(title).styleClass("filter-area-title").build();
		FilterSetView area = new FilterSetView(type, acceptableValues);
		_filters.add(area.getFilterSet());
		
		HBox.setHgrow(area, Priority.ALWAYS);
		
		grid.add(text, 0, row);
		grid.add(area, 1, row);
		
		area.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {	
				if (onActionProperty().get() != null)
					onActionProperty().get().handle(event);
			}		
		});
	}
	
	private void initActions() {
		List<ButtonBase> actions = new ArrayList<ButtonBase>();
		
		final ToggleGroup group = new ToggleGroup();
		
		actions.add(ToggleButtonBuilder.create()
						.styleClass("flat-toggle-button")
						.toggleGroup(group)
						.selected(true)
						.graphic(new ImageView(Resources.getIcon("line")))
						.onAction(new EventHandler<ActionEvent>() {
							
							@Override
							public void handle(ActionEvent event) {
								replaceChart(createLineChart());
								
							}
						})
						.build());
		
		actions.add(ToggleButtonBuilder.create()
				.styleClass("flat-toggle-button")
				.graphic(new ImageView(Resources.getIcon("bar_stacked")))
				.toggleGroup(group)
				.onAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event) {
						replaceChart(createStackedAreaChart());
					}
				})
				.build());
		
		actions.add(ToggleButtonBuilder.create()
				.styleClass("flat-toggle-button")
				.graphic(new ImageView(Resources.getIcon("chart_bar")))
				.toggleGroup(group)
				.onAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent event) {
						replaceChart(createStackedBarChart());
					}
				})
				.build());
		
		addActions(actions);
	}
	
	private void replaceChart(XYChart<?, Number> chart) {
		_chartParent.getChildren().set(_chartParent.getChildren().indexOf(getChart()), chart);
		setChart(chart);
		HBox.setHgrow(getChart(), Priority.ALWAYS);
		setChartItems(_itemsProperty.get());
	}
}
