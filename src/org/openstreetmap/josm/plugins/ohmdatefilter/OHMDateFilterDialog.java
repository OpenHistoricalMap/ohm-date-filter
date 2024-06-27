package org.openstreetmap.josm.plugins.ohmdatefilter;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import javax.swing.JButton;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.data.osm.search.SearchSetting;
import org.openstreetmap.josm.gui.HelpAwareOptionPane;
import org.openstreetmap.josm.gui.MainApplication;

import org.openstreetmap.josm.gui.SideButton;
import org.openstreetmap.josm.gui.dialogs.ToggleDialog;
import static org.openstreetmap.josm.gui.help.HelpUtil.ht;
import org.openstreetmap.josm.tools.Shortcut;

public class OHMDateFilterDialog extends ToggleDialog {

    String[] listYears = new String[]{"2 years", "5 years", "10 years", "50 years", "100 years", "200 years", "500 years"};
    String[] listMonths = new String[]{"2 Months", "5 Months", "10 Months", "12 Months"};
    Boolean updateComoBox = false;

    DateHandler dateHandler = new DateHandler();
    private JTextField jTextFieldYear = new JTextField();
    private JTextField jTextSettings = new JTextField();
    private RangeSlider rangeSlider = new RangeSlider();
    private JComboBox jComboBoxRange = new JComboBox<>();

    public OHMDateFilterDialog() {
        super(tr("OpenHistoricalMap Date Filter"),
                "iconohmdatefilter16",
                tr("Open OpenHistoricalMap date filter window"),
                Shortcut.registerShortcut("ohmDateFilter", tr("Toggle: {0}", tr("OpenHistoricalMap Date Filter")), KeyEvent.VK_I,
                        Shortcut.ALT_CTRL_SHIFT), 90);

        JosmAction oHMActionSaver = new JosmAction(
                tr("Save Filter"), "save",
                tr("Save Filter"),
                Shortcut.registerShortcut("tools:ohm_save_filter", tr("More tools: {0}", tr("Save Filter")),
                        KeyEvent.VK_S, Shortcut.SHIFT), true) {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchFormat = jTextSettings.getText();
                SearchSetting searchSetting = getSearchSetting(searchFormat);
                OHMDateFilterFunctions.applyDateFilter(searchSetting, true);
            }
        };

