package com.dataflowstores.dataflow.pojo.expenses;

import com.google.gson.annotations.SerializedName;

public class SubExpItem {

	@SerializedName("SubExpMenuISN")
	private String subExpMenuISN;

	@SerializedName("MainExpMenuBranchISN")
	private String mainExpMenuBranchISN;

	@SerializedName("MainExpMenuISN")
	private String mainExpMenuISN;

	@SerializedName("SubExpMenuBranchISN")
	private String subExpMenuBranchISN;

	@SerializedName("SubExpMenuName")
	private String subExpMenuName;

	boolean isEmpty=false;

	public SubExpItem(boolean isEmpty) {
		this.isEmpty = isEmpty;
	}

	public SubExpItem() {
	}

	public void setSubExpMenuISN(String subExpMenuISN){
		this.subExpMenuISN = subExpMenuISN;
	}

	public String getSubExpMenuISN(){
		return subExpMenuISN;
	}

	public void setMainExpMenuBranchISN(String mainExpMenuBranchISN){
		this.mainExpMenuBranchISN = mainExpMenuBranchISN;
	}

	public String getMainExpMenuBranchISN(){
		return mainExpMenuBranchISN;
	}

	public void setMainExpMenuISN(String mainExpMenuISN){
		this.mainExpMenuISN = mainExpMenuISN;
	}

	public String getMainExpMenuISN(){
		return mainExpMenuISN;
	}

	public void setSubExpMenuBranchISN(String subExpMenuBranchISN){
		this.subExpMenuBranchISN = subExpMenuBranchISN;
	}

	public String getSubExpMenuBranchISN(){
		return subExpMenuBranchISN;
	}

	public void setSubExpMenuName(String subExpMenuName){
		this.subExpMenuName = subExpMenuName;
	}

	public String getSubExpMenuName(){
		return subExpMenuName;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"subExpMenuISN = '" + subExpMenuISN + '\'' + 
			",mainExpMenuBranchISN = '" + mainExpMenuBranchISN + '\'' + 
			",mainExpMenuISN = '" + mainExpMenuISN + '\'' + 
			",subExpMenuBranchISN = '" + subExpMenuBranchISN + '\'' + 
			",subExpMenuName = '" + subExpMenuName + '\'' + 
			"}";
		}
}