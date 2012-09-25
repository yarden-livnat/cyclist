package cyclist.view.component;

import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ProgressIndicatorBuilder;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import cyclist.Resources;

public class View extends VBox {
	
	public static final double EDGE_SIZE = 4;
	
	public enum Edge { TOP, BOTTOM, LEFT, RIGHT, TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, NONE };
	
	private static final Cursor[] _cursor = {
		Cursor.N_RESIZE, Cursor.S_RESIZE, Cursor.E_RESIZE, Cursor.W_RESIZE, Cursor.NE_RESIZE, Cursor.NW_RESIZE, Cursor.SE_RESIZE, Cursor.SW_RESIZE, Cursor.DEFAULT
	};
	
	private Button _closeButton;
	private Button _minmaxButton;
	
	private Label _title;
	private ProgressIndicator _indicator;
	private ObjectProperty<EventHandler<ActionEvent>> selectPropery = new SimpleObjectProperty<EventHandler<ActionEvent>>();		
	private boolean _maximized = false;
	private HBox _actionsArea;
	private final Resize resize = new Resize();
	
	public View() {	
		super();
//		prefWidth(100);
//		prefHeight(100);
		getStyleClass().add("view");
		
		// Title
		HBox header = HBoxBuilder.create()
				.spacing(2)
				.padding(new Insets(0, 5, 0, 5))
				.styleClass("header")
				.alignment(Pos.CENTER_LEFT)
				.children(
					_title = LabelBuilder.create().build(),
					_indicator = ProgressIndicatorBuilder.create().progress(-1).maxWidth(8).maxHeight(8).visible(false).build(),
					new Spring(),
					_actionsArea = new HBox(),
					_minmaxButton = ButtonBuilder.create().styleClass("flat-button").graphic(new ImageView(Resources.getIcon("maximize"))).build(),
					_closeButton = ButtonBuilder.create().styleClass("flat-button").graphic(new ImageView(Resources.getIcon("close_view"))).build()
				)
				.build();
		setHeaderListeners(header);
		
		getChildren().add(header);
		
		setListeners();
	}
	
	public void setParam(String title) {
		_title.setText(title);
	}
	
	public void setWaiting(boolean value) {
		_indicator.setVisible(value);
	}
	
	public boolean isMaximized() {
		return _maximized;
	}
	
	public void setMaximized(boolean value) {
		if (_maximized != value) {
			_maximized = value;
			_minmaxButton.setGraphic(new ImageView(Resources.getIcon(value ? "restore" : "maximize")));
		}
	}
	
	/*
	 * Max/min button
	 */
	public ObjectProperty<EventHandler<ActionEvent>> onMinmaxProperty() {
		return _minmaxButton.onActionProperty();
	}
	
	public EventHandler<ActionEvent> getOnMinmax() {
		return _minmaxButton.getOnAction();
	}
	
	public void setOnMinmax(EventHandler<ActionEvent> handler) {
		_minmaxButton.setOnAction(handler);
	}
	
	/*
	 * Close 
	 */
	public ObjectProperty<EventHandler<ActionEvent>> onCloseProperty() {
		return _closeButton.onActionProperty();
	}
	
	public EventHandler<ActionEvent> getOnClose() {
		return _closeButton.getOnAction();
	}
	
	public void setOnClose(EventHandler<ActionEvent> handler) {
		_closeButton.setOnAction(handler);
	}
	
	/*
	 * Select
	 */
	public ObjectProperty<EventHandler<ActionEvent>> onSelectProperty() {
		return selectPropery;
	}
	
	public EventHandler<ActionEvent> getOnSelect() {
		return selectPropery.get();
	}
	
	public void setOnSelect(EventHandler<ActionEvent> handler) {
		selectPropery.set(handler);
	}	
	
	/*
	 * Content
	 */
	
	protected void setContent(Node node) {
		node.setOnMouseMoved(_onMouseMove);
		
		if (getChildren().size() > 1)
			getChildren().remove(1);
		getChildren().add(node);
		VBox.setVgrow(node, Priority.ALWAYS);
//		VBox.setVgrow(node, Priority.NEVER);
//		setPrefHeight(200);
	}
	
	/*
	 * 
	 */
	protected void addActions(List<ButtonBase> actions) {
		_actionsArea.getChildren().addAll(actions);
	}
	
	protected void setActions(List<ButtonBase> actions) {
		_actionsArea.getChildren().clear();
		addActions(actions);
	}
	
