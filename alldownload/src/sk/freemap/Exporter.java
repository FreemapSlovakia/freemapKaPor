package sk.freemap;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;

import com.autodesk.mgjava.MGMap;
import com.autodesk.mgjava.MGMapLayer;
import com.autodesk.mgjava.MGMapObject;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKBWriter;

public class Exporter {
	public Connection conn = null;

	WKBWriter wkbwrite = new WKBWriter();
	PreparedStatement insert_popisky;
	PreparedStatement insert_geom;

	public Exporter() throws IOException, SQLException {
	}

	public void setConnection(Connection c) throws SQLException {
		conn = c;
		conn.setAutoCommit(false);

		insert_popisky = conn
				.prepareStatement("INSERT INTO popisky(layer,key,name) VALUES (?,?,?)");
		insert_geom = conn
				.prepareStatement("INSERT INTO data (layer, key, geom) VALUES (?, ?, ?)");
	}

	public void export(MGMap map) throws SQLException, IOException {
		Vector<MGMapLayer> mgLayers = map.getMapLayers();

		for (final MGMapLayer mglayer : mgLayers) {

			if (!mglayer.isVisible())
				continue;

			final String layertype = mglayer.getType();
			final String layername = mglayer.getName();

			Vector<MGMapObject> objects = mglayer.getMapObjects();

			if (objects == null)
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			objects = mglayer.getMapObjects();

			if (objects == null)
				System.out.println("NULL: " + layername + "," + layertype);

			if (objects != null && objects.size() > 0) {

				for (final MGMapObject mgobject : objects) {

					assert layertype == mgobject.getType();
					long key = Long.parseLong(mgobject.getKey());
					String name = mgobject.getName();

					if (!name.isEmpty()) {
						synchronized (conn) {
							PreparedStatement ps = insert_popisky;
							ps.setString(1, layername);
							ps.setLong(2, key);
							ps.setString(3, name);
							ps.addBatch();
						}
					}

					Geometry g = null;

					try {
						g = Conversion.object_to_geometry(map, mgobject);
					} catch (Exception e) {

					}

					if (g != null && !g.isEmpty() && g.isValid()) {

						synchronized (conn) {
							PreparedStatement ps = insert_geom;
							ps.setString(1, layername);
							ps.setLong(2, key);
							ps.setBytes(3, wkbwrite.write(g));
							ps.addBatch();
						}
					}
				}

				synchronized (conn) {
					insert_popisky.executeBatch();
					insert_popisky.clearBatch();
					insert_geom.executeBatch();
					insert_geom.clearBatch();
				}
			}
		}
	}
}
