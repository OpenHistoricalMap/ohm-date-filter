package org.openstreetmap.josm.plugins.ohmdatefilter;

import java.awt.Dimension;
import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import javax.swing.JButton;

import javax.swing.JLabel;
import javax.swing.JPanel;
import org.openstreetmap.josm.data.osm.search.SearchSetting;

import org.openstreetmap.josm.gui.SideButton;
import org.openstreetmap.josm.gui.dialogs.ToggleDialog;
import org.openstreetmap.josm.tools.Shortcut;

public class OHMDateFilterDialog extends ToggleDialog {

    private OHMDateFilterCalendar start_dateFilterCalendar;
    private OHMDateFilterCalendar end_dateFilterCalendar;
    private JLabel dateRangeLabel; // Label to display the selected date range

    public OHMDateFilterDialog() {
        super(tr("OpenHistoricalMap Date Filter"),
                "iconohmdatefilter16",
                tr("Open OpenHistoricalMap date filter window"),
                Shortcut.registerShortcut("ohmDateFilter", tr("Toggle: {0}", tr("OpenHistoricalMap Date Filter")), KeyEvent.VK_I,
                        Shortcut.ALT_CTRL_SHIFT), 90);

        JPanel main_panel = new JPanel(new GridLayout(2, 1));

        // Add calendar textfile and picker
        main_panel.add(datesPickeraPanel());

        JPanel details_panel = new JPanel(new GridLayout(2, 1));
        dateRangeLabel = new JLabel("Date Range: Not set"); // Initial text
        details_panel.add(dateRangeLabel);

        JButton updateButton = new JButton("Filter data");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDateRange();
            }
        });

        details_panel.add(updateButton);

        main_panel.add(details_panel);

//        // Listen for changes in both date pickers
//        // Listen for changes in both date pickers
//        start_dateFilterCalendar.addDateChangeListener(e -> updateDateRange());
//        end_dateFilterCalendar.addDateChangeListener(e -> updateDateRange());
        createLayout(main_panel, false, Arrays.asList(new SideButton[]{}));
    }

    private JPanel datesPickeraPanel() {

        JPanel calendars_panel = new JPanel(new GridLayout(1, 2));
        calendars_panel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

//        Dimension fixedHeight = new Dimension(0, 80);
//        calendars_panel.setPreferredSize(fixedHeight);
//        calendars_panel.setMinimumSize(fixedHeight);
//        calendars_panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        start_dateFilterCalendar = new OHMDateFilterCalendar(new Date(), "Start Date");
        end_dateFilterCalendar = new OHMDateFilterCalendar(new Date(), "End Date");

        calendars_panel.add(start_dateFilterCalendar);
        calendars_panel.add(end_dateFilterCalendar);

        return calendars_panel;
    }

    private void updateDateRange() {
        Date startDate = start_dateFilterCalendar.getSelectedDate();
        Date endDate = end_dateFilterCalendar.getSelectedDate();
        
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedStartDate = dateFormat.format(startDate);
        String formattedEndDate = dateFormat.format(endDate);

        if (OHMDateFilterFunctionsDates.validateDateRange(startDate, endDate)) {

            System.out.println("Valid");

            String difference = OHMDateFilterFunctionsDates.calculateDateDifference(startDate, endDate);
            System.out.println("Date Range is valid.");
            System.out.println("Difference: " + difference);
            dateRangeLabel.setText("Date Range: " + difference);

            SearchSetting searchSetting = new SearchSetting();
            searchSetting.text = "start_date>" + formattedStartDate + " AND end_date<" + formattedEndDate;
//            searchSetting.text = "start_date>1960-01-01 AND end_date<2000-01-01";

            System.out.println(searchSetting.text);

            searchSetting.caseSensitive = false;
            searchSetting.regexSearch = false;
            searchSetting.mapCSSSearch = false;
            searchSetting.allElements = true;

            OHMDateFilterFunctions.applyDateFilter(searchSetting);

        } else {
            System.out.println("Date Range is invalid. Start date must not be later than end date.");
        }
    }

}
