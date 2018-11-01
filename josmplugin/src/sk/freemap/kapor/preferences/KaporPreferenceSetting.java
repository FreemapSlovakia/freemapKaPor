/**
 * 
 */
package sk.freemap.kapor.preferences;

import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.openstreetmap.josm.gui.preferences.DefaultTabPreferenceSetting;
import org.openstreetmap.josm.gui.preferences.PreferenceTabbedPane;
import org.openstreetmap.josm.spi.preferences.Config;
import org.openstreetmap.josm.tools.GBC;

public class KaporPreferenceSetting extends DefaultTabPreferenceSetting implements PreferenceKeys {
	JCheckBox prefExportName;
	
	public KaporPreferenceSetting()
	{
        super("kapor2", "Kapor2 Plugin", "Kapor2 exporting of cadastral data");
	}

	/* (non-Javadoc)
	 * @see org.openstreetmap.josm.gui.preferences.PreferenceSetting#addGui(org.openstreetmap.josm.gui.preferences.PreferenceTabbedPane)
	 */
	@Override
	public void addGui(final PreferenceTabbedPane gui) {
		
        JPanel settingsPanel = new JPanel(new GridBagLayout());
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        
        prefExportName = new JCheckBox("Export names.", Config.getPref().getBoolean(FREEMAPKAPOR_EXPORT_NAME, false));
        settingsPanel.add(prefExportName, GBC.eol());

        //JPanel tab = gui.createPreferenceTab("kapor2", "Kapor2 Plugin", "Kapor2 exporting of cadastral data");
        JPanel tab = gui.createPreferenceTab(this);
        tab.add(settingsPanel, GBC.eol().fill(GBC.VERTICAL ));
	}

	/* (non-Javadoc)
	 * @see org.openstreetmap.josm.gui.preferences.PreferenceSetting#ok()
	 */
	@Override
	public boolean ok() {
		Config.getPref().putBoolean(FREEMAPKAPOR_EXPORT_NAME, prefExportName.isSelected());
		return false;
	}

}
