package sk.freemap.kapor;

import org.openstreetmap.josm.spi.preferences.PreferenceChangeEvent;

import sk.freemap.kapor.preferences.PreferenceKeys;

public class PreferenceChangedListener implements org.openstreetmap.josm.spi.preferences.PreferenceChangedListener, PreferenceKeys {

	@Override
	public void preferenceChanged(PreferenceChangeEvent e) {
		if (e.getKey() == FREEMAPKAPOR_MWFURL) {
			KaporPlugin.mwfUrl = (String) e.getNewValue().getValue();
		}
	}

}
