package org.openstreetmap.josm.plugins.ohmdatefilter;

public class FilterItem {

    String text = "";
    Boolean inverted = false;
    Boolean active = false;

    public FilterItem(String filter, boolean inverted, boolean active) {
        this.text = filter;
        this.inverted = inverted;
        this.active = active;

    }

    public String getText() {
        return text;
    }

    public void setText(String filter) {
        this.text = filter;
    }

    public Boolean getInverted() {
        return inverted;
    }

    public void setInverted(Boolean inverted) {
        this.inverted = inverted;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "FilterItem{"
                + "text='" + text + '\''
                + ", inverted=" + inverted
                + ", active=" + active
                + '}';
    }
}
