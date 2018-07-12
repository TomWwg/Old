package com.sws.common.entity;


public class DepartInfo{
	private String departId;
	private String departName;
	public String getDepartId() {
		return departId;
	}
	public void setDepartId(String departId) {
		this.departId = departId;
	}
	public String getDepartName() {
		return departName;
	}
	public void setDepartName(String departName) {
		this.departName = departName;
	}
	@Override
	public String toString() {
		return "DepartInfo [departId=" + departId + ", departName="
				+ departName + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((departId == null) ? 0 : departId.hashCode());
		result = prime * result
				+ ((departName == null) ? 0 : departName.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DepartInfo other = (DepartInfo) obj;
		if (departId == null) {
			if (other.departId != null)
				return false;
		} else if (!departId.equals(other.departId))
			return false;
		if (departName == null) {
			if (other.departName != null)
				return false;
		} else if (!departName.equals(other.departName))
			return false;
		return true;
	}
	
	
	
}