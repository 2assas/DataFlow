package com.dataflowstores.dataflow.pojo.expenses;

import com.google.gson.annotations.SerializedName;

public class MainExpItem {

	@SerializedName("MustChooseWorker")
	private String mustChooseWorker;

	@SerializedName("MainExpMenuBranchISN")
	private String mainExpMenuBranchISN;

	@SerializedName("MainExpMenuName")
	private String mainExpMenuName;

	@SerializedName("MainExpMenuISN")
	private String mainExpMenuISN;

	boolean isEmpty = false;

	public MainExpItem(boolean isEmpty) {
		this.isEmpty = isEmpty;
	}

	public MainExpItem() {
	}

	public boolean isEmpty() {
		return isEmpty;
	}

	public void setEmpty(boolean empty) {
		isEmpty = empty;
	}

	public void setMustChooseWorker(String mustChooseWorker){
		this.mustChooseWorker = mustChooseWorker;
	}

	public String getMustChooseWorker(){
		return mustChooseWorker;
	}

	public void setMainExpMenuBranchISN(String mainExpMenuBranchISN){
		this.mainExpMenuBranchISN = mainExpMenuBranchISN;
	}

	public String getMainExpMenuBranchISN(){
		return mainExpMenuBranchISN;
	}

	public void setMainExpMenuName(String mainExpMenuName){
		this.mainExpMenuName = mainExpMenuName;
	}

	public String getMainExpMenuName(){
		return mainExpMenuName;
	}

	public void setMainExpMenuISN(String mainExpMenuISN){
		this.mainExpMenuISN = mainExpMenuISN;
	}

	public String getMainExpMenuISN(){
		return mainExpMenuISN;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"mustChooseWorker = '" + mustChooseWorker + '\'' + 
			",mainExpMenuBranchISN = '" + mainExpMenuBranchISN + '\'' + 
			",mainExpMenuName = '" + mainExpMenuName + '\'' + 
			",mainExpMenuISN = '" + mainExpMenuISN + '\'' + 
			"}";
		}
}