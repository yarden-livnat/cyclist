package weather.view.tool;

import javafx.scene.image.Image;

import org.puremvc.java.multicore.patterns.mediator.Mediator;

import weather.view.mediator.GISMapMediator;
import weather.view.mediator.WeatherMapMediator;
import weather.view.tool.component.GISMapView;
import weather.view.tool.component.WeatherMapView;

import cyclist.Resources;
import cyclist.view.component.View;
import cyclist.view.tool.Tool;

public class GISMapTool implements Tool {

		@Override
		public Image getIcon() {
			return Resources.getIcon("table");
		}

		@Override
		public String getName() {
			return "GISMap";
		}

		@Override
		public View getView() {
			View view = new GISMapView();
//			view.setParam("WeatherMap");
			return view;
		}

		@Override
		public Mediator getMediator() {
			return new GISMapMediator();
		}

	}