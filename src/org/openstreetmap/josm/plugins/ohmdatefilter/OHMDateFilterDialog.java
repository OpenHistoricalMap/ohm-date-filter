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
import java.util.Date;
import javax.swing.JButton;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openstreetmap.josm.data.osm.search.SearchSetting;

import org.openstreetmap.josm.gui.SideButton;
import org.openstreetmap.josm.gui.dialogs.ToggleDialog;
import org.openstreetmap.josm.tools.Shortcut;

public class OHMDateFilterDialog extends ToggleDialog {

    final int DAYS_IN_YEAR = 365;

//    private OHMDateFilterCalendar dateFilterCalendar = new OHMDateFilterCalendar(new Date(), "Set an estimated date to filter");
    private JTextField jTextFieldYear = new JTextField();
    private JTextField jTextSettings = new JTextField();
    private RangeSlider rangeSlider = new RangeSlider();

    public OHMDateFilterDialog() {
        super(tr("OpenHistoricalMap Date Filter"),
                "iconohmdatefilter16",
                tr("Open OpenHistoricalMap date filter window"),
                Shortcut.registerShortcut("ohmDateFilter", tr("Toggle: {0}", tr("OpenHistoricalMap Date Filter")), KeyEvent.VK_I,
                        Shortcut.ALT_CTRL_SHIFT), 90);

        //Main panel
        JPanel mainPanel = new JPanel(new GridLayout(4, 1));
        JButton jButtonSaveFilter = new JButton("Save Filter");
        jButtonSaveFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                
                String searchFormat = jTextSettings.getText();
                System.err.println("Search format: " + searchFormat);
                SearchSetting searchSetting = getSearchSetting(searchFormat);
                // Apply filter
                OHMDateFilterFunctions.applyDateFilter(searchSetting, true);
            }
        });
        //Add panels 
        mainPanel.add(imputDatePanel());
        mainPanel.add(rangeSliderPanel());
        mainPanel.add(jTextSettings);
        mainPanel.add(jButtonSaveFilter);
        createLayout(mainPanel, false, Arrays.asList(new SideButton[]{}));
    }

    private JPanel imputDatePanel() {
        // Imput panel to add initial date
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.setBorder(javax.swing.BorderFactory.createTitledBorder("Date range"));
        panel.add(jTextFieldYear);
        JButton jButtonSetYear = new JButton("Set date");
        panel.add(jButtonSetYear);

        // Add ActionListener to the button
        jButtonSetYear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = jTextFieldYear.getText();
                if (input != null && !input.isEmpty()) {
                    setMinMaxYearForSlider(input);
                }
            }
        });
        return panel;
    }

    private JPanel rangeSliderPanel() {
        // Panel that contains slider range panel 
        JPanel panel = new JPanel(new GridBagLayout());
        rangeSlider.setPreferredSize(new Dimension(300, 100));
        panel.setBorder(javax.swing.BorderFactory.createTitledBorder("Date range"));
        rangeSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {

                RangeSlider slider = (RangeSlider) e.getSource();

                System.err.println("####### Slider values");
                System.err.println("Start num days: " + slider.getValue());
                System.err.println("End num days: " + slider.getUpperValue());
                // Get serach format
                String searchFormat = getSearchFormat(slider.getValue(), slider.getUpperValue());
                System.err.println("Search format: " + searchFormat);
                jTextSettings.setText(searchFormat);
                SearchSetting searchSetting = getSearchSetting(searchFormat);
                // Apply filter
                OHMDateFilterFunctions.applyDateFilter(searchSetting, false);
            }
        });

        panel.add(rangeSlider, new GridBagConstraints(0, 2, 2, 1, 1.0, 0.0, // Adjusted weightx to 1.0
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, // Changed fill to HORIZONTAL
                new Insets(0, 0, 0, 0), 0, 0));
        return panel;
    }

    private void setMinMaxYearForSlider(String datestr) {

        boolean isMonthValue = false;

        System.err.println("setMinMaxYearForSlider -> ==================== " + datestr);

        if (datestr.contains("-")) {
            isMonthValue = true;
        }

        System.err.println("isMonthValue -> ==================== " + isMonthValue);

        try {
            // Conver string date to date format
            String fixedDate_str = UtilDates.formatDate(datestr);
            System.err.println("fixedDate : " + fixedDate_str);

            // Midle date is the date the user insert
            Date midDate = UtilDates.stringToDate(fixedDate_str);

            //===============================================
            //==================Year evaluation==============
            //===============================================
            // Get number of days from year 0 to the midle date
            int daysFromYear0 = UtilDates.daysFromYear0(midDate);
            System.err.println("daysFromYear0 : " + daysFromYear0);

            // Current year
            int year = midDate.getYear();

            int minRange = daysFromYear0 - UtilDates.getDaysInYears(year, -2);
            int maxRange = daysFromYear0 + UtilDates.getDaysInYears(year, +2);

            int minWindow = daysFromYear0 + 1;
            int maxWindow = daysFromYear0 + UtilDates.getDaysInYear(year);

            // Values for a Month
            if (isMonthValue) {
                int daysMonth = UtilDates.daysInMonth(midDate.getYear(), midDate.getMonth());

                String fixedDate_str_ = UtilDates.formatDate(String.valueOf(midDate.getYear()));
                Date midDate_ = UtilDates.stringToDate(fixedDate_str_);

                int daysFromYear0_ = UtilDates.daysFromYear0(midDate_);

                minRange = daysFromYear0_;
                int[] maxRangeYears_ = new int[]{year};
//                maxRange = daysFromYear0_ + UtilDates.getDaysInYears(maxRangeYears_);

                minWindow = daysFromYear0;
                maxWindow = daysFromYear0 + daysMonth - 1;

            }

            // Remove print later
            System.err.println("minRange : " + minRange);
            System.err.println("minWindow : " + minWindow);
            System.err.println("maxWindow : " + maxWindow);
            System.err.println("maxRange : " + maxRange);

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
        String start_date_str = UtilDates.dateFromDays(start_num_days);
        String end_date_str = UtilDates.dateFromDays(end_num_days);
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
