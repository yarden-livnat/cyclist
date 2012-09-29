package weather.view.tool;

import javafx.scene.image.Image;

import org.puremvc.java.multicore.patterns.mediator.Mediator;

import pnnl.cyclist.Resources;
import pnnl.cyclist.view.component.View;
import pnnl.cyclist.view.tool.Tool;

import weather.view.mediator.WeatherMapMediator;
import weather.view.tool.view.WeatherMapView;


public class WeatherMapTool implements Tool {

		@Override
		public Image getIcon() {
			return Resources.getIcon("table");
		}

		@Override
		public String getName() {
			return "Map";
		}

		@Override
		public View getView() {
			View view = new WeatherMapView();
			view.setParam("Weather Map");
			return view;
		}

		@Override
		public Mediator getMediator() {
			return new WeatherMapMediator();
		}

	}