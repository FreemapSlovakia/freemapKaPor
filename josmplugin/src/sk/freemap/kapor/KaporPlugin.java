package sk.freemap.kapor;

import java.io.IOException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.JMenuItem;

import org.opengis.referencing.FactoryException;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.NavigatableComponent;
import org.openstreetmap.josm.gui.preferences.PreferenceSetting;
import org.openstreetmap.josm.plugins.Plugin;
import org.openstreetmap.josm.plugins.PluginInformation;
import org.openstreetmap.josm.spi.preferences.Config;

import sk.freemap.kapor.preferences.KaporPreferenceSetting;
import sk.freemap.kapor.preferences.PreferenceKeys;

public class KaporPlugin extends Plugin implements PreferenceKeys {
	public static String mwfUrl;

	public KaporPlugin(PluginInformation info) throws FactoryException, IOException {
		super(info);
		mwfUrl = Config.getPref().get(FREEMAPKAPOR_MWFURL);
		if (mwfUrl == null || mwfUrl.length() == 0) {
			// mwfUrl = "http://195.28.70.134/kapor2/maps/mapa.mwf";
			mwfUrl = "https://195.28.70.133/kapor2/maps/mapa.mwf";
			Config.getPref().put(FREEMAPKAPOR_MWFURL, mwfUrl);
		}

//		SSLUtilities.trustAllHostnames();
//		SSLUtilities.trustAllHttpsCertificates();

		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
		}

		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

		Projection.initCRS();
		Projection.initGrid();

		JMenuItem menuItem = new JMenuItem("Kapor");
		menuItem.addActionListener(new KaporMenuActionListener());
		MainApplication.getMenu().add(menuItem);

		NavigatableComponent.addZoomChangeListener(new KaporZoomChangeListener());
	}

	@Override
	public PreferenceSetting getPreferenceSetting() {
		return new KaporPreferenceSetting();
	}
}
