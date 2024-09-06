package org.openstreetmap.josm.plugins.ohmdatefilter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;

import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.data.osm.search.SearchSetting;
import javax.swing.JOptionPane;
import org.openstreetmap.josm.gui.HelpAwareOptionPane;

import org.openstreetmap.josm.gui.SideButton;
import org.openstreetmap.josm.gui.dialogs.ToggleDialog;
import org.openstreetmap.josm.tools.Shortcut;

public class OHMDateFilterDialog extends ToggleDialog {

    DateHandler dateHandler = new DateHandler();
    private JTextField jTextFieldStartDate = new JTextField("1890");
    private JTextField jTextFieldEndDate = new JTextField("1950");
    private JLabel jLabelCurrentDate = new JLabel();
    private JTextField jTextSearchFormat = new JTextField();
    // Checkboxes
    private JCheckBox jcheckbox_start_date_null = new JCheckBox("start_date", false); // Start as true
    private JCheckBox jcheckbox_end_date_null = new JCheckBox("end_date", false);     // Start as true

    // Slider
    JPanel sliderPanel = new JPanel(new GridBagLayout());
    private JSlider jSliderDate = new JSlider(JSlider.HORIZONTAL);

    private JTextField jTextSettings = new JTextField();

    public OHMDateFilterDialog() {
        super(tr("OpenHistoricalMap Date Filter"),
                "iconohmdatefilter16",
                tr("Open OpenHistoricalMap date filter window"),
                Shortcut.registerShortcut("ohmDateFilter", tr("Toggle: {0}", tr("OpenHistoricalMap Date Filter")), KeyEvent.VK_I,
                        Shortcut.ALT_CTRL_SHIFT), 130);

        JosmAction oHMActionSaver = new JosmAction(
                tr("Save Filter"), "save",
                tr("Save Filter"),
                Shortcut.registerShortcut("tools:ohm_save_filter", tr("More tools: {0}", tr("Save Filter")),
                        KeyEvent.VK_S, Shortcut.SHIFT), true) {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterMapData(true, false);
            }
        };

