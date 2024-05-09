package org.openstreetmap.josm.plugins.ohmdatefilter;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.util.Arrays;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.openstreetmap.josm.gui.SideButton;
import org.openstreetmap.josm.gui.dialogs.ToggleDialog;
import org.openstreetmap.josm.tools.Shortcut;


public class OHMDateFilterDialog extends ToggleDialog {

    public OHMDateFilterDialog() { 
        super(tr("OpenHistoricalMap Date Filter"),
          "iconohmdatefilter16",
          tr("Open OpenHistoricalMap date filter window"),
          Shortcut.registerShortcut("ohmDateFilter", tr("Toggle: {0}", tr("OpenHistoricalMap Date Filter")), KeyEvent.VK_I,
              Shortcut.ALT_CTRL_SHIFT), 90);


        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        createLayout(panel, false, Arrays.asList(new SideButton[] {}));
    }


}
