package org.openstreetmap.josm.plugins.ohmdatefilter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;

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

//    String[] listYears = new String[]{"2 years", "5 years", "10 years", "50 years", "100 years", "200 years", "500 years"};
//    String[] listMonths = new String[]{"2 Month", "6 Months", "12 Months", "24 Months"};
//    String[] listDays = new String[]{"1 Days", "5 days", "10 days"};
//
//    Boolean updateComoBoxYear = false;
//    Boolean updateComoBoxMonth = true;
//    Boolean updateComoBoxDay = true;
//
//    String filter_by = "start_date";
//    int current_lower_value = 0;
//    int current_upper_value = 0;
    DateHandler dateHandler = new DateHandler();
    private JTextField jTextFieldStartDate = new JTextField();
    private JTextField jTextFieldEndDate = new JTextField();
    private JLabel jLabelCurrentDate = new JLabel();
    private JTextField jTextSearchFormat = new JTextField();

    // Slider
    JPanel sliderPanel = new JPanel(new GridBagLayout());
    private JSlider jSliderDate = new JSlider(JSlider.HORIZONTAL);

    private JTextField jTextSettings = new JTextField();
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
        mainPanel.setMinimumSize(new Dimension(500, 30));

        //Add panels 
        mainPanel.add(imputDatePanel());

        mainPanel.add(dateSliderPanel());

        mainPanel.add(jTextSearchFormat);

        createLayout(mainPanel, false, Arrays.asList(sideButtonSaveFilter, sideButtonReset));
    }

    private JPanel imputDatePanel() {
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
                    dateHandler.setStartDateString(inputStartDate);
                    dateHandler.setEndDateString(inputEndDate);
                    jSliderDate.setMinimum(0);
                    jSliderDate.setValue(0);
                    jSliderDate.setMaximum(dateHandler.getRangeInDays());
                }
            }

        });

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
        sliderPanel.setBorder(titlePanel(currentDateString));
        System.out.println("Current slider value: " + currentValue + "<>" + currentDateString);
        String searchFormat_start_date = getSearchFormat(currentDateString, true);
        String searchFormat_end_date = getSearchFormat(currentDateString, false);
        jTextSearchFormat.setText(searchFormat_start_date + "---AND---" + searchFormat_end_date);

        // Convert searchFormat into searchSetting
        SearchSetting searchSetting_start_date = getSearchSetting(searchFormat_start_date);
        SearchSetting searchSetting_end_date = getSearchSetting(searchFormat_end_date);

        // Apply filter
        OHMDateFilterFunctions.applyDateFilter(searchSetting_start_date, searchSetting_end_date, saveFilter, resetFilters);
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

}
