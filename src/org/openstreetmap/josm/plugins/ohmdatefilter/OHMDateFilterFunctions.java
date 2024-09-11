package org.openstreetmap.josm.plugins.ohmdatefilter;

import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.data.osm.Filter;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.tools.Logging;
import org.openstreetmap.josm.data.osm.FilterModel;
import org.openstreetmap.josm.gui.dialogs.FilterDialog;
import org.openstreetmap.josm.gui.dialogs.FilterTableModel;

public class OHMDateFilterFunctions {

    public static void applyDateFilter(FilterList filterList, boolean saveFilter, boolean resetFilters) {

        DataSet currentDataSet = getCurrentDataSet();
        if (currentDataSet == null) {
            Logging.warn("No active dataset available for filtering.");
            return;
        }

        try {
            System.out.print(filterList.toString());
            if (saveFilter) {
                // Save filter in filer window
                FilterDialog filterDialog = MainApplication.getMap().filterDialog;
                FilterTableModel filterModel = filterDialog.getFilterModel();

                for (FilterItem filterItem : filterList.getFilters()) {
                    Filter filter = new Filter();
                    if (filterItem.getActive()) {
                        filter.text = filterItem.getText();
                        filter.inverted = filterItem.getInverted();
                        filterModel.addFilter(filter);
                    }
                }

                filterModel.executeFilters();
                // Update Filters
                filterDialog.setVisible(true);
                filterDialog.unfurlDialog();
            } else {
                // Just apply filter in the map
                FilterModel filterModel = new FilterModel();

                for (FilterItem filterItem : filterList.getFilters()) {
                    Filter filter = new Filter();
                    if (filterItem.getActive()) {
                        filter.text = filterItem.getText();
                        filter.inverted = filterItem.getInverted();
                        filterModel.addFilter(filter);
                    }
                }

                filterModel.executeFilters();
            }

            if (resetFilters) {
                // Reset
                FilterModel filterModel = new FilterModel();
                Filter filter_reset = new Filter();
                filter_reset.text = "";
                filter_reset.inverted = true;
                filterModel.addFilter(filter_reset);
                filterModel.executeFilters();
            }

        } catch (Exception e) {
            Logging.error("Error applying date filter: " + e.getMessage());
        }
    }

    private static DataSet getCurrentDataSet() {
        return MainApplication.getLayerManager().getEditDataSet();
    }

}
