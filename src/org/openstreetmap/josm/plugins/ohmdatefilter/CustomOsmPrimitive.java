/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.openstreetmap.josm.plugins.ohmdatefilter;
import org.openstreetmap.josm.data.osm.OsmPrimitive;

public class CustomOsmPrimitive {

    private final OsmPrimitive osmPrimitive;
    private boolean incomplete;

    public CustomOsmPrimitive(OsmPrimitive osmPrimitive) {
        this.osmPrimitive = osmPrimitive;
        this.incomplete = false;
    }

    // Getter and setter for incomplete status
    public boolean isIncomplete() {
        return incomplete;
    }

    public void setIncomplete(boolean incomplete) {
        this.incomplete = incomplete;
    }

    // Forward other method calls to the encapsulated OsmPrimitive object
    public long getId() {
        return osmPrimitive.getUniqueId();
    }

    // You can forward other necessary methods similarly
}
