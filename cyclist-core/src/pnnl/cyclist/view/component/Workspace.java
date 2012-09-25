package cyclist.view.component;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import cyclist.model.vo.DnD;
import cyclist.view.event.CyclistDropEvent;

public class Workspace extends Pane {

	public static final String WORKSPACE_ID = "workspace";
		
	// -- Properties
	
	// OnToolDrop
	private ObjectProperty<EventHandler<CyclistDropEvent>> _propertyOnToolDrop = new SimpleObjectProperty<EventHandler<CyclistDropEvent>>();
	
	public final ObjectProperty<EventHandler<CyclistDropEvent>> onToolDropPropery() {
		return _propertyOnToolDrop;
	}
	
	public final void setOnToolDrop(EventHandler<CyclistDropEvent> eventHandler) {
		_propertyOnToolDrop.set(eventHandler);
	}
	
	public final EventHandler<CyclistDropEvent> getOnToolDrop() {
		return _propertyOnToolDrop.get();
	}
	
	
	/**
	 * Constructor
	 */
	public Workspace() {
		getStyleClass().add("workspace");
		setPadding(new Insets(5, 10, 5, 10));

		final Workspace workspace = this;
		
		setOnDragOver(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
//				System.out.println("workspace over: \n\tsrc:"+event.getSource()+"\n\ttarget: "+event.getTarget());
//				System.out.println("gesture \n\tsrc:"+event.getGestureSource()+"\n\ttarget: "+event.getGestureTarget());
				if (event.getGestureSource() != workspace && 
					//event.getTarget() == workspace &&
					event.getDragboard().getContent(DnD.TOOL_FORMAT) != null) 
				{
					event.acceptTransferModes(TransferMode.COPY);
				} 
				
				event.consume();
			}
		});	
		
		setOnDragEntered(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {		
//				System.out.println("workspace enter: \n\tsrc:"+event.getSource()+"\n\ttarget: "+event.getTarget());
				event.consume();
			}
		});
		
		setOnDragExited(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
//				System.out.println("worspace exit");
				// do nothing
				event.consume();
			}
		});
		
		setOnDragDropped(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
//				System.out.println("dropped on workspace");
				if (event.getGestureSource() != this) {
					if (event.getDragboard().hasContent(DnD.TOOL_FORMAT)) {
						String toolName = (String) event.getDragboard().getContent(DnD.TOOL_FORMAT);
						
						onToolDropPropery().get().handle(
								new CyclistDropEvent(CyclistDropEvent.DROP, toolName, event.getX(), event.getY()));
					}
				
				}
				event.setDropCompleted(true);
				
				event.consume();
			}
		});
		
	}
	
	public void addView(final View view) {
		getChildren().add(view);
		
		view.setOnSelect(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {	
				view.toFront();
			}
		});
		
		view.setOnMinmax(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (view.isMaximized()) {
					view.setTranslateX(_viewPos.x);
					view.setTranslateY(_viewPos.y);
					view.setPrefSize(_viewPos.width, _viewPos.height);
					
					view.setMaximized(false);
					
				} else {
					_viewPos.x = view.getTranslateX();
					_viewPos.y = view.getTranslateY();
					_viewPos.width = view.getWidth();
					_viewPos.height = view.getHeight();
		
					view.setTranslateX(0);
					view.setTranslateY(0);
					Bounds b = getLayoutBounds();
					view.setPrefSize(b.getWidth(), b.getHeight());
					
					view.toFront();
					
					view.setMaximized(true);
				}
					
			}
		});
	}
	
	public void removeView(View view) {
		view.setOnSelect(null);
		getChildren().remove(view);
	}
	
	private ViewPos _viewPos = new ViewPos();
}

class ViewPos {
	public double x;
	public double y;
	public double width;
	public double height;
}