package org.openstreetmap.josm.plugins.ohmdatefilter;

import java.util.ArrayList;
import java.util.List;

public class FilterList {

    FilterItem existingStartDate = new FilterItem("start_date:", true, false);
    FilterItem existingEndDate = new FilterItem("end_date:", true, false);
    FilterItem startDate = new FilterItem("", false, false);
    FilterItem endDate = new FilterItem("", false, false);
    FilterItem onlyRelations = new FilterItem("type:relation OR child type:relation", true, false);
    FilterItem onlyRelationsStartDate = new FilterItem("", false, false);
    FilterItem onlyRelationsEndDate = new FilterItem("", false, false);
    FilterItem reset = new FilterItem("", true, false);

    public FilterItem getExistingStartDate() {
        return existingStartDate;
    }

    public void setExistingStartDate(FilterItem existingStartDate) {
        this.existingStartDate = existingStartDate;
    }

    public FilterItem getExistingEndDate() {
        return existingEndDate;
    }

    public void setExistingEndDate(FilterItem existingEndDate) {
        this.existingEndDate = existingEndDate;
    }

    public FilterItem getStartDate() {
        return startDate;
    }

    public void setStartDate(FilterItem startDate) {
        this.startDate = startDate;
    }

    public FilterItem getEndDate() {
        return endDate;
    }

    public void setEndDate(FilterItem endDate) {
        this.endDate = endDate;
    }

    public FilterItem getOnlyRelations() {
        return onlyRelations;
    }

    public void setOnlyRelations(FilterItem onlyRelations) {
        this.onlyRelations = onlyRelations;
    }

    public FilterItem getOnlyRelationsStartDate() {
        return onlyRelationsStartDate;
    }

    public void setOnlyRelationsStartDate(FilterItem onlyRelationsStartDate) {
        this.onlyRelationsStartDate = onlyRelationsStartDate;
    }

    public FilterItem getOnlyRelationsEndDate() {
        return onlyRelationsEndDate;
    }

    public void setOnlyRelationsEndDate(FilterItem onlyRelationsEndDate) {
        this.onlyRelationsEndDate = onlyRelationsEndDate;
    }

    public FilterItem getReset() {
        return reset;
    }

    public void setReset(FilterItem reset) {
        this.reset = reset;
    }

    public List<FilterItem> getFilters() {
        List<FilterItem> list = new ArrayList<>();
        list.add(existingStartDate);
        list.add(existingEndDate);
        list.add(startDate);
        list.add(endDate);
        list.add(onlyRelations);
        list.add(onlyRelationsStartDate);
        list.add(onlyRelationsEndDate);
//        list.add(reset);
        return list;
    }
       // toString method to print all filters
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        List<FilterItem> filters = getFilters();
        for (FilterItem filter : filters) {
            result.append(filter.toString()).append("\n");
        }
        return result.toString();
    }
}
