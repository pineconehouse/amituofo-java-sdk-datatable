package com.amituofo.datatable.impl.basic;

public abstract class StdDefinition {
	protected String name;

	public StdDefinition(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// public abstract T clone();
}
