package com.example.projetmobdev.model;

import java.util.List;

public class SectionDataModel {



    private String headerTitle;
    private List<Movie> allItemsInSection;


    public SectionDataModel() {

    }
    public SectionDataModel(String headerTitle, List<Movie> allItemsInSection) {
        this.headerTitle = headerTitle;
        this.allItemsInSection = allItemsInSection;
    }



    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public List<Movie> getAllItemsInSection() {
        return allItemsInSection;
    }

    public void setAllItemsInSection(List<Movie> allItemsInSection) {
        this.allItemsInSection = allItemsInSection;
    }


}