package org.openstreetmap.josm.plugins.ohmdatefilter;

import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.data.osm.search.SearchCompiler;

import org.openstreetmap.josm.data.osm.search.SearchCompiler.Match;
import org.openstreetmap.josm.data.osm.search.SearchSetting;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.layer.OsmDataLayer;
import org.openstreetmap.josm.tools.Logging;
import java.util.Set;
import java.util.stream.Collectors;
import org.openstreetmap.josm.data.osm.Filter;

public class OHMDateFilterFunctions {

    /**
     * Applies a date filter based on a provided SearchSetting object.
     *
     * @param searchSetting The search settings including the date range.
     */
    public static void applyDateFilter(SearchSetting searchSetting) {
        DataSet currentDataSet = getCurrentDataSet();
        System.out.println(currentDataSet);

        if (currentDataSet == null) {
            Logging.warn("No active dataset available for filtering.");
            return;
        }

        try {
            // Compile the search query from the settings
            Match matcher = SearchCompiler.compile(searchSetting);
// Correct usage of the compile method
            Set<OsmPrimitive> filteredPrimitives = currentDataSet.allPrimitives().stream()
                    .filter(matcher::match)
                    .collect(Collectors.toSet());

            
            
//            System.out.println("matcher");
//
////            MainApplication.getMap().repaint();
//            // Select the filtered primitives in the dataset
//            currentDataSet.setSelected(filteredPrimitives);
//            // Set non-filtered primitives as 'incomplete' to disable editing
//
//            Filter filter = new Filter(searchSetting);
//            filter.getPreferenceEntry();
//
//            Logging.info("Filtered and selected " + filteredPrimitives.size() + " primitives based on date criteria.");

        } catch (Exception e) {
            Logging.error("Error applying date filter: " + e.getMessage());
        }
    }

    /**
     * Gets the current dataset from the active data layer.
     *
     * @return the active dataset, or null if there is no active OsmDataLayer
     */
    public static DataSet getCurrentDataSet() {
        if (MainApplication.getLayerManager().getActiveLayer() instanceof OsmDataLayer) {
            return ((OsmDataLayer) MainApplication.getLayerManager().getActiveLayer()).getDataSet();
        }
        return null;
    }
}
