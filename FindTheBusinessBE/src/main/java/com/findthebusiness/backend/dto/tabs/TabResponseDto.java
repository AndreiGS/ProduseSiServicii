package com.findthebusiness.backend.dto.tabs;

public class TabResponseDto implements Comparable<TabResponseDto> {

    private String id;
    private String name;

    public TabResponseDto() {
    }

    public TabResponseDto(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public TabResponseDto setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TabResponseDto setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public int compareTo(TabResponseDto o) {
        return this.name.compareTo(o.getName());
    }
}
