package weather.utils;

import java.awt.geom.AffineTransform;

import javafx.scene.transform.Affine;

public class AffineUtils {

	public static Affine convert(AffineTransform t) {
		Affine a = new Affine();
		
		a.setMxx(t.getScaleX());
		a.setMxy(t.getShearX());
		a.setMxz(0);
		a.setTx(t.getTranslateX());
		
		a.setMyx(t.getShearY());
		a.setMyy(t.getScaleY());
		a.setMyz(0);
		a.setTy(t.getTranslateY());
		
		a.setMzx(0);
		a.setMzy(0);
		a.setMzz(1);
		a.setTz(0);
		
		return a;
	}
}
