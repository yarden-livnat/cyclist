package weather.view.tool;

import javafx.scene.image.Image;

import org.puremvc.java.multicore.patterns.mediator.Mediator;

import pnnl.cyclist.Resources;
import pnnl.cyclist.view.component.View;
import pnnl.cyclist.view.tool.Tool;
import weather.view.mediator.ChartMediator;
import weather.view.tool.view.ChartView;


public class ChartTool implements Tool {

		@Override
		public Image getIcon() {
			return Resources.getIcon("chart_curve");
		}

		@Override
		public String getName() {
			return "Chart";
		}

		@Override
		public View getView() {
			View view = new ChartView();
			view.setParam("Chart");
			return view;
		}

		@Override
		public Mediator getMediator() {
			return new ChartMediator();
		}

	}