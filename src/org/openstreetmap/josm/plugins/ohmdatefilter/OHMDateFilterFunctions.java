package org.openstreetmap.josm.plugins.ohmdatefilter;

import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.data.osm.search.SearchCompiler;

import org.openstreetmap.josm.data.osm.search.SearchSetting;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.tools.Logging;
import org.openstreetmap.josm.data.osm.Filter;
import org.openstreetmap.josm.data.osm.FilterModel;
import org.openstreetmap.josm.gui.dialogs.FilterDialog;
import org.openstreetmap.josm.gui.dialogs.FilterTableModel;

public class OHMDateFilterFunctions {

    /**
     * Applies a date filter based on a provided SearchSetting object.
     *
     * @param searchSetting The search settings including the date range.
     */
    public static void applyDateFilter(SearchSetting searchSetting_start_date, SearchSetting searchSetting_end_date, boolean saveFilter, boolean resetFilters) {

        DataSet currentDataSet = getCurrentDataSet();
        if (currentDataSet == null) {
            Logging.warn("No active dataset available for filtering.");
            return;
        }

        try {
            // Validate searchSetting
            SearchCompiler.compile(searchSetting_start_date);
            SearchCompiler.compile(searchSetting_end_date);

            // Create filter for start date
            Filter filter_start_date = new Filter();
            filter_start_date.text = searchSetting_start_date.text;
            filter_start_date.caseSensitive = searchSetting_start_date.caseSensitive;
            filter_start_date.regexSearch = searchSetting_start_date.regexSearch;
            filter_start_date.mapCSSSearch = searchSetting_start_date.mapCSSSearch;
            filter_start_date.allElements = searchSetting_start_date.allElements;
            filter_start_date.inverted = false;

            // Exist start_date
            Filter filter_start_date_exist = new Filter();
            filter_start_date_exist.text = "start_date";
            filter_start_date_exist.inverted = true;

            // Create filter for end date
            Filter filter_end_date = new Filter();
            filter_end_date.text = searchSetting_end_date.text;
            filter_end_date.caseSensitive = searchSetting_end_date.caseSensitive;
            filter_end_date.regexSearch = searchSetting_end_date.regexSearch;
            filter_end_date.mapCSSSearch = searchSetting_end_date.mapCSSSearch;
            filter_end_date.allElements = searchSetting_end_date.allElements;
            filter_end_date.inverted = false;

            //Exist end_date
            Filter filter_end_date_exist = new Filter();
            filter_end_date_exist.text = "end_date";
            filter_end_date_exist.inverted = true;

            // Create filter for reset
            Filter filter_reset = new Filter();
            filter_reset.text = "";
            filter_reset.caseSensitive = searchSetting_start_date.caseSensitive;
            filter_reset.regexSearch = searchSetting_start_date.regexSearch;
            filter_reset.mapCSSSearch = searchSetting_start_date.mapCSSSearch;
            filter_reset.allElements = searchSetting_start_date.allElements;
            filter_reset.inverted = true;

            if (saveFilter) {
                // Save filter in filer window
                FilterDialog filterDialog = MainApplication.getMap().filterDialog;
                FilterTableModel filterModel = filterDialog.getFilterModel();

                filterModel.addFilter(filter_start_date_exist);
                filterModel.addFilter(filter_end_date_exist);
                filterModel.addFilter(filter_start_date);
                filterModel.addFilter(filter_end_date);

                filterModel.executeFilters();
                // Update Filters
                filterDialog.setVisible(true);
                filterDialog.unfurlDialog();
            } else {
                // Just apply filter in the map
                FilterModel filterModel = new FilterModel();
                filterModel.addFilter(filter_start_date_exist);
                filterModel.addFilter(filter_end_date_exist);
                filterModel.addFilter(filter_start_date);
                filterModel.addFilter(filter_end_date);
                filterModel.executeFilters();
            }

            if (resetFilters) {
                // Reset
                FilterModel filterModel = new FilterModel();
                filterModel.addFilter(filter_reset);
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

}
