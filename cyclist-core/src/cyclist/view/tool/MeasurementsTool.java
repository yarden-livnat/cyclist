package cyclist.view.tool;

import javafx.scene.image.Image;

import org.puremvc.java.multicore.patterns.mediator.Mediator;

import cyclist.Resources;
import cyclist.view.component.View;
import cyclist.view.tool.component.MeasurementsView;
import cyclist.view.tool.mediator.MeasurementsMediator;

public class MeasurementsTool implements Tool {

		@Override
		public Image getIcon() {
			return Resources.getIcon("table");
		}

		@Override
		public String getName() {
			return "Weather";
		}

		@Override
		public View getView() {
			View view = new MeasurementsView();
//			view.setParam("Measurements");
			return view;
		}

		@Override
		public Mediator getMediator() {
			return new MeasurementsMediator();
		}

	}