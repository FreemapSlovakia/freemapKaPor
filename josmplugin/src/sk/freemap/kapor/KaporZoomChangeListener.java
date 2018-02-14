package sk.freemap.kapor;

import org.geotools.geometry.jts.JTS;
import org.opengis.referencing.operation.TransformException;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.coor.EastNorth;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.MapFrame;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.gui.NavigatableComponent.ZoomChangeListener;

import com.vividsolutions.jts.geom.Coordinate;

public class KaporZoomChangeListener implements ZoomChangeListener {

	@Override
	public void zoomChanged() {
		if (MainApplication.getMap() != null && KaporMenuActionListener.applet != null
				&& KaporMenuActionListener.frame.isVisible()) {
			KaporZoomChangeListener.setAppletZoom();
		}
	}

	public static void setAppletZoom() {

		KatApplet applet = KaporMenuActionListener.applet;
		MapFrame map = MainApplication.getMap();
		if (map != null && applet != null) {
			MapView mapView = map.mapView;
			EastNorth center = mapView.getCenter();
			LatLon centerLatLon = mapView.getProjection()
					.eastNorth2latlon(center);

			Bounds b = mapView.getRealBounds();
			LatLon p1 = b.getMin(), p2 = b.getMax();

			double x = centerLatLon.getX();
			double y = centerLatLon.getY();

			if (16 < x && x < 24 && 47 < y && y < 50) {
				Coordinate newcenter = new Coordinate();
				Coordinate newp1 = new Coordinate(), newp2 = new Coordinate();

				try {
					// Ignore difference between S-JTSK and S-JTSK/03
					JTS.transform(new Coordinate(x, y), newcenter, 
							Projection.etrs89_to_s_jtsk_03);

					JTS.transform(new Coordinate(p1.getX(), p1.getY()), newp1,
							Projection.etrs89_to_s_jtsk_03);
					JTS.transform(new Coordinate(p2.getX(), p2.getY()), newp2,
							Projection.etrs89_to_s_jtsk_03);
				} catch (TransformException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				double width = Math.abs(newp1.x - newp2.x);
				if (width < 1)
					width = 1;

				applet.zoomWidth(-newcenter.x, -newcenter.y, width, "M");
			}
		}
	}

}
