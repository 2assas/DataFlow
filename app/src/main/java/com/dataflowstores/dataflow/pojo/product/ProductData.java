package com.dataflowstores.dataflow.pojo.product;
import android.os.Parcel;
import android.os.Parcelable;

import com.dataflowstores.dataflow.pojo.settings.PriceTypeData;
import com.dataflowstores.dataflow.pojo.settings.StoresData;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductData implements Serializable, Parcelable {
    String selectedExpireDate;
    String selectedSerial;
    ColorsList selectedColor;
    Group1List selectedGroup1;
    Group2List selectedGroup2;
    MeasureUnit selectedUnit;
    SeasonsList selectedSeason;
    SizesList selectedSize;
    StoresData selectedStore;
    StoresData selectedToStore;
    PriceTypeData selectedPriceType;
    float quantity;
    float actualQuantity;
    float bonusQuantity=0;
    double priceItem;
    double priceTotal;
    String userNote;
    MeasureUnit basicMeasureUnit;
    double totalTax;
    double discount1=0;
    double netPrice;
    int allowStoreMinus;

    @SerializedName("ItemISNBranch")
    private int itemISNBranch;
    @SerializedName("BranchISN")
    private int branchISN;
    @SerializedName("Item_ISN")
    private int itemISN;
    @SerializedName("ItemName")
    private String itemName;
    @SerializedName("ExpireDate")
    private Boolean expireDate;
    @SerializedName("Colors")
    private Boolean colors;
    @SerializedName("colors_list")
    private List<ColorsList> colorsList;
    @SerializedName("Seasons")
    private Boolean seasons;
    @SerializedName("Seaons_list")
    private List<SeasonsList> seasonsLists;
    @SerializedName("Sizes")
    private Boolean sizes;
    @SerializedName("Sizes_list")
    private List<SizesList> sizesList;
    @SerializedName("Serial")
    private Boolean serial;
    @SerializedName("Group1")
    private Boolean group1;
    @SerializedName("Group1_list")
    private List<Group1List> group1List = new ArrayList<>();
    @SerializedName("Group2")
    private Boolean group2;
    @SerializedName("Group2_list")
    private List<Group2List> group2List= new ArrayList<>();
    @SerializedName("ServiceItem")
    private Boolean serviceItem;
    @SerializedName("StopDeal")
    private Boolean stopDeal;
    @SerializedName("ItemNotes")
    private String itemNotes;
    @SerializedName("MeasureUnits")
    private List<MeasureUnit> measureUnits= new ArrayList<>();
    @SerializedName("ItemTax")
    private String itemTax;

    @SerializedName("xPriceFromBarcode")
    private double xPriceFromBarcode;

    @SerializedName("xBarCodeColorISN")
    private int xBarCodeColorISN;

    @SerializedName("xBarCodeGroup2BranchISN")
    private int xBarCodeGroup2BranchISN;
    @SerializedName("xQuanFromBarcode")
    private String xQuanFromBarcode;

    @SerializedName("xBarCodeGroup2ISN")
    private int xBarCodeGroup2ISN;

    @SerializedName("xBarCodeMeasureUnitBranchISN")
    private int xBarCodeMeasureUnitBranchISN;

    @SerializedName("xBarCodeGroup1BranchISN")
    private int xBarCodeGroup1BranchISN;

    @SerializedName("xBarCodeMeasureUnitName")
    private String xBarCodeMeasureUnitName;

    @SerializedName("xBarCodeColorBranchISN")
    private int xBarCodeColorBranchISN;

    @SerializedName("xBarCodeSizeISN")
    private int xBarCodeSizeISN;

    @SerializedName("xBarCodeSerial")
    private String xBarCodeSerial;

    @SerializedName("xBarCodeColorName")
    private String xBarCodeColorName;

    @SerializedName("xBarCodeMeasureUnitISN")
    private int xBarCodeMeasureUnitISN;

    @SerializedName("xBarCodeSizeName")
    private String xBarCodeSizeName;

    @SerializedName("xBarCodeGroup1ISN")
    private int xBarCodeGroup1ISN;

    @SerializedName("xBarCodeSeasonISN")
    private int xBarCodeSeasonISN;

    @SerializedName("xItemCode")
    private String xItemCode;

    @SerializedName("xBarCodeGroup1Name")
    private String xBarCodeGroup1Name;


    @SerializedName("xBarCodeSizeBranchISN")
    private int xBarCodeSizeBranchISN;

    @SerializedName("xBarCodeSeasonBranchISN")
    private int xBarCodeSeasonBranchISN;

    @SerializedName("xBarCodeSeasonName")
    private String xBarCodeSeasonName;


    @SerializedName("xBarCodeExpireDate")
    private String xBarCodeExpireDate;

    @SerializedName("xBarCodeGroup2Name")
    private String xBarCodeGroup2Name;

    @SerializedName("QuanSumAddStartIndex")
    private String QuanSumAddStartIndex;
    @SerializedName("QuanSumAddLenght")
    private String QuanSumAddLength;
    @SerializedName("ShowQuanSumAdd")
    private String ShowQuanSumAdd;
    @SerializedName("QuanSumAddDevideOn")
    private String QuanSumAddDevideOn;

    private double illustrativeQuantity = 0;

    private boolean BarCodePrice = false;

    @SerializedName("QuanSumAddWriteToNotes")
    Integer QuanSumAddWriteToNotes;

    public Integer getQuanSumAddWriteToNotes() {
        return QuanSumAddWriteToNotes;
    }


    public String getQuanSumAddDevideOn() {
        return QuanSumAddDevideOn;
    }

    public void setQuanSumAddDevideOn(String quanSumAddDevideOn) {
        QuanSumAddDevideOn = quanSumAddDevideOn;
    }

    public void setIllustrativeQuantity(double illustrativeQuantity) {
        this.illustrativeQuantity = illustrativeQuantity;
    }

    public double getIllustrativeQuantity() {
        return illustrativeQuantity;
    }

    public String getQuanSumAddStartIndex() {
        return QuanSumAddStartIndex;
    }

    public String getQuanSumAddLength() {
        return QuanSumAddLength;
    }

    public String getShowQuanSumAdd() {
        return ShowQuanSumAdd;
    }

    public int getAllowStoreMinus() {
        return allowStoreMinus;
    }

    public void setAllowStoreMinus(int allowStoreMinus) {
        this.allowStoreMinus = allowStoreMinus;
    }

    public boolean isBarCodePrice() {
        return BarCodePrice;
    }

    public void setBarCodePrice(boolean barCodePrice) {
        BarCodePrice = barCodePrice;
    }

    public void setxPriceFromBarcode(double xPriceFromBarcode) {
        this.xPriceFromBarcode = xPriceFromBarcode;
    }

    public double getxPriceFromBarcode() {
        return xPriceFromBarcode;
    }



    public int getxBarCodeColorISN() {
        return xBarCodeColorISN;
    }

    public void setxBarCodeColorISN(int xBarCodeColorISN) {
        this.xBarCodeColorISN = xBarCodeColorISN;
    }

    public int getxBarCodeGroup2BranchISN() {
        return xBarCodeGroup2BranchISN;
    }

    public void setxBarCodeGroup2BranchISN(int xBarCodeGroup2BranchISN) {
        this.xBarCodeGroup2BranchISN = xBarCodeGroup2BranchISN;
    }

    public String getxQuanFromBarcode() {
        return xQuanFromBarcode;
    }

    public void setxQuanFromBarcode(String xQuanFromBarcode) {
        this.xQuanFromBarcode = xQuanFromBarcode;
    }

    public int getxBarCodeGroup2ISN() {
        return xBarCodeGroup2ISN;
    }

    public void setxBarCodeGroup2ISN(int xBarCodeGroup2ISN) {
        this.xBarCodeGroup2ISN = xBarCodeGroup2ISN;
    }

    public int getxBarCodeMeasureUnitBranchISN() {
        return xBarCodeMeasureUnitBranchISN;
    }

    public void setxBarCodeMeasureUnitBranchISN(int xBarCodeMeasureUnitBranchISN) {
        this.xBarCodeMeasureUnitBranchISN = xBarCodeMeasureUnitBranchISN;
    }

    public int getxBarCodeGroup1BranchISN() {
        return xBarCodeGroup1BranchISN;
    }

    public void setxBarCodeGroup1BranchISN(int xBarCodeGroup1BranchISN) {
        this.xBarCodeGroup1BranchISN = xBarCodeGroup1BranchISN;
    }

    public String getxBarCodeMeasureUnitName() {
        return xBarCodeMeasureUnitName;
    }

    public void setxBarCodeMeasureUnitName(String xBarCodeMeasureUnitName) {
        this.xBarCodeMeasureUnitName = xBarCodeMeasureUnitName;
    }

    public int getxBarCodeColorBranchISN() {
        return xBarCodeColorBranchISN;
    }

    public void setxBarCodeColorBranchISN(int xBarCodeColorBranchISN) {
        this.xBarCodeColorBranchISN = xBarCodeColorBranchISN;
    }

    public int getxBarCodeSizeISN() {
        return xBarCodeSizeISN;
    }

    public void setxBarCodeSizeISN(int xBarCodeSizeISN) {
        this.xBarCodeSizeISN = xBarCodeSizeISN;
    }

    public String getxBarCodeSerial() {
        return xBarCodeSerial;
    }

    public void setxBarCodeSerial(String xBarCodeSerial) {
        this.xBarCodeSerial = xBarCodeSerial;
    }

    public String getxBarCodeColorName() {
        return xBarCodeColorName;
    }

    public void setxBarCodeColorName(String xBarCodeColorName) {
        this.xBarCodeColorName = xBarCodeColorName;
    }

    public int getxBarCodeMeasureUnitISN() {
        return xBarCodeMeasureUnitISN;
    }

    public void setxBarCodeMeasureUnitISN(int xBarCodeMeasureUnitISN) {
        this.xBarCodeMeasureUnitISN = xBarCodeMeasureUnitISN;
    }

    public String getxBarCodeSizeName() {
        return xBarCodeSizeName;
    }

    public void setxBarCodeSizeName(String xBarCodeSizeName) {
        this.xBarCodeSizeName = xBarCodeSizeName;
    }

    public int getxBarCodeGroup1ISN() {
        return xBarCodeGroup1ISN;
    }

    public void setxBarCodeGroup1ISN(int xBarCodeGroup1ISN) {
        this.xBarCodeGroup1ISN = xBarCodeGroup1ISN;
    }

    public int getxBarCodeSeasonISN() {
        return xBarCodeSeasonISN;
    }

    public void setxBarCodeSeasonISN(int xBarCodeSeasonISN) {
        this.xBarCodeSeasonISN = xBarCodeSeasonISN;
    }

    public String getxItemCode() {
        return xItemCode;
    }

    public void setxItemCode(String xItemCode) {
        this.xItemCode = xItemCode;
    }

    public String getxBarCodeGroup1Name() {
        return xBarCodeGroup1Name;
    }

    public void setxBarCodeGroup1Name(String xBarCodeGroup1Name) {
        this.xBarCodeGroup1Name = xBarCodeGroup1Name;
    }

    public int getxBarCodeSizeBranchISN() {
        return xBarCodeSizeBranchISN;
    }

    public void setxBarCodeSizeBranchISN(int xBarCodeSizeBranchISN) {
        this.xBarCodeSizeBranchISN = xBarCodeSizeBranchISN;
    }

    public int getxBarCodeSeasonBranchISN() {
        return xBarCodeSeasonBranchISN;
    }

    public void setxBarCodeSeasonBranchISN(int xBarCodeSeasonBranchISN) {
        this.xBarCodeSeasonBranchISN = xBarCodeSeasonBranchISN;
    }

    public String getxBarCodeSeasonName() {
        return xBarCodeSeasonName;
    }

    public void setxBarCodeSeasonName(String xBarCodeSeasonName) {
        this.xBarCodeSeasonName = xBarCodeSeasonName;
    }

    public String getxBarCodeExpireDate() {
        return xBarCodeExpireDate;
    }

    public void setxBarCodeExpireDate(String xBarCodeExpireDate) {
        this.xBarCodeExpireDate = xBarCodeExpireDate;
    }

    public String getxBarCodeGroup2Name() {
        return xBarCodeGroup2Name;
    }

    public void setxBarCodeGroup2Name(String xBarCodeGroup2Name) {
        this.xBarCodeGroup2Name = xBarCodeGroup2Name;
    }

    public StoresData getSelectedToStore() {
        return selectedToStore;
    }

    public void setSelectedToStore(StoresData selectedToStore) {
        this.selectedToStore = selectedToStore;
    }

    public double getDiscount1() {
        return discount1;
    }

    public void setDiscount1(double discount1) {
        this.discount1 = discount1;
    }

    public String getItemTax() { return itemTax; }

    public void setItemTax(String itemTax) { this.itemTax = itemTax; }

    public MeasureUnit getBasicMeasureUnit() {
        return basicMeasureUnit;
    }

    public void setBasicMeasureUnit(MeasureUnit basicMeasureUnit) { this.basicMeasureUnit = basicMeasureUnit; }

    public long getItemISNBranch() {
        return itemISNBranch;
    }

    public void setItemISNBranch(int itemISNBranch) {
        this.itemISNBranch = itemISNBranch;
    }

    public int getBranchISN() {
        return branchISN;
    }

    public void setBranchISN(int branchISN) {
        this.branchISN = branchISN;
    }

    public int getItemISN() {
        return itemISN;
    }

    public void setItemISN(int itemISN) {
        this.itemISN = itemISN;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Boolean getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Boolean expireDate) {
        this.expireDate = expireDate;
    }

    public Boolean getColors() {
        return colors;
    }

    public void setColors(Boolean colors) {
        this.colors = colors;
    }

    public List<ColorsList> getColorsList() {
        return colorsList;
    }

    public void setColorsList(List<ColorsList> colorsList) {
        this.colorsList = colorsList;
    }

    public Boolean getSeasons() {
        return seasons;
    }

    public void setSeasons(Boolean seasons) {
        this.seasons = seasons;
    }

    public List<SeasonsList> getSeasonsLists() {
        return seasonsLists;
    }

    public void setSeasonsLists(List<SeasonsList> seasonsLists) { this.seasonsLists = seasonsLists; }

    public double getPriceItem() {
        return priceItem;
    }

    public void setPriceItem(double priceItem) {
        this.priceItem = priceItem;
    }

    public double getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(double priceTotal) {
        this.priceTotal = priceTotal;
    }

    public Boolean getSizes() {
        return sizes;
    }

    public void setSizes(Boolean sizes) {
        this.sizes = sizes;
    }

    public List<SizesList> getSizesList() {
        return sizesList;
    }

    public void setSizesList(List<SizesList> sizesList) {
        this.sizesList = sizesList;
    }

    public Boolean getSerial() {
        return serial;
    }

    public void setSerial(Boolean serial) {
        this.serial = serial;
    }

    public Boolean getGroup1() {
        return group1;
    }

    public void setGroup1(Boolean group1) {
        this.group1 = group1;
    }

    public List<Group1List> getGroup1List() {
        return group1List;
    }

    public void setGroup1List(List<Group1List> group1List) {
        this.group1List = group1List;
    }

    public Boolean getGroup2() {
        return group2;
    }

    public void setGroup2(Boolean group2) {
        this.group2 = group2;
    }

    public List<Group2List> getGroup2List() {
        return group2List;
    }

    public void setGroup2List(List<Group2List> group2List) {
        this.group2List = group2List;
    }

    public Boolean getServiceItem() {
        return serviceItem;
    }

    public void setServiceItem(Boolean serviceItem) {
        this.serviceItem = serviceItem;
    }

    public Boolean getStopDeal() {
        return stopDeal;
    }

    public void setStopDeal(Boolean stopDeal) {
        this.stopDeal = stopDeal;
    }

    public String getItemNotes() {
        return itemNotes;
    }

    public String getUserNote() {
        return userNote;
    }

    public void setUserNote(String userNote) {
        this.userNote = userNote;
    }

    public void setItemNotes(String itemNotes) {
        this.itemNotes = itemNotes;
    }

    public List<MeasureUnit> getMeasureUnits() {
        return measureUnits;
    }

    public void setMeasureUnits(List<MeasureUnit> measureUnits) {
        this.measureUnits = measureUnits;
    }

    public String getSelectedExpireDate() {
        return selectedExpireDate;
    }

    public void setSelectedExpireDate(String selectedExpireDate) {
        this.selectedExpireDate = selectedExpireDate;
    }

    public String getSelectedSerial() {
        return selectedSerial;
    }

    public void setSelectedSerial(String selectedSerial) {
        this.selectedSerial = selectedSerial;
    }

    public ColorsList getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(ColorsList selectedColor) {
        this.selectedColor = selectedColor;
    }

    public Group1List getSelectedGroup1() {
        return selectedGroup1;
    }

    public void setSelectedGroup1(Group1List selectedGroup1) {
        this.selectedGroup1 = selectedGroup1;
    }

    public Group2List getSelectedGroup2() {
        return selectedGroup2;
    }

    public void setSelectedGroup2(Group2List selectedGroup2) {
        this.selectedGroup2 = selectedGroup2;
    }

    public MeasureUnit getSelectedUnit() {
        return selectedUnit;
    }

    public void setSelectedUnit(MeasureUnit selectedUnit) {
        this.selectedUnit = selectedUnit;
    }

    public SeasonsList getSelectedSeason() {
        return selectedSeason;
    }

    public void setSelectedSeason(SeasonsList selectedSeason) {
        this.selectedSeason = selectedSeason;
    }

    public SizesList getSelectedSize() {
        return selectedSize;
    }

    public void setSelectedSize(SizesList selectedSize) {
        this.selectedSize = selectedSize;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public StoresData getSelectedStore() {
        return selectedStore;
    }

    public void setSelectedStore(StoresData selectedStore) {
        this.selectedStore = selectedStore;
    }

    public PriceTypeData getSelectedPriceType() {
        return selectedPriceType;
    }

    public void setSelectedPriceType(PriceTypeData selectedPriceType) { this.selectedPriceType = selectedPriceType; }

    public float getActualQuantity() {
        return actualQuantity;
    }

    public void setActualQuantity(float actualQuantity) {
        this.actualQuantity = actualQuantity;
    }

    public float getBonusQuantity() {
        return bonusQuantity;
    }

    public void setBonusQuantity(float bonusQuantity) {
        this.bonusQuantity = bonusQuantity;
    }

    public double getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(double totalTax) {
        this.totalTax = totalTax;
    }

    public double getNetPrice() {
        return netPrice;
    }

    public void setNetPrice(double netPrice) {
        this.netPrice = netPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.selectedExpireDate);
        dest.writeString(this.selectedSerial);
        dest.writeSerializable(this.selectedColor);
        dest.writeSerializable(this.selectedGroup1);
        dest.writeSerializable(this.selectedGroup2);
        dest.writeSerializable(this.selectedUnit);
        dest.writeSerializable(this.selectedSeason);
        dest.writeSerializable(this.selectedSize);
        dest.writeSerializable(this.selectedStore);
        dest.writeSerializable(this.selectedToStore);
        dest.writeSerializable(this.selectedPriceType);
        dest.writeFloat(this.quantity);
        dest.writeFloat(this.actualQuantity);
        dest.writeFloat(this.bonusQuantity);
        dest.writeDouble(this.priceItem);
        dest.writeDouble(this.priceTotal);
        dest.writeString(this.userNote);
        dest.writeSerializable(this.basicMeasureUnit);
        dest.writeDouble(this.totalTax);
        dest.writeDouble(this.discount1);
        dest.writeDouble(this.netPrice);
        dest.writeInt(this.allowStoreMinus);
        dest.writeInt(this.itemISNBranch);
        dest.writeInt(this.branchISN);
        dest.writeInt(this.itemISN);
        dest.writeString(this.itemName);
        dest.writeValue(this.expireDate);
        dest.writeValue(this.colors);
        dest.writeList(this.colorsList);
        dest.writeValue(this.seasons);
        dest.writeList(this.seasonsLists);
        dest.writeValue(this.sizes);
        dest.writeList(this.sizesList);
        dest.writeValue(this.serial);
        dest.writeValue(this.group1);
        dest.writeList(this.group1List);
        dest.writeValue(this.group2);
        dest.writeList(this.group2List);
        dest.writeValue(this.serviceItem);
        dest.writeValue(this.stopDeal);
        dest.writeString(this.itemNotes);
        dest.writeList(this.measureUnits);
        dest.writeString(this.itemTax);
        dest.writeDouble(this.xPriceFromBarcode);
        dest.writeInt(this.xBarCodeColorISN);
        dest.writeInt(this.xBarCodeGroup2BranchISN);
        dest.writeString(this.xQuanFromBarcode);
        dest.writeInt(this.xBarCodeGroup2ISN);
        dest.writeInt(this.xBarCodeMeasureUnitBranchISN);
        dest.writeInt(this.xBarCodeGroup1BranchISN);
        dest.writeString(this.xBarCodeMeasureUnitName);
        dest.writeInt(this.xBarCodeColorBranchISN);
        dest.writeInt(this.xBarCodeSizeISN);
        dest.writeString(this.xBarCodeSerial);
        dest.writeString(this.xBarCodeColorName);
        dest.writeInt(this.xBarCodeMeasureUnitISN);
        dest.writeString(this.xBarCodeSizeName);
        dest.writeInt(this.xBarCodeGroup1ISN);
        dest.writeInt(this.xBarCodeSeasonISN);
        dest.writeString(this.xItemCode);
        dest.writeString(this.xBarCodeGroup1Name);
        dest.writeInt(this.xBarCodeSizeBranchISN);
        dest.writeInt(this.xBarCodeSeasonBranchISN);
        dest.writeString(this.xBarCodeSeasonName);
        dest.writeString(this.xBarCodeExpireDate);
        dest.writeString(this.xBarCodeGroup2Name);
        dest.writeByte(this.BarCodePrice ? (byte) 1 : (byte) 0);
    }

    public void readFromParcel(Parcel source) {
        this.selectedExpireDate = source.readString();
        this.selectedSerial = source.readString();
        this.selectedColor = (ColorsList) source.readSerializable();
        this.selectedGroup1 = (Group1List) source.readSerializable();
        this.selectedGroup2 = (Group2List) source.readSerializable();
        this.selectedUnit = (MeasureUnit) source.readSerializable();
        this.selectedSeason = (SeasonsList) source.readSerializable();
        this.selectedSize = (SizesList) source.readSerializable();
        this.selectedStore = (StoresData) source.readSerializable();
        this.selectedToStore = (StoresData) source.readSerializable();
        this.selectedPriceType = (PriceTypeData) source.readSerializable();
        this.quantity = source.readFloat();
        this.actualQuantity = source.readFloat();
        this.bonusQuantity = source.readFloat();
        this.priceItem = source.readDouble();
        this.priceTotal = source.readDouble();
        this.userNote = source.readString();
        this.basicMeasureUnit = (MeasureUnit) source.readSerializable();
        this.totalTax = source.readDouble();
        this.discount1 = source.readDouble();
        this.netPrice = source.readDouble();
        this.allowStoreMinus = source.readInt();
        this.itemISNBranch = source.readInt();
        this.branchISN = source.readInt();
        this.itemISN = source.readInt();
        this.itemName = source.readString();
        this.expireDate = (Boolean) source.readValue(Boolean.class.getClassLoader());
        this.colors = (Boolean) source.readValue(Boolean.class.getClassLoader());
        this.colorsList = new ArrayList<ColorsList>();
        source.readList(this.colorsList, ColorsList.class.getClassLoader());
        this.seasons = (Boolean) source.readValue(Boolean.class.getClassLoader());
        this.seasonsLists = new ArrayList<SeasonsList>();
        source.readList(this.seasonsLists, SeasonsList.class.getClassLoader());
        this.sizes = (Boolean) source.readValue(Boolean.class.getClassLoader());
        this.sizesList = new ArrayList<SizesList>();
        source.readList(this.sizesList, SizesList.class.getClassLoader());
        this.serial = (Boolean) source.readValue(Boolean.class.getClassLoader());
        this.group1 = (Boolean) source.readValue(Boolean.class.getClassLoader());
        this.group1List = new ArrayList<Group1List>();
        source.readList(this.group1List, Group1List.class.getClassLoader());
        this.group2 = (Boolean) source.readValue(Boolean.class.getClassLoader());
        this.group2List = new ArrayList<Group2List>();
        source.readList(this.group2List, Group2List.class.getClassLoader());
        this.serviceItem = (Boolean) source.readValue(Boolean.class.getClassLoader());
        this.stopDeal = (Boolean) source.readValue(Boolean.class.getClassLoader());
        this.itemNotes = source.readString();
        this.measureUnits = new ArrayList<MeasureUnit>();
        source.readList(this.measureUnits, MeasureUnit.class.getClassLoader());
        this.itemTax = source.readString();
        this.xPriceFromBarcode = source.readDouble();
        this.xBarCodeColorISN = source.readInt();
        this.xBarCodeGroup2BranchISN = source.readInt();
        this.xQuanFromBarcode = source.readString();
        this.xBarCodeGroup2ISN = source.readInt();
        this.xBarCodeMeasureUnitBranchISN = source.readInt();
        this.xBarCodeGroup1BranchISN = source.readInt();
        this.xBarCodeMeasureUnitName = source.readString();
        this.xBarCodeColorBranchISN = source.readInt();
        this.xBarCodeSizeISN = source.readInt();
        this.xBarCodeSerial = source.readString();
        this.xBarCodeColorName = source.readString();
        this.xBarCodeMeasureUnitISN = source.readInt();
        this.xBarCodeSizeName = source.readString();
        this.xBarCodeGroup1ISN = source.readInt();
        this.xBarCodeSeasonISN = source.readInt();
        this.xItemCode = source.readString();
        this.xBarCodeGroup1Name = source.readString();
        this.xBarCodeSizeBranchISN = source.readInt();
        this.xBarCodeSeasonBranchISN = source.readInt();
        this.xBarCodeSeasonName = source.readString();
        this.xBarCodeExpireDate = source.readString();
        this.xBarCodeGroup2Name = source.readString();
        this.BarCodePrice = source.readByte() != 0;
    }

    public ProductData() {
    }

    protected ProductData(Parcel in) {
        this.selectedExpireDate = in.readString();
        this.selectedSerial = in.readString();
        this.selectedColor = (ColorsList) in.readSerializable();
        this.selectedGroup1 = (Group1List) in.readSerializable();
        this.selectedGroup2 = (Group2List) in.readSerializable();
        this.selectedUnit = (MeasureUnit) in.readSerializable();
        this.selectedSeason = (SeasonsList) in.readSerializable();
        this.selectedSize = (SizesList) in.readSerializable();
        this.selectedStore = (StoresData) in.readSerializable();
        this.selectedToStore = (StoresData) in.readSerializable();
        this.selectedPriceType = (PriceTypeData) in.readSerializable();
        this.quantity = in.readFloat();
        this.actualQuantity = in.readFloat();
        this.bonusQuantity = in.readFloat();
        this.priceItem = in.readDouble();
        this.priceTotal = in.readDouble();
        this.userNote = in.readString();
        this.basicMeasureUnit = (MeasureUnit) in.readSerializable();
        this.totalTax = in.readDouble();
        this.discount1 = in.readDouble();
        this.netPrice = in.readDouble();
        this.allowStoreMinus = in.readInt();
        this.itemISNBranch = in.readInt();
        this.branchISN = in.readInt();
        this.itemISN = in.readInt();
        this.itemName = in.readString();
        this.expireDate = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.colors = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.colorsList = new ArrayList<ColorsList>();
        in.readList(this.colorsList, ColorsList.class.getClassLoader());
        this.seasons = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.seasonsLists = new ArrayList<SeasonsList>();
        in.readList(this.seasonsLists, SeasonsList.class.getClassLoader());
        this.sizes = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.sizesList = new ArrayList<SizesList>();
        in.readList(this.sizesList, SizesList.class.getClassLoader());
        this.serial = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.group1 = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.group1List = new ArrayList<Group1List>();
        in.readList(this.group1List, Group1List.class.getClassLoader());
        this.group2 = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.group2List = new ArrayList<Group2List>();
        in.readList(this.group2List, Group2List.class.getClassLoader());
        this.serviceItem = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.stopDeal = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.itemNotes = in.readString();
        this.measureUnits = new ArrayList<MeasureUnit>();
        in.readList(this.measureUnits, MeasureUnit.class.getClassLoader());
        this.itemTax = in.readString();
        this.xPriceFromBarcode = in.readDouble();
        this.xBarCodeColorISN = in.readInt();
        this.xBarCodeGroup2BranchISN = in.readInt();
        this.xQuanFromBarcode = in.readString();
        this.xBarCodeGroup2ISN = in.readInt();
        this.xBarCodeMeasureUnitBranchISN = in.readInt();
        this.xBarCodeGroup1BranchISN = in.readInt();
        this.xBarCodeMeasureUnitName = in.readString();
        this.xBarCodeColorBranchISN = in.readInt();
        this.xBarCodeSizeISN = in.readInt();
        this.xBarCodeSerial = in.readString();
        this.xBarCodeColorName = in.readString();
        this.xBarCodeMeasureUnitISN = in.readInt();
        this.xBarCodeSizeName = in.readString();
        this.xBarCodeGroup1ISN = in.readInt();
        this.xBarCodeSeasonISN = in.readInt();
        this.xItemCode = in.readString();
        this.xBarCodeGroup1Name = in.readString();
        this.xBarCodeSizeBranchISN = in.readInt();
        this.xBarCodeSeasonBranchISN = in.readInt();
        this.xBarCodeSeasonName = in.readString();
        this.xBarCodeExpireDate = in.readString();
        this.xBarCodeGroup2Name = in.readString();
        this.BarCodePrice = in.readByte() != 0;
    }

    public ProductData copy() {
        Parcel parcel = Parcel.obtain();
        writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        return CREATOR.createFromParcel(parcel);
    }

    public static final Parcelable.Creator<ProductData> CREATOR = new Parcelable.Creator<ProductData>() {
        @Override
        public ProductData createFromParcel(Parcel source) {
            return new ProductData(source);
        }

        @Override
        public ProductData[] newArray(int size) {
            return new ProductData[size];
        }
    };
}
