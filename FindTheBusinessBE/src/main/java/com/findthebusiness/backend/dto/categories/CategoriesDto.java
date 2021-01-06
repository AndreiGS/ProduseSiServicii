package com.findthebusiness.backend.dto.categories;

public class CategoriesDto {
	private String id;
	private String name;

	public CategoriesDto(){}

	public CategoriesDto(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return this.id;
	}

	public CategoriesDto setId(String id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return this.name;
	}

	public CategoriesDto setName(String name) {
		this.name = name;
		return this;
	}
}
