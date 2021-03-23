package com.amituofo.datatable.impl.basic;

import java.util.HashMap;
import java.util.Map;

import com.amituofo.datatable.DataField;

public abstract class AbsDataField implements DataField {
	// protected DataFieldDefinition definition = null;
	protected Object value = null;
	protected Map<String, Object> attributeMap = null;

	// protected List<String> errors = null;

	public AbsDataField() {
	}

	public AbsDataField(Object defaultValue) {
		this.value = defaultValue;
	}

	// public AbsDataField(DataFieldDefinition definition) {
	// // this.definition = definition;
	// this.value = definition.getType().getDefaultValue();
	// }

	@Override
	public Object getAttribute(String name) {
		if (attributeMap == null) {
			return null;
		}

		return attributeMap.get(name);
	}

	@Override
	public void setAttribute(String name, Object data) {
		if (attributeMap == null) {
			attributeMap = new HashMap<String, Object>();
		}

		this.attributeMap.put(name, data);
	}

	// public boolean isInvalid() {
	// return errors != null && errors.size() != 0;
	// }
	//
	// public List<String> getErrors() {
	// return errors;
	// }
	//
	// public void appendError(String message) {
	// if (errors == null) {
	// errors = new ArrayList<String>();
	// }
	//
	// errors.add(message);
	// }
	//
	// public void clearErrors() {
	// errors = null;
	// }

//	public void setValue(Object o) {
//		this.value = o;
//	}

	@Override
	public Object getValue() {
		return value;
	}

	// public String getName() {
	// return definition.getName();
	// }
	//
	// public DataFieldDefinition getDefinition() {
	// return definition;
	// }
	//
	// public void validate() {
	// definition.getType().validate(this);
	// }

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof DataField) {
			Object o = ((DataField) obj).getValue();
			if (o != null) {
				return o.equals(value);
			}
		}

		return false;
	}

	@Override
	public boolean equals(DataField obj) {
		if (obj != null) {
			Object o = obj.getValue();
			if (o != null && this.value != null) {
				return o.equals(this.value);
			} else if (o == null && this.value == null) {
				return true;
			}
		}

		return false;
	}

	@Override
	public String toString() {
		if (value != null) {
			return value.toString();
		} else {
			return "";
		}
	}

	// public String randerValue() {
	// return definition.rending(this);
	// }
}
