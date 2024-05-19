package org.openstreetmap.josm.plugins.ohmdatefilter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.data.osm.search.SearchCompiler;

import org.openstreetmap.josm.data.osm.search.SearchSetting;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.tools.Logging;
import org.openstreetmap.josm.data.osm.Filter;
import org.openstreetmap.josm.data.osm.FilterModel;
import org.openstreetmap.josm.data.osm.Way;
import org.openstreetmap.josm.gui.dialogs.FilterDialog;
import org.openstreetmap.josm.gui.dialogs.FilterTableModel;

public class OHMDateFilterFunctions {

    /**
     * Applies a date filter based on a provided SearchSetting object.
     *
     * @param searchSetting The search settings including the date range.
     */
    public static void applyDateFilter(SearchSetting searchSetting, boolean saveFilter) {

        DataSet currentDataSet = getCurrentDataSet();
        if (currentDataSet == null) {
            Logging.warn("No active dataset available for filtering.");
            return;
        }

        try {
            // Validate searchSetting
            SearchCompiler.compile(searchSetting);
            // Create filter
            Filter filter = new Filter();
            filter.text = searchSetting.text;
            filter.caseSensitive = searchSetting.caseSensitive;
            filter.regexSearch = searchSetting.regexSearch;
            filter.mapCSSSearch = searchSetting.mapCSSSearch;
            filter.allElements = searchSetting.allElements;
            filter.inverted = true;

            if (saveFilter) {
                // Save filter in filer window
                // Get filterDialog and apply filter
                FilterDialog filterDialog = MainApplication.getMap().filterDialog;
                FilterTableModel filterModel = filterDialog.getFilterModel();
                filterModel.addFilter(filter);
                filterModel.executeFilters();
                // Update Filters
                filterDialog.setVisible(true);
                filterDialog.unfurlDialog();
            } else {
                // Just apply filter in the map
                FilterModel filterModel = new FilterModel();
                filterModel.addFilter(filter);
                filterModel.executeFilters();
            }

        } catch (Exception e) {
            Logging.error("Error applying date filter: " + e.getMessage());
        }
    }

    private static DataSet getCurrentDataSet() {
        // Use MainApplication to get the current active dataset
        return MainApplication.getLayerManager().getEditDataSet();
    }

    public static int[] getMinMax() {
        DataSet dataSet = MainApplication.getLayerManager().getEditDataSet();

        Date minStartDate = null;
        Date maxEndDate = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (Way way : dataSet.getWays()) {
            String startDateString = way.getKeys().get("start_date");
            String endDateString = way.getKeys().get("end_date");

            System.err.println(startDateString);
            System.err.println(endDateString);

            try {
                if (startDateString != null) {
                    Date startDate = dateFormat.parse(startDateString);
                    if (minStartDate == null || startDate.before(minStartDate)) {
                        minStartDate = startDate;
                    }
                }

                if (endDateString != null) {
                    Date endDate = dateFormat.parse(endDateString);
                    if (maxEndDate == null || endDate.after(maxEndDate)) {
                        maxEndDate = endDate;
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (minStartDate != null && maxEndDate != null) {
            System.err.println(minStartDate.getYear());

            System.err.println(maxEndDate.getYear());
            return new int[]{(int) minStartDate.getYear(), (int) maxEndDate.getYear()};
        } else {
            return null;
        }
    }
}