        JosmAction oHMActionReset = new JosmAction(
                tr("Reset Filters"), "reset",
                tr("Reset Filters"),
                Shortcut.registerShortcut("tools:ohm_reset_filter", tr("More tools: {0}", tr("Reset Filters")),
                        KeyEvent.VK_R, Shortcut.SHIFT), true) {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterMapData(false, true);
            }
        };

        SideButton sideButtonSaveFilter = new SideButton(oHMActionSaver);
        SideButton sideButtonReset = new SideButton(oHMActionReset);

        //Main panel
        JPanel mainPanel = new JPanel(new GridLayout(3, 1));
        mainPanel.setMinimumSize(new Dimension(130, 30));

        //Add panels 
        mainPanel.add(inputDatePanel());
        mainPanel.add(checkDatesPanel());

        mainPanel.add(dateSliderPanel());

        createLayout(mainPanel, false, Arrays.asList(sideButtonSaveFilter, sideButtonReset));
    }

    private JPanel inputDatePanel() {
        // Imput panel to add initial date
        JPanel panel = new JPanel(new GridLayout(1, 3));
        panel.setBorder(javax.swing.BorderFactory.createTitledBorder("Set Date and Range value"));
        panel.add(jTextFieldStartDate);
        panel.add(jTextFieldEndDate);

        JButton jButtonSetYear = new JButton("Set");
        panel.add(jButtonSetYear);

        // Add ActionListener to the button
        jButtonSetYear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputStartDate = jTextFieldStartDate.getText();
                String inputEndDate = jTextFieldEndDate.getText();

                System.out.println("======================");
                System.out.println(inputStartDate);
                System.out.println(inputEndDate);

                if (inputStartDate != null && !inputStartDate.isEmpty() && inputEndDate != null && !inputEndDate.isEmpty()) {

                    String fixedInputStartDate = formatDateString(inputStartDate, true);
                    String fixedInputEndDate = formatDateString(inputEndDate, false);
//                    System.out.println(fixedInputStartDate);
//                    System.out.println(fixedInputEndDate);
                    if (fixedInputStartDate != null && fixedInputEndDate != null) {
                        dateHandler.setStartDateString(fixedInputStartDate);
                        dateHandler.setEndDateString(fixedInputEndDate);
                        jSliderDate.setMinimum(0);
                        jSliderDate.setValue(0);
                        jSliderDate.setMaximum(dateHandler.getRangeInDays());
                    }
                }
            }

        });

        return panel;
    }

    private JPanel checkDatesPanel() {
        // Create a panel with a titled border "Include"
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Include null values for"));
        // Add checkboxes to the panel, arranged horizontally
        panel.add(jcheckbox_start_date_null);
        panel.add(jcheckbox_end_date_null);

        return panel;
    }

    private JPanel dateSliderPanel() {
        sliderPanel.setBorder(titlePanel("Date"));
        jSliderDate.setPreferredSize(new Dimension(300, 50));
        jSliderDate.addChangeListener(e -> {
            filterMapData(false, false);
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        sliderPanel.add(jSliderDate, gbc);
        return sliderPanel;
    }

    private void filterMapData(boolean saveFilter, boolean resetFilters) {

        int currentValue = jSliderDate.getValue();
        String currentDateString = dateHandler.addDaysToStartDate(currentValue);
        // Set date in the panel title
        sliderPanel.setBorder(titlePanel("start_date > " + currentDateString + " AND end_date < " + currentDateString));
//        System.out.println("Current slider value: " + currentValue + "<>" + currentDateString);

        String searchFormat_start_date = getSearchFormat(currentDateString, true);
        String searchFormat_end_date = getSearchFormat(currentDateString, false);

        // Convert searchFormat into searchSetting
        SearchSetting searchSetting_start_date = getSearchSetting(searchFormat_start_date);
        SearchSetting searchSetting_end_date = getSearchSetting(searchFormat_end_date);

        // Include 
        boolean include_start_date_null = jcheckbox_start_date_null.isSelected();
        boolean include_end_date_null = jcheckbox_end_date_null.isSelected();

        OHMDateFilterFunctions.applyDateFilter(searchSetting_start_date, searchSetting_end_date, saveFilter, resetFilters, include_start_date_null, include_end_date_null);
    }

    private String getSearchFormat(String currentDate, boolean start_date) {

        if (start_date) {
            return "child(type:relation start_date>\"" + currentDate + "\") OR start_date>\"" + currentDate + "\"";
        } else {
            return "child(type:relation end _date<\"" + currentDate + "\") OR end_date<\"" + currentDate + "\"";
        }

//        if (start_date) {
//            return "start_date>\"" + currentDate + "\"";
//        } else {
//            return "end_date<\"" + currentDate + "\"";
//        }
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

    private TitledBorder titlePanel(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(title);
        border.setTitleColor(Color.decode("#337AFF"));
        border.setTitleFont(border.getTitleFont().deriveFont(Font.BOLD));
        return border;
    }

    public String formatDateString(String dateString, boolean startDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (startDate) {
            // Check the format of the date string and complete it to 'yyyy-MM-dd' for start date
            if (dateString.matches("\\d{4}")) {  // Only year
                dateString += "-01-01";  // Complete to 'yyyy-01-01'
            } else if (dateString.matches("\\d{4}-\\d{2}")) {  // Year and month
                dateString += "-01";  // Complete to 'yyyy-MM-01'
            }

        } else {

            // Check the format of the date string and complete it to 'yyyy-MM-dd' for start date
            if (dateString.matches("\\d{4}")) {  // Only year
                dateString += "-12-31";  // Complete to 'yyyy-01-01'
            } else if (dateString.matches("\\d{4}-\\d{2}")) {  // Year and month
                dateString += "-01";  // Complete to 'yyyy-MM-01'
            }

        }

        try {
            // Parse the completed date string to LocalDate
            LocalDate date = LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Error: Format should be 'yyyy-MM-dd'.");

            String message = "";
            if (startDate) {
                message = "Set a valid start_date in valid format yyyy-MM-dd";
            } else {
                message = "Set a valid end_date in valid format yyyy-MM-dd";

            }

            HelpAwareOptionPane.showOptionDialog(
                    null,
                    message,
                    "Date Issue",
                    JOptionPane.WARNING_MESSAGE,
                    null
            );
            return null;
        }

        return dateString;
    }

}
