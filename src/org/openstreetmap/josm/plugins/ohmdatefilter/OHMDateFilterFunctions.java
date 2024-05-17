package org.openstreetmap.josm.plugins.ohmdatefilter;

import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.data.osm.search.SearchCompiler;

import org.openstreetmap.josm.data.osm.search.SearchSetting;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.tools.Logging;
import org.openstreetmap.josm.data.osm.Filter;
import org.openstreetmap.josm.data.osm.FilterModel;

public class OHMDateFilterFunctions {

    /**
     * Applies a date filter based on a provided SearchSetting object.
     *
     * @param searchSetting The search settings including the date range.
     */
    public static void applyDateFilter(SearchSetting searchSetting) {

        DataSet currentDataSet = getCurrentDataSet();

        if (currentDataSet == null) {
            Logging.warn("No active dataset available for filtering.");
            return;
        }

        try {
            SearchCompiler.compile(searchSetting);

            FilterModel filterModel = new FilterModel();
            Filter filter = new Filter();
            filter.text = searchSetting.text;
            filter.caseSensitive = searchSetting.caseSensitive;
            filter.regexSearch = searchSetting.regexSearch;
            filter.mapCSSSearch = searchSetting.mapCSSSearch;
            filter.allElements=searchSetting.allElements;
            filter.inverted=true;
            filterModel.addFilter(filter);
            filterModel.executeFilters();

        } catch (Exception e) {
            Logging.error("Error applying date filter: " + e.getMessage());
        }
    }

    private static DataSet getCurrentDataSet() {
        // Use MainApplication to get the current active dataset
        return MainApplication.getLayerManager().getEditDataSet();
    }
}
