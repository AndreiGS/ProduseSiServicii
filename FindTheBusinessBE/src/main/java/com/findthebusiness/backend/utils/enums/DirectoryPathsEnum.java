package com.findthebusiness.backend.utils.enums;

public enum DirectoryPathsEnum {

    SITEMAP_LOCATION("/");

    public final String fileLocation;

    DirectoryPathsEnum(String fileLocation) {
        this.fileLocation = fileLocation;
    }

}
