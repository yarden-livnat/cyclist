package cyclist;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import cyclist.model.vo.ToolInfo;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

public class Resources {

	private static Map<String, Image> _icons = new HashMap<String, Image>();
	
	public static Image getIcon(String name) {
		return getIcon(name, -1, -1);
	}
	
	public static Image getIcon(String name, double width, double height) {
		String fullname =  name.contains(".") ? name : name+".png";
		Image image = _icons.get(fullname);
		if (image == null) {
			InputStream is = Resources.class.getResourceAsStream("assets/icons/"+fullname);
			if (is == null)
				is = Resources.class.getResourceAsStream("assets/icons/unknown.png");
			if (width > 0)
				image = new Image(is, width, height, true, true);
			else
				image = new Image(is);
			_icons.put(fullname, image);
		}
		return image;
	}
	
	public static void clean() {
		_icons = new HashMap<String, Image>();
	}
	
	public static ObservableList<ToolInfo> getToolsInfo() {
		ObservableList<ToolInfo> list = FXCollections.observableArrayList(
			new ToolInfo("Agents", "table", 
					"cyclist.view.component.tools.AgentView", "Agents",
					"cyclist.view.mediator.AgentMediator", null),
			new ToolInfo("Markets", "table", 
					"cyclist.view.component.tools.AgentView", "Markets",
					"cyclist.view.mediator.AgentMediator", "Market"),
			new ToolInfo("Regions", "table", 
					"cyclist.view.component.tools.AgentView", "Regions",
					"cyclist.view.mediator.AgentMediator", "Region"),
			new ToolInfo("Institution", "table", 
					"cyclist.view.component.tools.AgentView", "Institutions",
					"cyclist.view.mediator.AgentMediator", "Institute"),
			new ToolInfo("Facilities", "table", 
					"cyclist.view.component.tools.FacilityView", "Facilities",
					"cyclist.view.mediator.FacilityMediator", "Facility"),						
			new ToolInfo("Flow", "chart_bar", 
					"cyclist.view.component.tools.FlowView", "Material Flow",
					"cyclist.view.mediator.FlowMediator", "")	,
			new ToolInfo("Isotopes", "unknown", 
					"cyclist.view.component.tools.IsotopesView", "Active Isotopes",
					"cyclist.view.mediator.IsotopesMediator", "")	, 
			new ToolInfo("Elements", "unknown", 
					"cyclist.view.component.tools.ElementView", "Elements",
					"cyclist.view.mediator.CyclistMediator", "")
		);
		
		return list;
	}
}
