package com.google.glassware.model;

public class MonsterDefinition {
	public Long id;
	public String name;
	public Float difficulty;
	public Float weight;
	public String image;

	public MonsterDefinition(String name, Float difficulty, Float weight, String image) {
		this.name = name;
		this.difficulty = difficulty;
		this.weight = weight;
		this.image = image;
	}
}
