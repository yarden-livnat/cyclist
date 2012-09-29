package weather.view.tool;

import javafx.scene.image.Image;

import org.puremvc.java.multicore.patterns.mediator.Mediator;

import pnnl.cyclist.Resources;
import pnnl.cyclist.view.component.View;
import pnnl.cyclist.view.tool.Tool;
import weather.view.mediator.DegreeDaysMapMediator;
import weather.view.tool.view.DegreeDaysMap;


public class DegreeDaysMapTool implements Tool {

		@Override
		public Image getIcon() {
			return Resources.getIcon("table");
		}

		@Override
		public String getName() {
			return "Degree Days";
		}

		@Override
		public View getView() {
			View view = new DegreeDaysMap();
			view.setParam("Degree Days Map");
			return view;
		}

		@Override
		public Mediator getMediator() {
			return new DegreeDaysMapMediator();
		}

	}