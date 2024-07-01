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
import javax.swing.ButtonGroup;
import javax.swing.JButton;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

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
import org.openstreetmap.josm.tools.Shortcut;

public class OHMDateFilterDialog extends ToggleDialog {

    String[] listYears = new String[]{"2 years", "5 years", "10 years", "50 years", "100 years", "200 years", "500 years"};
    String[] listMonths = new String[]{"2 Month", "6 Months", "12 Months", "24 Months"};
    String[] listDays = new String[]{"1 Days", "5 days", "10 days"};

    Boolean updateComoBoxYear = false;
    Boolean updateComoBoxMonth = true;
    Boolean updateComoBoxDay = true;

    String filter_by = "start_date";
    int current_lower_value = 0;
    int current_upper_value = 0;

    DateHandler dateHandler = new DateHandler();
    private JTextField jTextFieldInputDate = new JTextField();
    private JTextField jTextSettings = new JTextField();
    private RangeSlider rangeSlider = new RangeSlider();
    private JComboBox jComboBoxRange = new JComboBox<>();

    public OHMDateFilterDialog() {
        super(tr("OpenHistoricalMap Date Filter"),
                "iconohmdatefilter16",
                tr("Open OpenHistoricalMap date filter window"),
                Shortcut.registerShortcut("ohmDateFilter", tr("Toggle: {0}", tr("OpenHistoricalMap Date Filter")), KeyEvent.VK_I,
                        Shortcut.ALT_CTRL_SHIFT), 250);

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
                SearchSetting searchSetting = new SearchSetting();
                OHMDateFilterFunctions.applyDateFilter(searchSetting, false);
            }
        };

        SideButton sideButtonSaveFilter = new SideButton(oHMActionSaver);
        SideButton sideButtonReset = new SideButton(oHMActionReset);

        //Main panel
        JPanel mainPanel = new JPanel(new GridLayout(4, 1));
        mainPanel.setMinimumSize(new Dimension(500, 30));

        //Add panels 
        mainPanel.add(imputDatePanel());
        mainPanel.add(rangeSliderPanel());
        mainPanel.add(radioOptions());

        mainPanel.add(jTextSettings);

        createLayout(mainPanel, false, Arrays.asList(sideButtonSaveFilter, sideButtonReset));

    }

    private JPanel imputDatePanel() {
        // Imput panel to add initial date
        JPanel panel = new JPanel(new GridLayout(1, 3));
        panel.setBorder(javax.swing.BorderFactory.createTitledBorder("Set Date and Range value"));
        panel.add(jTextFieldInputDate);

        jComboBoxRange.setModel(new javax.swing.DefaultComboBoxModel<>(listYears));
        panel.add(jComboBoxRange, java.awt.BorderLayout.CENTER);

        JButton jButtonSetYear = new JButton("Set date");
        panel.add(jButtonSetYear);

        // Add ActionListener to the button
        jButtonSetYear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input_date = jTextFieldInputDate.getText();

                if (input_date != null && !input_date.isEmpty()) {
                    dateHandler.setDate(input_date);
                    if (dateHandler.getDate() != null) {
                        setMinMaxValuesForSlider();

                    } else {
                        HelpAwareOptionPane.showMessageDialogInEDT(
                                MainApplication.getMainFrame(),
                                "Wrong date: " + input_date,
                                tr("Warning"),
                                JOptionPane.WARNING_MESSAGE,
                                ht("/Action/Open#MissingImporterForFiles")
                        );
                    }
                }
            }

            private String ht(String actionOpenMissingImporterForFiles) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }
        });

        jTextFieldInputDate.getDocument().addDocumentListener(new DocumentListener() {
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
                String text = jTextFieldInputDate.getText();

                try {
                    String[] parts = text.split("-");
                    for (String part : parts) {
                        System.out.println(part);
                    }
                    if (parts.length == 1) {
                        //year
                        if (updateComoBoxYear) {

                            jComboBoxRange.removeAllItems();
                            for (String year : listYears) {
                                jComboBoxRange.addItem(year);
                            }
                            updateComoBoxYear = false;
                            updateComoBoxMonth = true;
                            updateComoBoxDay = true;
                        }

                    } else if (parts.length == 2) {

                        //month
                        if (updateComoBoxMonth) {
                            jComboBoxRange.removeAllItems();
                            for (String month : listMonths) {
                                jComboBoxRange.addItem(month);
                            }
                            updateComoBoxYear = true;
                            updateComoBoxMonth = false;
                            updateComoBoxDay = true;
                        }
                    } else if (parts.length == 3) {
                        //day
                        if (updateComoBoxDay) {
                            jComboBoxRange.removeAllItems();
                            for (String day : listDays) {
                                jComboBoxRange.addItem(day);
                            }
                            updateComoBoxYear = true;
                            updateComoBoxMonth = true;
                            updateComoBoxDay = false;
                        }
                    }

                } catch (NumberFormatException ex) {
                    System.out.println("Invalid date");
                }
            }
        }
        );

        return panel;
    }

    private JPanel rangeSliderPanel() {
        // Panel that contains slider range panel 
        JPanel panel = new JPanel(new GridBagLayout());
        rangeSlider.setPreferredSize(new Dimension(300, 100));
        panel.setBorder(javax.swing.BorderFactory.createTitledBorder("Set Date Range"));
        rangeSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                RangeSlider slider = (RangeSlider) e.getSource();
                // Get serach format
                current_lower_value = slider.getValue();
                current_upper_value = slider.getUpperValue();
                filterObjects();
            }
        });

        panel.add(rangeSlider, new GridBagConstraints(0, 2, 2, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), 0, 0));
        return panel;
    }

    private JPanel radioOptions() {
        // Crear el panel
        JPanel panel = new JPanel(new GridLayout(1, 3));
        panel.setBorder(javax.swing.BorderFactory.createTitledBorder("Filter by"));

        // Crear los botones de radio
        JRadioButton startDateButton = new JRadioButton("start_date");
        JRadioButton endDateButton = new JRadioButton("end_date");
        JRadioButton rangeButton = new JRadioButton("range");

        // Crear el ButtonGroup y agregar los botones de radio
        ButtonGroup group = new ButtonGroup();
        group.add(startDateButton);
        group.add(endDateButton);
        group.add(rangeButton);
        startDateButton.setSelected(true);

        // Agregar los botones de radio al panel
        panel.add(startDateButton);
        panel.add(endDateButton);
        panel.add(rangeButton);

        // Agregar ActionListener a cada botón de radio
        startDateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filter_by = "start_date";
                filterObjects();
            }
        });

        endDateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filter_by = "end_date";
                filterObjects();
            }
        });

        rangeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filter_by = "range";
                filterObjects();
            }
        });
        return panel;
    }

    private void filterObjects() {
        String searchFormat = getSearchFormat(current_lower_value, current_upper_value);
        System.err.println("Search format: " + searchFormat);
        jTextSettings.setText(searchFormat);
        SearchSetting searchSetting = getSearchSetting(searchFormat);
        // Apply filter
        OHMDateFilterFunctions.applyDateFilter(searchSetting, false);
    }

    private void setMinMaxValuesForSlider() {
        try {

            int rangeValue = (int) UtilDates.getRangeValue(jComboBoxRange.getSelectedItem() + "");

            int minRange = 0;
            int maxRange = 0;
            int minWindow = 0;
            int maxWindow = 0;

            if ("year".equals(dateHandler.getFormat())) {
                DateCompose minRangeYear = dateHandler.getRangeYear(-1 * rangeValue);
                DateCompose maxWindowYear = dateHandler.getRangeYear(rangeValue);
                DateCompose maxRangeYear = dateHandler.getRangeYear(rangeValue);
                minWindow = dateHandler.getCurrentYearFromYearZero();
                maxWindow = maxRangeYear.getNumValue();
                minRange = minRangeYear.getNumValue();
                maxRange = maxWindowYear.getNumValue();

            } else if ("month".equals(dateHandler.getFormat())) {
                DateCompose minRangeMonth = dateHandler.getRangeMonth(-1 * rangeValue);
                DateCompose maxWindowMonth = dateHandler.getRangeMonth(rangeValue);
                DateCompose maxRangeMonth = dateHandler.getRangeMonth(rangeValue);
                minWindow = dateHandler.getCurrentMonthFromYearZero();
                maxWindow = maxRangeMonth.getNumValue();
                minRange = minRangeMonth.getNumValue();
                maxRange = maxRangeMonth.getNumValue();

            } else if ("day".equals(dateHandler.getFormat())) {
                DateCompose minRangeDay = dateHandler.getRangeDays(-1 * rangeValue);
                DateCompose maxWindowDay = dateHandler.getRangeDays(rangeValue);
                DateCompose maxRangeDay = dateHandler.getRangeDays(rangeValue);
                minWindow = dateHandler.getCurrentDaysFromYearZero();
                maxWindow = maxWindowDay.getNumValue();
                minRange = minRangeDay.getNumValue();
                maxRange = maxRangeDay.getNumValue();

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

    private String getSearchFormat(int start_num, int end_num) {
        String start_date_str = dateHandler.getFormattedDateString(start_num);
        String end_date_str = dateHandler.getFormattedDateString(end_num);
        String filter_values = "";

        if (filter_by == "range") {
            filter_values = "start_date>" + start_date_str + " AND end_date<" + end_date_str;
        } else if (filter_by == "end_date") {
            filter_values = "end_date=" + start_date_str;
        } else if (filter_by == "start_date") {
            filter_values = "start_date=" + start_date_str;
        }
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
