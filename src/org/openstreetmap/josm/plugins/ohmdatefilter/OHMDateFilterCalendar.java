package org.openstreetmap.josm.plugins.ohmdatefilter;

import com.toedter.calendar.JCalendar;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OHMDateFilterCalendar extends JPanel {

    private JCalendar calendar;
    private JTextField dateField;
    private JFrame calendarFrame;
    private JButton triggerButton;

    // Existing default constructor
    public OHMDateFilterCalendar() {
        initializeComponents();
    }

    // Overloaded constructor to set a default date
    public OHMDateFilterCalendar(Date defaultDate) {
        initializeComponents(); // Initialize components
        setDate(defaultDate); // Set the default date
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        // TextField to display the date
        dateField = new JTextField();
        dateField.setEditable(false);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Create a button with "..." to trigger the calendar
        triggerButton = new JButton("...");
        triggerButton.setMargin(new Insets(0, 0, 0, 0));
        triggerButton.setFocusable(false);
        triggerButton.setFocusPainted(false);
        triggerButton.setBorderPainted(false);
        triggerButton.setContentAreaFilled(false);
        triggerButton.setOpaque(false);
        triggerButton.addActionListener(e -> {
            if (calendarFrame.isVisible()) {
                calendarFrame.setVisible(false);
            } else {
                positionCalendarFrame();
                calendarFrame.setVisible(true);
            }
        });

        // Add the trigger button to the east side of the text field
        dateField.setLayout(new BorderLayout());
        dateField.add(triggerButton, BorderLayout.EAST);

        // JCalendar setup
        calendar = new JCalendar();
        calendar.addPropertyChangeListener("calendar", evt -> {
            Date date = calendar.getDate();
            dateField.setText(dateFormat.format(date));
            calendarFrame.setVisible(false); // Hide after selection
            System.out.println("Date selected: " + date);
        });

        // Frame to hold the JCalendar
        calendarFrame = new JFrame("Select a Date");
        calendarFrame.setUndecorated(true);
        calendarFrame.setLayout(new BorderLayout());
        calendarFrame.add(calendar, BorderLayout.CENTER);

        // Add close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> calendarFrame.setVisible(false));
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(closeButton);
        calendarFrame.add(bottomPanel, BorderLayout.SOUTH);

        calendarFrame.pack();
        calendarFrame.setVisible(false);

        // Adding components to this panel
        add(dateField, BorderLayout.CENTER);
    }

    public void addDateChangeListener(PropertyChangeListener listener) {
        calendar.addPropertyChangeListener("calendar", listener);
    }

    public void setDate(Date date) {
        calendar.setDate(date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateField.setText(dateFormat.format(date)); // Update the text field when setting the date
    }

    private void positionCalendarFrame() {
        SwingUtilities.invokeLater(() -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (parentFrame != null) {
                int x = parentFrame.getLocation().x + (parentFrame.getWidth() - calendarFrame.getWidth()) / 2;
                int y = parentFrame.getLocation().y + (parentFrame.getHeight() - calendarFrame.getHeight()) / 2;
                calendarFrame.setLocation(x, y);
            }
        });
    }

    public Date getSelectedDate() {
        return calendar.getDate(); // Directly return the date from the calendar
    }
}
