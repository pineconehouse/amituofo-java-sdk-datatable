package com.amituofo.datatable;

public interface Formula {
	// public Class<? extends Formula> getFormulaClass();

	// public void setFormulaClass(Class<? extends Formula> cls);

	// public void init();

	// public DataField update(List<DataField> data);
	public DataField update(DataField... data);
}
