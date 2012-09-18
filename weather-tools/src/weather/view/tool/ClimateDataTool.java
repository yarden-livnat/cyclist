package weather.view.tool;

import javafx.scene.image.Image;

import org.puremvc.java.multicore.patterns.mediator.Mediator;

import weather.view.mediator.ClimateDataMediator;
import weather.view.tool.component.ClimateDataView;
import weather.view.tool.component.GenericTableView;

import cyclist.Resources;
import cyclist.view.component.View;
import cyclist.view.tool.Tool;

public class ClimateDataTool implements Tool {

	private String _title;
	private String _param;
	
	public ClimateDataTool(String title, String param) {
		_title = title;
		_param = param;
	}
	
	@Override
	public Image getIcon() {
		return Resources.getIcon("table");
	}

	@Override
	public String getName() {
		return _title;
	}

	@Override
	public View getView() {
		View view = new GenericTableView();
		view.setParam(_title);
		return view;
	}

	@Override
	public Mediator getMediator() {
		ClimateDataMediator mediator = new ClimateDataMediator(_param);
		return mediator;
	}

}
