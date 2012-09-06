package cyclist.view.tool.component;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleButtonBuilder;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.RegionBuilder;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;

public class Legend extends VBox {
	
	private Text _title;
	private ObjectProperty<XYChart<?,?>> _chartProperty = new SimpleObjectProperty<>();
	
//	private Color[] colors = {Color.web("#f9d900"), Color.web("#a9e200"), Color.web("#22bad9"), Color.web("#0181e2"), Color.web("#2f357f") };

	public Legend() {
		getStyleClass().add("chart-legend");
		setPrefWidth(150);
		
		_title = TextBuilder.create().text("Legend").style("-fx-font-weight: bold").build();
		getChildren().add(_title); 
		
		chartProperty().addListener(new ChangeListener<XYChart<?,?>>()  {

			@Override
			public void changed(ObservableValue<? extends XYChart<?, ?>> observable, XYChart<?, ?> oldValue, XYChart<?, ?> newValue) {
//				System.out.println("chart changed: \n\t"+observable+"\n\t"+oldValue+"\n\t"+newValue);
				if (newValue != null) {
					newValue.dataProperty().addListener(new InvalidationListener() {
						
						@Override
						public void invalidated(Observable observable) {
//							System.out.println("data changed: "+observable);
							observable.addListener(new InvalidationListener() {
								
								@Override
								public void invalidated(Observable observable) {
//									System.out.println("series changed: "+observable);
									
								}
							});
						}
					});
				}
			}
		});
	}
	
	public ObjectProperty<XYChart<?,?>> chartProperty() {
		return _chartProperty;
	}
	
	public XYChart<?,?> getChart() {
		return _chartProperty.get();
	}
	
	public void setChart(XYChart<?,?> chart) {
		_chartProperty.set(chart);
	}
	
	public void setSeries(ObservableList<? extends XYChart.Series<?, ?>> list) {
		getChildren().clear();
		getChildren().add(_title);
	
		int i=0;
		for (Series<?, ?> series : list) {
			
			if (series.getNode() instanceof Node) {
				final Node g = series.getNode();
//				System.out.println("g classStyle: "+g.getStyleClass());		
				
				g.setOnMouseEntered(new EventHandler<MouseEvent>() {
	
					@Override
					public void handle(MouseEvent event) {
//						System.out.println("Enter: "+index);
						DropShadow ds = new DropShadow();
						ds.setOffsetX(4);
						ds.setOffsetY(4);
						
						g.setEffect(ds);
					}
				});
				
				g.setOnMouseExited(new EventHandler<MouseEvent>() {
					
					@Override
					public void handle(MouseEvent event) {
						DropShadow ds = new DropShadow();
						ds.setOffsetX(4);
						ds.setOffsetY(4);
						
						g.setEffect(null);
					}
				});
			}
			
			Region symbol = RegionBuilder.create()
				.prefWidth(8).prefHeight(8)
				.minWidth(USE_PREF_SIZE).minHeight(USE_PREF_SIZE)
				.styleClass("legend-symbol"+i)
				.build();
			
			final ToggleButton button = ToggleButtonBuilder.create()
					.styleClass("flat-toggle-button")
					.text(series.getName())
					.graphic(symbol)
					.build();

			getChildren().add(button);
			i++;
		}
	}
}
