package sk.freemap.kapor;

import sk.freemap.kapor.preferences.PreferenceKeys;

import org.openstreetmap.josm.data.Preferences.PreferenceChangeEvent;

public class PreferenceChangedListener implements
		org.openstreetmap.josm.data.Preferences.PreferenceChangedListener, PreferenceKeys {

	@Override
	public void preferenceChanged(PreferenceChangeEvent e) {
		if (e.getKey() == FREEMAPKAPOR_MWFURL) {
			KaporPlugin.mwfUrl = (String) e.getNewValue().getValue();
		}
	}

}
