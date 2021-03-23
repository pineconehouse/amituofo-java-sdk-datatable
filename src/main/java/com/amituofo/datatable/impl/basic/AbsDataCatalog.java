package com.amituofo.datatable.impl.basic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amituofo.datatable.DataCatalog;
import com.amituofo.datatable.DataException;
import com.amituofo.datatable.DataTable;

public abstract class AbsDataCatalog implements DataCatalog {

	protected String name = "";
	private final Map<String, DataTable> dataTables = new HashMap<String, DataTable>();

	public AbsDataCatalog(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int getTableCount() {
		return dataTables.size();
	}

	@Override
	public List<DataTable> getTables() {
		List<DataTable> tbs = new ArrayList<DataTable>();
		tbs.addAll(dataTables.values());
		return tbs;
	}

	@Override
	public DataTable getTable(int index) {
		List<DataTable> tbs = getTables();
		if (tbs.size() > index) {
			return getTables().get(index);
		} else {
			return null;
		}
	}

//	@Override
	public void setDataTable(String name, DataTable tableSet) throws DataException {
		if (dataTables.containsKey(name)) {
			throw new DataException(name + " duplicate table!");
		}
		dataTables.put(name.toUpperCase(), tableSet);
	}

	@Override
	public boolean isTableExists(String name) {
		return getTable(name.toUpperCase()) != null;
	}

	@Override
	public DataTable getTable(String name) {
		return dataTables.get(name.toUpperCase());
	}

	@Override
	public DataTable dropTable(String name) throws DataException {
		return dataTables.remove(name.toUpperCase());
	}

	// public void setDataTableDefinition(DataTableDefinition definition) throws DataException {
	// List<DataTable> list = this.getDataTables();
	// for (DataTable dt : list) {
	// dt.setDefinition(definition);
	// }
	// }
	//
	// public void setDataTableDefinition(String name, DataTableDefinition definition) throws DataException {
	// DataTable dt = this.getDataTable(name.toUpperCase());
	// if (dt != null) {
	// dt.setDefinition(definition);
	// }
	// }

	@Override
	public void close() {
		List<DataTable> list = this.getTables();
		for (DataTable dt : list) {
			dt.close();
		}
	}
}
