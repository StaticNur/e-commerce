package com.staticnur.models;

public class RegionsDTO {
    private String regions;

    public RegionsDTO() {
    }
    public RegionsDTO(String regions) {
        this.regions = regions;
    }

    public String getRegions() {
        return regions;
    }

    public void setRegions(String regions) {
        this.regions = regions;
    }

    @Override
    public String toString() {
        return "RegionsDTO{" +
               "regions='" + regions + '\'' +
               '}';
    }
}