        JosmAction oHMActionReset = new JosmAction(
                tr("Reset Filters"), "reset",
                tr("Reset Filters"),
                Shortcut.registerShortcut("tools:ohm_reset_filter", tr("More tools: {0}", tr("Reset Filters")),
                        KeyEvent.VK_R, Shortcut.SHIFT), true) {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("OHMActionSaver performed!");

            }
        };

        SideButton sideButtonSaveFilter = new SideButton(oHMActionSaver);
        SideButton sideButtonReset = new SideButton(oHMActionReset);

        //Main panel
        JPanel mainPanel = new JPanel(new GridLayout(3, 1));
        mainPanel.setMinimumSize(new Dimension(100, 30));

        //Add panels 
        mainPanel.add(imputDatePanel());
        mainPanel.add(rangeSliderPanel());
        mainPanel.add(jTextSettings);

        createLayout(mainPanel, false, Arrays.asList(sideButtonSaveFilter, sideButtonReset));

    }

    private JPanel imputDatePanel() {
        // Imput panel to add initial date
        JPanel panel = new JPanel(new GridLayout(1, 3));
        panel.setBorder(javax.swing.BorderFactory.createTitledBorder("Set Date and Range value"));
        panel.add(jTextFieldYear);

        jComboBoxRange.setModel(new javax.swing.DefaultComboBoxModel<>(listYears));
        panel.add(jComboBoxRange, java.awt.BorderLayout.CENTER);

        JButton jButtonSetYear = new JButton("Set date");
        panel.add(jButtonSetYear);

        // Add ActionListener to the button
        jButtonSetYear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = jTextFieldYear.getText();
                int rangeValue = (int) UtilDates.getRangeValue(jComboBoxRange.getSelectedItem() + "");

                if (input != null && !input.isEmpty()) {
                    setMinMaxYearForSlider(input, rangeValue);
                }
            }
        });
        jTextFieldYear.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                handleTextChange();
            }

            public void removeUpdate(DocumentEvent e) {
                handleTextChange();
            }

            public void insertUpdate(DocumentEvent e) {
                handleTextChange();
            }

            public void handleTextChange() {
                String text = jTextFieldYear.getText();

                try {
                    if (text.contains("-")) {
                        if (!updateComoBox) {
                            jComboBoxRange.removeAllItems();
                            for (String month : listMonths) {
                                jComboBoxRange.addItem(month);
                            }
                        }
                        updateComoBox = true;
                    } else {
                        if (updateComoBox) {
                            jComboBoxRange.removeAllItems();
                            for (String year : listYears) {
                                jComboBoxRange.addItem(year);
                            }
                            updateComoBox = false;
                        }

                    }
                } catch (NumberFormatException ex) {
                    System.out.println("Invalid number: " + text);
                }
            }
        });

        return panel;
    }

    private JPanel rangeSliderPanel() {
        // Panel that contains slider range panel 
        JPanel panel = new JPanel(new GridBagLayout());
        rangeSlider.setPreferredSize(new Dimension(300, 100));
        panel.setBorder(javax.swing.BorderFactory.createTitledBorder("Filter by Date Range"));
        rangeSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {

                RangeSlider slider = (RangeSlider) e.getSource();
                // Get serach format
                String searchFormat = getSearchFormat(slider.getValue(), slider.getUpperValue());
                System.err.println("Search format: " + searchFormat);
                jTextSettings.setText(searchFormat);
                SearchSetting searchSetting = getSearchSetting(searchFormat);
                // Apply filter
                OHMDateFilterFunctions.applyDateFilter(searchSetting, false);
            }
        });

        panel.add(rangeSlider, new GridBagConstraints(0, 2, 2, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        return panel;
    }

    private void setMinMaxYearForSlider(String datestr, int RangeValue) {
        boolean isMonthValue = false;
        if (datestr.contains("-")) {
            isMonthValue = true;
        }
        try {

            // Conver string date to date format
            String currentDate_str = UtilDates.formatDate(datestr);
            dateHandler.setDate(currentDate_str);
            int minRange = dateHandler.getDaysAfterAdjustingYears(-1 * RangeValue);
            int maxRange = dateHandler.getDaysAfterAdjustingYears(RangeValue);
            int minWindow = dateHandler.getDaysAfterAdjustingYears(0);
            int maxWindow = dateHandler.getDaysAfterAdjustingYears(1);

            if (isMonthValue) {
                minRange = dateHandler.getDaysAfterAdjustingMonths(-1 * RangeValue);
                maxRange = dateHandler.getDaysAfterAdjustingMonths(RangeValue);
                minWindow = dateHandler.getDaysAfterAdjustingMonths(0);
                maxWindow = dateHandler.getDaysAfterAdjustingMonths(1);
            }

            if (minRange < 0 || maxRange < 0 || minWindow < 0 || maxWindow < 0) {
                HelpAwareOptionPane.showMessageDialogInEDT(
                        MainApplication.getMainFrame(),
                        "Wrong date: " + currentDate_str,
                        tr("Warning"),
                        JOptionPane.WARNING_MESSAGE,
                        ht("/Action/Open#MissingImporterForFiles")
                );
                return;
            }

            // Fill range
            rangeSlider.setMinimum(minRange);
            rangeSlider.setMaximum(maxRange);
            rangeSlider.setUpperValue(maxWindow);
            rangeSlider.setValue(minWindow);

        } catch (NumberFormatException e) {
            System.out.println("Invalid year format: ");
        }
    }

    private String getSearchFormat(int start_num_days, int end_num_days) {
        String start_date_str = dateHandler.getDateFromDaysSinceYearZero(start_num_days);
        String end_date_str = dateHandler.getDateFromDaysSinceYearZero(end_num_days - 1);
        String filter_values = "start_date>" + start_date_str + " AND end_date<" + end_date_str;
        return filter_values;
    }

    private SearchSetting getSearchSetting(String searchFormat) {
        SearchSetting searchSetting = new SearchSetting();
        searchSetting.text = searchFormat;
        searchSetting.caseSensitive = false;
        searchSetting.regexSearch = false;
        searchSetting.mapCSSSearch = false;
        searchSetting.allElements = true;
        return searchSetting;
    }
}
