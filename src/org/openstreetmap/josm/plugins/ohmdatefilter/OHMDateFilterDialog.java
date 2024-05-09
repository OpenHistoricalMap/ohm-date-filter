package org.openstreetmap.josm.plugins.ohmdatefilter;

import static org.openstreetmap.josm.gui.MainApplication.getLayerManager;
import static org.openstreetmap.josm.gui.MainApplication.getMap;
import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.openstreetmap.josm.data.UserIdentityManager;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.data.osm.event.SelectionEventManager;
import org.openstreetmap.josm.gui.SideButton;
import org.openstreetmap.josm.gui.dialogs.ToggleDialog;
import org.openstreetmap.josm.gui.layer.NoteLayer;
import org.openstreetmap.josm.gui.util.GuiHelper;
import org.openstreetmap.josm.tools.ImageProvider;
import org.openstreetmap.josm.tools.ImageProvider.ImageSizes;
import org.openstreetmap.josm.tools.Shortcut;
import org.openstreetmap.josm.tools.date.DateUtils;


public class OHMDateFilterDialog extends ToggleDialog {

    protected JLabel lbUser;
    protected JLabel lbVersion;
    protected JLabel lbIdobj;
    protected JLabel lbRemoteControl;
    protected JLabel lbTimestamp;
    protected JLabel lbIdChangeset;
    protected JLabel lbLinkUser;
    protected JLabel lbLinnkIdobj;
    protected JLabel lbLinkMapillary;
    protected JLabel lbLinkOSMcamp;
    protected JLabel lbLinkYandex;
    protected JLabel lbLinkIdChangeset;
    protected JLabel lbCopyUser;
    protected JLabel lbCopyIdobj;
    protected JLabel lbCopyIdChangeset;

    protected JLabel lbNeisUser;
    protected JLabel lbChangesetMap;
    protected JLabel lbOsmDeepHistory;
    protected JLabel lbUserOsmComments;

    protected JLabel lbMapillary;
    protected JLabel lbOsmcamp;



    public OHMDateFilterDialog() { 
        super(tr("OpenHistoricalMap Date Filter"),
          "iconohmdatefilter16",
          tr("Open OpenHistoricalMap date filter window"),
          Shortcut.registerShortcut("ohmDateFilter", tr("Toggle: {0}", tr("OpenHistoricalMap Date Filter")), KeyEvent.VK_I,
              Shortcut.ALT_CTRL_SHIFT), 90);


        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
//        //user
//        panel.add(new JLabel(tr("User")));
//        panel.add(buildUser());
//        //changeset
//        panel.add(new JLabel(tr("Changeset")));
//        panel.add(buildChangeset());
//        //obj id
//        panel.add(new JLabel(tr("Object Id")));
//        panel.add(buildidObject());
//        //version
//        panel.add(new JLabel(tr("Version")));
//        lbVersion = new JLabel();
//        panel.add(lbVersion);
//        //date
//        panel.add(new JLabel(tr("Date")));
//        lbTimestamp = new JLabel();
//        panel.add(lbTimestamp);
//
//        panel.add(new JLabel(tr("Images")));
//        lbMapillary = new JLabel();
//        lbOsmcamp = new JLabel();
//        panel.add(streetLevelImages());

        createLayout(panel, false, Arrays.asList(new SideButton[] {}));
//        SelectionEventManager.getInstance().addSelectionListener(event -> selection(event.getSelection()));

//        getMap().mapView.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                getInfoNotes(e);
//            }
//        });
    }


}
