package weather.view.tool.component;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import weather.view.gis.component.Feature;

public class FeaturesLayer extends Layer {

	private List<Feature> _features = new ArrayList<>();
	
	public FeaturesLayer() {
		this("Features");
	}
	
	public FeaturesLayer(String name) {
		super(name);
	}

	public List<Feature> getFeatures() {
		return _features;
	}
	
	@Override
	public void redraw(AffineTransform tx) {
		super.redraw(tx);
		
		for (Feature f : _features) {
			f.draw(gc);
		}
	}
	
}
