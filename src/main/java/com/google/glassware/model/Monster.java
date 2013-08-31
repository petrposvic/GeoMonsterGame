package com.google.glassware.model;

public class Monster {
	public MonsterDefinition definition;
	public Double latitude, longitude;
	public Long duration;

	public String toString() {
		return definition.name + " on " + latitude + "x" + longitude;
	}
}
