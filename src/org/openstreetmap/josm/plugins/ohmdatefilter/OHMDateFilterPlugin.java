package org.openstreetmap.josm.plugins.ohmdatefilter;

import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.openstreetmap.josm.data.preferences.IntegerProperty;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.MapFrame;
import org.openstreetmap.josm.plugins.Plugin;
import org.openstreetmap.josm.plugins.PluginInformation;
import org.openstreetmap.josm.tools.I18n;
import org.openstreetmap.josm.actions.JosmAction;

public class OHMDateFilterPlugin extends Plugin {

    protected static OHMDateFilterDialog oHMDateFilterDialog;

    public OHMDateFilterPlugin(PluginInformation info) {
        super(info);
        createMenuEntry();
    }


    public void createMenuEntry() {
    JosmAction action = new JosmAction(I18n.tr("Date Filter"), 
                                       "iconohmdatefilter16", 
                                       I18n.tr("Open the OpenHistoricalMap date filter window"), 
                                       null, 
                                       true) {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (oHMDateFilterDialog == null) {
                oHMDateFilterDialog = new OHMDateFilterDialog();
            }
            // Toggle the visibility based on the current state
            if (oHMDateFilterDialog.isVisible()) {
                oHMDateFilterDialog.setVisible(false);
            } else {
                oHMDateFilterDialog.setVisible(true);
            }
        }
    };
    JMenuItem menuItem = new JMenuItem(action);
    JMenu ohmMenu = new JMenu(I18n.tr("OpenHistoricalMap"));
    ohmMenu.add(menuItem);
    MainApplication.getMenu().add(ohmMenu);
}

    @Override
    public void mapFrameInitialized(MapFrame oldFrame, MapFrame newFrame) {
        if (newFrame != null && !GraphicsEnvironment.isHeadless()) {
            if (oHMDateFilterDialog == null) {
                oHMDateFilterDialog = new OHMDateFilterDialog();
            }
            newFrame.addToggleDialog(oHMDateFilterDialog);
        }
    }
}
