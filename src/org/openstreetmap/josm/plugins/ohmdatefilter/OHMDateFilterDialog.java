package org.openstreetmap.josm.plugins.ohmdatefilter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import javax.swing.JButton;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jdesktop.swingx.JXSearchField;
import org.jdesktop.swingx.JXTextField;
import org.openstreetmap.josm.data.osm.search.SearchSetting;

import org.openstreetmap.josm.gui.SideButton;
import org.openstreetmap.josm.gui.dialogs.ToggleDialog;
import org.openstreetmap.josm.tools.Shortcut;

public class OHMDateFilterDialog extends ToggleDialog {

    private OHMDateFilterCalendar start_dateFilterCalendar;
//    private OHMDateFilterCalendar end_dateFilterCalendar;
//    private JLabel dateRangeLabel;

    private OHMDateFilterCalendar dateFilterCalendar = new OHMDateFilterCalendar(new Date(),"Set an estimated date to filter");

//    private JTextField jTextFieldDate = new JTextField();
    private JLabel rangeDetailValues = new JLabel();
    private RangeSlider rangeSlider = new RangeSlider();

    public OHMDateFilterDialog() {
        super(tr("OpenHistoricalMap Date Filter"),
                "iconohmdatefilter16",
                tr("Open OpenHistoricalMap date filter window"),
                Shortcut.registerShortcut("ohmDateFilter", tr("Toggle: {0}", tr("OpenHistoricalMap Date Filter")), KeyEvent.VK_I,
                        Shortcut.ALT_CTRL_SHIFT), 90);

        JPanel main_panel = new JPanel(new GridLayout(4, 1));

        JButton updateButton = new JButton("Save Filter");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("save filter");

            }
        });

        main_panel.add(dateFilterCalendar);
        main_panel.add(createRangeSliderPanel());
        main_panel.add(rangeDetailValues);
        main_panel.add(updateButton);
        createLayout(main_panel, false, Arrays.asList(new SideButton[]{}));
    }
//
//    private JPanel createImputValuesPanel() {
//        JPanel panel = new JPanel(new GridLayout(1, 2));
//        panel.add(new JLabel("Set stimate date to filter"));
//        panel.add(dateFilterCalendar);
//        return panel;
//    }

    private JPanel createRangeSliderPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        rangeSlider.setPreferredSize(new Dimension(300, 100));
        panel.setBorder(javax.swing.BorderFactory.createTitledBorder("Date range"));

        rangeSlider.setMinimum(1960 - 100);
        rangeSlider.setMaximum(2000 + 100);
        rangeSlider.setUpperValue(2000);
        rangeSlider.setValue(1960);

        // Add listener to update display.
        rangeSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                RangeSlider slider = (RangeSlider) e.getSource();
                filterData(slider.getValue(), slider.getUpperValue());
            }
        });

        panel.add(rangeSlider, new GridBagConstraints(0, 2, 2, 1, 1.0, 0.0, // Adjusted weightx to 1.0
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, // Changed fill to HORIZONTAL
                new Insets(0, 0, 0, 0), 0, 0));

        return panel;
    }

    private void filterData(int start_date_num, int end_date_num) {
        System.err.println(start_date_num);
        System.err.println(end_date_num);
        String filter_values = "start_date>" + start_date_num + " AND end_date<" + end_date_num;
        System.out.println(filter_values);
        rangeDetailValues.setText(filter_values);
        SearchSetting searchSetting = new SearchSetting();
        searchSetting.text = filter_values;
        searchSetting.caseSensitive = false;
        searchSetting.regexSearch = false;
        searchSetting.mapCSSSearch = false;
        searchSetting.allElements = true;
        OHMDateFilterFunctions.applyDateFilter(searchSetting);
    }
}
