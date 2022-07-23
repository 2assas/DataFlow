package com.example.dataflow.pojo.product;
import com.example.dataflow.pojo.settings.PriceTypeData;
import com.example.dataflow.pojo.settings.StoresData;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductData implements Serializable {
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
}