	/*
	 * Listeners
	 */
	private void setHeaderListeners(HBox header) {
		final View view = this;
		final Delta delta = new Delta();
		
		header.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				delta.x = getTranslateX() - event.getSceneX();
				delta.y = getTranslateY() - event.getSceneY();
				if (selectPropery.get() != null)
					selectPropery.get().handle(new ActionEvent());
			}
		});
		
		header.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Parent parent = view.getParent();
				double maxX = parent.getLayoutBounds().getMaxX() - getWidth();				
				double maxY = parent.getLayoutBounds().getMaxY() - getHeight();
				setTranslateX(Math.min(Math.max(0, delta.x + event.getSceneX()), maxX)) ;
				setTranslateY(Math.min(Math.max(0, delta.y+event.getSceneY()), maxY));
			}
			
		});	
		
		EventHandler<MouseEvent> eh = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (selectPropery.get() != null)
					selectPropery.get().handle(new ActionEvent());
			}
		};
		
		header.setOnMouseClicked(eh);
		setOnMouseClicked(eh);
	}
	

	private void setListeners() {
		
		
		setOnMouseMoved(_onMouseMove);
		setOnMousePressed(_onMousePressed);
		setOnMouseDragged(_onMouseDragged);
		setOnMouseExited(_onMouseExited);
	}
	
	private Edge getEdge(MouseEvent event) {
		double x = event.getX();
		double y = event.getY();
		double right = getWidth() - EDGE_SIZE;
		double bottom = getHeight() - EDGE_SIZE;
		
		Edge edge = Edge.NONE;
		
		if (x < EDGE_SIZE) {
			if (y < EDGE_SIZE) edge = Edge.TOP_LEFT;
			else if (bottom < y) edge = Edge.BOTTOM_LEFT;
			else edge = Edge.LEFT;
		} 
		else if (right < x) {
			if (y < EDGE_SIZE) edge = Edge.TOP_RIGHT;
			else if (bottom < y) edge = Edge.BOTTOM_RIGHT;
			else edge = Edge.RIGHT;			
		}
		else if (y < EDGE_SIZE) edge = Edge.TOP;
		else if (bottom < y) edge = Edge.BOTTOM;
		
		return edge;
	}
	
	private EventHandler<MouseEvent> _onMouseMove = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {
			Edge edge = getEdge(event);
			Cursor c = _cursor[edge.ordinal()];
			if (getCursor() != c)
				setCursor(c);
		}
	};
	
	private EventHandler<MouseEvent> _onMousePressed = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {
			resize.edge = getEdge(event);
			if ( resize.edge != Edge.NONE) {
				resize.x = getTranslateX();
				resize.y = getTranslateY();
				resize.width = getWidth();
				resize.height = getHeight();
				resize.sceneX = event.getSceneX();
				resize.sceneY = event.getSceneY() ;
			}
		}
	};
	
	private EventHandler<MouseEvent> _onMouseDragged = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
        	
        	if (resize.edge == Edge.NONE) {
        		return;
        	}
        	
        	setMaximized(false);
        	
        	double dx = resize.sceneX - event.getSceneX();
        	double dy = resize.sceneY - event.getSceneY();
        	
        	// top/bottom
        	if (resize.edge == Edge.TOP || resize.edge == Edge.TOP_LEFT || resize.edge == Edge.TOP_RIGHT) {
        		setTranslateY(resize.y-dy);
        		setPrefHeight(resize.height+dy);
        	} else if (resize.edge == Edge.BOTTOM || resize.edge == Edge.BOTTOM_LEFT || resize.edge == Edge.BOTTOM_RIGHT){
        		//setTranslateY(resize.y+dy);
        		setPrefHeight(resize.height-dy);           		
        	}
        	
        	// left/right
        	if (resize.edge == Edge.TOP_LEFT || resize.edge == Edge.LEFT || resize.edge == Edge.BOTTOM_LEFT) {
        		setTranslateX(resize.x-dx);
        		setPrefWidth(resize.width+dx);
        	} else if (resize.edge == Edge.TOP_RIGHT || resize.edge == Edge.RIGHT || resize.edge == Edge.BOTTOM_RIGHT){
        		//setTranslateY(resize.y+dy);
        		setPrefWidth(resize.width-dx);
        	}
        }
	};
	
	private EventHandler<MouseEvent> _onMouseExited = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {
			setCursor(Cursor.DEFAULT);
		}
	};
	
	class Delta {
		public double x;
		public double y;
	}

	class Resize {
		public View.Edge edge;
		public double x;
		public double y;
		public double width;
		public double height;
		public double sceneX;
		public double sceneY;
	}
}




