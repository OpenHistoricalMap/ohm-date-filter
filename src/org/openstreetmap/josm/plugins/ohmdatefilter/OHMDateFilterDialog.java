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
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;

import org.openstreetmap.josm.actions.JosmAction;
import javax.swing.JOptionPane;
import org.openstreetmap.josm.gui.HelpAwareOptionPane;

import org.openstreetmap.josm.gui.SideButton;
import org.openstreetmap.josm.gui.dialogs.ToggleDialog;
import org.openstreetmap.josm.tools.Shortcut;

public class OHMDateFilterDialog extends ToggleDialog {

    String defaultStartDate = "1950";
    String defaultEndDate = "2024";
    DateHandler dateHandler = new DateHandler();
    FilterList filterList = new FilterList();
    private JTextField jTextFieldStartDate = new JTextField(defaultStartDate);
    private JTextField jTextFieldEndDate = new JTextField(defaultEndDate);
    // Checkboxes for null data
    private JCheckBox jcheckboxStartSateNull = new JCheckBox("start_date", true);
    private JCheckBox jcheckboxEndDateNull = new JCheckBox("end_date", true);
    // Checkboxes for null relations
    private JCheckBox jcheckboxRelationOnly = new JCheckBox("Filter Relations and Children", false);

    // Slider
    JPanel sliderPanel = new JPanel(new GridBagLayout());
    private JSlider jSliderDate = new JSlider(JSlider.HORIZONTAL);

    public OHMDateFilterDialog() {
        super(tr("OpenHistoricalMap Date Filter"),
                "iconohmdatefilter16",
                tr("Open OpenHistoricalMap date filter window"),
                Shortcut.registerShortcut("ohmDateFilter", tr("Toggle: {0}", tr("OpenHistoricalMap Date Filter")), KeyEvent.VK_I,
                        Shortcut.ALT_CTRL_SHIFT), 100);

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
        mainPanel.setMinimumSize(new Dimension(100, 30));
        //Add panels 
        mainPanel.add(dateSliderPanel());
        mainPanel.add(inputDatePanel());
        mainPanel.add(onlyRelation());
        SetSliderVaues(defaultStartDate, defaultEndDate);
        createLayout(mainPanel, false, Arrays.asList(sideButtonSaveFilter, sideButtonReset));
    }

    private JPanel onlyRelation() {
        JPanel panel = new JPanel(new GridLayout(1, 1));
        panel.setMinimumSize(new Dimension(130, 20));
        panel.add(jcheckboxRelationOnly);
        jcheckboxRelationOnly.addActionListener(e -> {
            filterMapData(false, false);
        });
        return panel;
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
                SetSliderVaues(inputStartDate, inputEndDate);

            }
        });

        return panel;
    }

    private JPanel checkNullObjects() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Display objects with null values"));
        panel.add(jcheckboxStartSateNull);
        panel.add(jcheckboxEndDateNull);

        jcheckboxStartSateNull.addActionListener(e -> {
            filterMapData(false, false);
        });

        jcheckboxEndDateNull.addActionListener(e -> {
            filterMapData(false, false);
        });

        return panel;
    }

    private JPanel dateSliderPanel() {
        sliderPanel.setBorder(titlePanel("Date"));
        jSliderDate.setPreferredSize(new Dimension(100, 50));
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
        sliderPanel.setBorder(titlePanel("Current date :" + currentDateString));

        // Include 
        boolean includeStartDateNull = !jcheckboxStartSateNull.isSelected();
        boolean includeEndDateNull = !jcheckboxEndDateNull.isSelected();

        boolean isRelationOnly = jcheckboxRelationOnly.isSelected();

        //Include null values for start_date
        filterList.getExistingStartDate().setActive(includeStartDateNull);

        //Include null values for end_date
        filterList.getExistingEndDate().setActive(includeEndDateNull);

        //Start date for nodes,ways and relations
        filterList.getStartDate().setText("start_date>\"" + currentDateString + "\"");
        filterList.getStartDate().setActive(!isRelationOnly);

        //End date for nodes,ways and relations
        filterList.getEndDate().setText("end_date<\"" + currentDateString + "\"");
        filterList.getEndDate().setActive(!isRelationOnly);

        //Start date only for relations
        filterList.getOnlyRelationsStartDate().setText("child(type:relation start_date>\"" + currentDateString + "\")");
        filterList.getOnlyRelationsStartDate().setActive(isRelationOnly);

        //End date only for relations
        filterList.getOnlyRelationsEndDate().setText("child(type:relation end_date<\"" + currentDateString + "\")");
        filterList.getOnlyRelationsEndDate().setActive(isRelationOnly);

        filterList.getOnlyRelations().setActive(isRelationOnly);

        OHMDateFilterFunctions.applyDateFilter(filterList, saveFilter, resetFilters);
    }

    private TitledBorder titlePanel(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(title);
        border.setTitleColor(Color.decode("#337AFF"));
        border.setTitleFont(border.getTitleFont().deriveFont(Font.BOLD));
        return border;
    }

    public static String formatDateString(String dateString, boolean startDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Validate that the year is 1 or greater
        if (dateString.matches("\\d{4}") || dateString.matches("\\d{4}-\\d{2}") || dateString.matches("\\d{4}-\\d{2}-\\d{2}")) {
            int year = Integer.parseInt(dateString.substring(0, 4));
            if (year < 1) {
                JOptionPane.showMessageDialog(
                        null,
                        "The year must be 1 or greater (AD).",
                        "Date Issue",
                        JOptionPane.WARNING_MESSAGE
                );
                return null;
            }
        }

        // Adjust the dateString based on whether it's a start or end date
        if (startDate) {
            if (dateString.matches("\\d{4}")) {
                dateString += "-01-01";
            } else if (dateString.matches("\\d{4}-\\d{2}")) {
                dateString += "-01";
            }
        } else {
            if (dateString.matches("\\d{4}")) {
                dateString += "-12-31";
            } else if (dateString.matches("\\d{4}-\\d{2}")) {
                YearMonth yearMonth = YearMonth.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM"));
                dateString = yearMonth.atEndOfMonth().toString();
            }
        }

        // Validate the final date format
        try {
            LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            String message;
            if (startDate) {
                message = "Set a valid start_date in the format yyyy-MM-dd.";
            } else {
                message = "Set a valid end_date in the format yyyy-MM-dd.";
            }
            JOptionPane.showMessageDialog(
                    null,
                    message,
                    "Date Issue",
                    JOptionPane.WARNING_MESSAGE
            );
            return null;
        }

        return dateString;
    }

    private void SetSliderVaues(String inputStartDate, String inputEndDate) {
        if (inputStartDate != null && !inputStartDate.isEmpty() && inputEndDate != null && !inputEndDate.isEmpty()) {
            String fixedInputStartDate = formatDateString(inputStartDate, true);
            String fixedInputEndDate = formatDateString(inputEndDate, false);
            if (fixedInputStartDate != null && fixedInputEndDate != null) {
                dateHandler.setStartDateString(fixedInputStartDate);
                dateHandler.setEndDateString(fixedInputEndDate);

                int numDays = dateHandler.getRangeInDays();
                if (numDays <= 0) {
                    HelpAwareOptionPane.showOptionDialog(
                            null,
                            "Start date should be greater than End date.",
                            "Date Issue",
                            JOptionPane.WARNING_MESSAGE,
                            null
                    );
                } else {
                    jSliderDate.setMinimum(0);
                    jSliderDate.setValue(0);
                    jSliderDate.setMaximum(numDays);
                }
            }
        }
    }
}
