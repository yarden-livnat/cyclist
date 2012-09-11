package cyclist.view.tool;

import javafx.scene.image.Image;

import org.puremvc.java.multicore.patterns.mediator.Mediator;

import cyclist.Resources;
import cyclist.view.component.View;
import cyclist.view.tool.component.WeatherMapView;
import cyclist.view.tool.mediator.WeatherMapMediator;

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
//			view.setParam("WeatherMap");
			return view;
		}

		@Override
		public Mediator getMediator() {
			return new WeatherMapMediator();
		}

	}