package com.example.dataflow.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.dataflow.App;
import com.example.dataflow.R;
import com.example.dataflow.ViewModels.CheckoutVM;
import com.example.dataflow.ViewModels.ProductVM;
import com.example.dataflow.databinding.ProductScreenBinding;
import com.example.dataflow.pojo.product.MeasureUnit;
import com.example.dataflow.pojo.product.ProductData;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ProductDetails extends AppCompatActivity implements View.OnFocusChangeListener {
    ProductScreenBinding binding;
    MeasureUnit measureUnit = new MeasureUnit();
    Calendar myCalendar = Calendar.getInstance();
    boolean dateValidation = false;
    float quantity = 1;
    float bonus = 0;
    CheckoutVM checkoutVM;
    float totalQuantity = 1;
    ProductVM productVM;
    int allowStoreMinusConfirm = 0;
    String uuid;
    ProductData productDataOriginal;
    TextWatcher basicQuantity, itemDiscPer, itemDiscVal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            startActivity(new Intent(this, SplashScreen.class));
            finishAffinity();
        } else {
            binding = DataBindingUtil.setContentView(this, R.layout.product_screen);
            productVM = new ViewModelProvider(this).get(ProductVM.class);
            checkoutVM = new ViewModelProvider(this).get(CheckoutVM.class);
            fillViews();
            handleSpinners();
            unitSpinner();
            stores_priceTypeSpinner();
            quantityButtons();
            bonusButtons();
            addButton();
            quantityWatchers();
            discount();
        }
    }

    //    =====================================================================================================================
    @SuppressLint("SetTextI18n")
    public void fillViews() {
        productDataOriginal = App.product;
        binding.textView10.setText(App.product.getItemName());
        binding.orderNotes.setText(App.product.getItemNotes());
        binding.branchISN.setText(Integer.toString(App.product.getBranchISN()));
        uuid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        binding.item.setText(Integer.toString(App.product.getItemISN()));
        binding.itemBranchISN.setText(Long.toString(App.product.getItemISNBranch()));
        if (App.product.getQuantity() > 1) {
            quantity = App.product.getActualQuantity();
            if (App.product.getBonusQuantity() > 1) {
                bonus = App.product.getBonusQuantity();
            }
            binding.totalQuantity.setText(String.format(Locale.US, "%.3f", quantity + bonus) + "");
            binding.textView26.setText(String.format(Locale.US, "%.3f", App.product.getActualQuantity()) + "");
        }
        if (App.product.getDiscount1() != 0) {
            binding.itemDiscPer.setText(App.product.getDiscount1() + "");
        }
        if (App.product.getBonusQuantity() != 0) {
            binding.bounusQuantity.setText(App.product.getBonusQuantity() + "");
        }
        App.product.setQuantity(Float.parseFloat(String.format(Locale.US, "%.3f", quantity)));
        App.product.setActualQuantity(Float.parseFloat(String.format(Locale.US, "%.3f", quantity)));
        App.product.setBonusQuantity(Float.parseFloat(String.format(Locale.US, "%.3f", bonus)));
        Log.e("checkPrice", App.product.getPriceTotal() + "");
        binding.close.setOnClickListener(view -> {
            startActivity(new Intent(this, AddProducts.class));
            finish();
        });
        for (int i = 0; i < App.product.getMeasureUnits().size(); i++) {
            if (App.product.getMeasureUnits().get(i).getBasicMeasureUnit() == 1)
                App.product.setBasicMeasureUnit(App.product.getMeasureUnits().get(i));
        }
        if (App.currentUser.getMobileBonus() == 0) {
            binding.textView32.setVisibility(View.GONE);
            binding.minusBounus.setVisibility(View.GONE);
            binding.plusBounus.setVisibility(View.GONE);
            binding.bounusQuantity.setVisibility(View.GONE);
            binding.totalPriceTxt2.setVisibility(View.GONE);
            binding.totalQuantity.setVisibility(View.GONE);
        }
        if (App.currentUser.getMobileDiscount() == 0) {
            binding.serviceLayout.setVisibility(View.GONE);
        }
    }

    @SuppressLint("SetTextI18n")
    public void handleSpinners() {
        if (App.product.getColors()) {
            binding.colorSpinner.setVisibility(View.VISIBLE);
            binding.textView19.setVisibility(View.VISIBLE);
            ArrayList<String> colors = new ArrayList<>();
            for (int i = 0; i < App.product.getColorsList().size(); i++) {
                colors.add(App.product.getColorsList().get(i).getStoreColorName());
            }
            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, colors);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.colorSpinner.setAdapter(aa);
            binding.colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    App.product.setSelectedColor(App.product.getColorsList().get(i));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    App.product.setSelectedColor(App.product.getColorsList().get(0));
                }
            });
        }
        if (App.product.getSeasons()) {
            binding.seasonSpinner.setVisibility(View.VISIBLE);
            binding.textView20.setVisibility(View.VISIBLE);
            ArrayList<String> seasons = new ArrayList<>();
            for (int i = 0; i < App.product.getSeasonsLists().size(); i++) {
                seasons.add(App.product.getSeasonsLists().get(i).getStoreSeasonName());
            }
            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, seasons);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.seasonSpinner.setAdapter(aa);
            binding.seasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    App.product.setSelectedSeason(App.product.getSeasonsLists().get(i));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    App.product.setSelectedSeason(App.product.getSeasonsLists().get(0));
                }
            });
        }
        if (App.product.getSizes()) {
            binding.sizeSpinner.setVisibility(View.VISIBLE);
            binding.textView22.setVisibility(View.VISIBLE);
            ArrayList<String> sizes = new ArrayList<>();
            for (int i = 0; i < App.product.getSizesList().size(); i++) {
                sizes.add(App.product.getSizesList().get(i).getStoreSizeName());
            }
            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sizes);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.sizeSpinner.setAdapter(aa);

            binding.sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    App.product.setSelectedSize(App.product.getSizesList().get(i));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    App.product.setSelectedSize(App.product.getSizesList().get(0));
                }
            });
        }
        if (App.product.getGroup1()) {
            binding.group1Spinner.setVisibility(View.VISIBLE);
            binding.textView21.setVisibility(View.VISIBLE);

            ArrayList<String> group1 = new ArrayList<>();
            for (int i = 0; i < App.product.getGroup1List().size(); i++) {
                group1.add(App.product.getGroup1List().get(i).getStoreGroup1Name());
            }
            Log.e("checkSpinner", group1.size() + "");
            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, group1);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.group1Spinner.setAdapter(aa);
            binding.group1Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    App.product.setSelectedGroup1(App.product.getGroup1List().get(i));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    App.product.setSelectedGroup1(App.product.getGroup1List().get(0));
                }
            });
        }
        if (App.product.getGroup2()) {
            binding.group2Spinner.setVisibility(View.VISIBLE);
            binding.textView24.setVisibility(View.VISIBLE);

            ArrayList<String> group2 = new ArrayList<>();
            for (int i = 0; i < App.product.getGroup2List().size(); i++) {
                group2.add(App.product.getGroup2List().get(i).getStoreGroup2Name());
            }
            Log.e("checkSpinner", group2.size() + "");
            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, group2);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.group2Spinner.setAdapter(aa);
            binding.group2Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    App.product.setSelectedGroup2(App.product.getGroup2List().get(i));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    App.product.setSelectedGroup2(App.product.getGroup2List().get(0));
                }
            });
        }
        if (App.product.getExpireDate()) {
            binding.expirePicker.setVisibility(View.VISIBLE);
            binding.textView25.setVisibility(View.VISIBLE);
            datePicker();
        }
        if (App.product.getSerial()) {
            binding.serial.setVisibility(View.VISIBLE);
            binding.textView27.setVisibility(View.VISIBLE);
            binding.plusItem.setClickable(false);
            binding.plusBounus.setClickable(false);
            binding.minusBounus.setClickable(false);
            binding.minusItem.setClickable(false);
            binding.textView26.setEnabled(false);
            binding.bounusQuantity.setEnabled(false);
            binding.serial.setText(App.serialNumber);
            App.product.setSelectedSerial(App.serialNumber);
//          Log.e("Serial", "SS + "+binding.serial.getText().toString());
        }

    }

    public void discount() {
        binding.itemDiscVal.setOnFocusChangeListener(this);
        binding.itemDiscPer.setOnFocusChangeListener(this);
        itemDiscPer = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                double value = 0;
                if (!charSequence.toString().isEmpty()&&!charSequence.toString().equals(".")) {
                    if (Double.parseDouble(charSequence.toString()) <= 100) {
                        value = (measureUnit.getPrice() / 100) * Double.parseDouble(binding.itemDiscPer.getText().toString());
                        binding.itemDiscVal.setText(String.format(Locale.US, "%.2f", value * quantity) + "");
                        App.product.setNetPrice(((measureUnit.getPrice() - value) * quantity));
                        App.product.setDiscount1(Double.parseDouble(binding.itemDiscPer.getText().toString()));
                        binding.button.setClickable(true);
                        binding.price4.setText("قيمة: " + String.format(Locale.ENGLISH, "%.2f", ((App.product.getNetPrice() / 100) * Double.parseDouble(App.product.getItemTax()))) + "جنيه ");
                    } else {
                        binding.itemDiscPer.setError("ERROR!");
                        binding.itemDiscPer.setText("");
                        binding.itemDiscVal.setText("");
                        binding.button.setClickable(false);
                    }
                } else {
                    binding.price4.setText("قيمة: " + String.format(Locale.ENGLISH, "%.2f",
                            (App.product.getNetPrice() / 100 * Double.parseDouble(App.product.getItemTax()))) + "جنيه ");
                    binding.itemDiscVal.setText("");
                    App.product.setNetPrice(measureUnit.getPrice() * quantity);
                    App.product.setDiscount1(0);
                }

                binding.price2.setText(String.format(Locale.US, "%.2f", (measureUnit.getPrice() - value) * quantity) + " جنيه");

            }

            @Override

            public void afterTextChanged(Editable editable) {
            }
        };
        itemDiscVal = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                double value = 0;
                if (!charSequence.toString().isEmpty()&&!charSequence.toString().equals(".")) {
                    if (Double.parseDouble(charSequence.toString()) <= (measureUnit.getPrice() * quantity)) {
                        value = (Double.parseDouble(binding.itemDiscVal.getText().toString()) / (measureUnit.getPrice() * quantity));
                        double value1 = (measureUnit.getPrice() / 100) * value * 100 * quantity;
                        binding.itemDiscPer.setText(String.format(Locale.US, "%.2f", value * 100) + "");
                        App.product.setNetPrice((measureUnit.getPrice() * quantity) - Double.parseDouble(binding.itemDiscVal.getText().toString()));
                        App.product.setDiscount1(value * 100);
                        binding.button.setClickable(true);
                        binding.price2.setText(String.format(Locale.US, "%.2f", ((measureUnit.getPrice() * quantity) - Double.parseDouble(binding.itemDiscVal.getText().toString()))) + " جنيه");
                        binding.price4.setText("قيمة: " + String.format(Locale.ENGLISH, "%.2f", ((App.product.getNetPrice() / 100) * Double.parseDouble(App.product.getItemTax()))) + "جنيه ");

//                        binding.price4.setText( "قيمة: " +String.format(Locale.ENGLISH, "%.2f", (((measureUnit.getPrice()*quantity))-Double.parseDouble(binding.itemDiscVal.getText().toString())) * Double.parseDouble(App.product.getItemTax()) * quantity)+"جنيه ");
                    } else {
                        binding.itemDiscVal.setError("ERROR!");
                        binding.itemDiscPer.setText("");
                        binding.itemDiscVal.setText("");
                        binding.button.setClickable(false);
                    }
                } else {
                    binding.price2.setText(String.format(Locale.US, "%.2f", (measureUnit.getPrice() * quantity)) + " جنيه");
                    binding.price4.setText("قيمة: " + String.format(Locale.ENGLISH, "%.2f",
                            (App.product.getNetPrice() / 100 * Double.parseDouble(App.product.getItemTax()))) + "جنيه ");
                    binding.itemDiscPer.setText("");
                    App.product.setNetPrice(measureUnit.getPrice() * quantity);
                    App.product.setDiscount1(0);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (view.equals(binding.itemDiscPer)) {
            if (hasFocus) {
                binding.itemDiscPer.addTextChangedListener(itemDiscPer);
            } else {
                binding.itemDiscPer.removeTextChangedListener(itemDiscPer);
            }
        }
        if (view.equals(binding.itemDiscVal)) {
            if (hasFocus) {
                binding.itemDiscVal.addTextChangedListener(itemDiscVal);
            } else {
                binding.itemDiscVal.removeTextChangedListener(itemDiscVal);
            }
        }
    }

    public void unitSpinner() {
        ArrayList<String> measureUnits = new ArrayList<>();
        for (int i = 0; i < App.product.getMeasureUnits().size(); i++) {
            measureUnits.add(App.product.getMeasureUnits().get(i).getMeasureUnitArName() + " - " + App.product.getMeasureUnits().get(i).getPrice() + " جنيه");
        }
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, measureUnits);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.measureUnitSpinner.setAdapter(aa);
        binding.measureUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                measureUnit = App.product.getMeasureUnits().get(i);
                App.product.setNetPrice(measureUnit.getPrice());
                binding.price.setText(String.format(Locale.US, "%.3f", measureUnit.getPrice() * quantity) + " جنيه");
                if (binding.itemDiscPer.getText().toString().isEmpty()) {
                    binding.price2.setText(String.format(Locale.US, "%.3f", measureUnit.getPrice() * quantity) + " جنيه");
                    App.product.setNetPrice(measureUnit.getPrice() * quantity);
                } else {
                    binding.price2.setText(String.format(Locale.US, "%.3f", (measureUnit.getPrice() * quantity) - ((measureUnit.getPrice() * quantity) / 100) * Double.parseDouble(
                            binding.itemDiscPer.getText().toString()
                    )) + " جنيه");
                    App.product.setNetPrice((measureUnit.getPrice() * quantity) - ((measureUnit.getPrice() * quantity) / 100) * Double.parseDouble(
                            binding.itemDiscPer.getText().toString()));
                    binding.itemDiscVal.setText(String.format(Locale.US, "%.2f", ((measureUnit.getPrice() * quantity) / 100) * Double.parseDouble(
                            binding.itemDiscPer.getText().toString())) + "");
                }
                binding.price3.setText("نسبة: " + String.format(Locale.ENGLISH, "%.2f", Double.parseDouble(App.product.getItemTax())) + "% ");
                binding.price4.setText("قيمة: " + String.format(Locale.ENGLISH, "%.2f",
                        (App.product.getNetPrice() / 100 * Double.parseDouble(App.product.getItemTax()))) + "جنيه ");
                App.product.setSelectedUnit(App.product.getMeasureUnits().get(i));
                App.product.setPriceItem(measureUnit.getPrice());
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                measureUnit = App.product.getMeasureUnits().get(0);
                if (binding.itemDiscPer.getText().toString().isEmpty()) {
                    binding.price.setText(String.format(Locale.US, "%.2f", measureUnit.getPrice() * quantity) + " جنيه");
                    binding.price2.setText(String.format(Locale.US, "%.2f", measureUnit.getPrice() * quantity) + " جنيه");
                } else {
                    binding.price2.setText(String.format(Locale.US, "%.2f", (measureUnit.getPrice() * quantity) - ((measureUnit.getPrice() * quantity) / 100) * Double.parseDouble(
                            binding.itemDiscPer.getText().toString()
                    )) + " جنيه");
                    App.product.setNetPrice((measureUnit.getPrice() * quantity) - ((measureUnit.getPrice() * quantity) / 100) * Double.parseDouble(
                            binding.itemDiscPer.getText().toString()));
                    binding.itemDiscVal.setText(String.format(Locale.US, "%.2f", ((measureUnit.getPrice() * quantity) / 100) * Double.parseDouble(
                            binding.itemDiscPer.getText().toString())) + "");
                }
                binding.price3.setText("نسبة: " + String.format(Locale.ENGLISH, "%.2f", Double.parseDouble(App.product.getItemTax())) + "% ");
                binding.price4.setText("قيمة: " + String.format(Locale.ENGLISH, "%.2f", (App.product.getNetPrice() / 100 * Double.parseDouble(App.product.getItemTax()))) + "جنيه ");
                App.product.setSelectedUnit(App.product.getMeasureUnits().get(0));
                App.product.setPriceItem(measureUnit.getPrice());
            }
        });
        if (App.product.getSelectedUnit() != null) {
            for (int i = 0; i < App.product.getMeasureUnits().size(); i++) {
                if (App.product.getMeasureUnits().get(i) == App.product.getSelectedUnit())
                    binding.measureUnitSpinner.setSelection(i);
            }
        }
    }

    public void stores_priceTypeSpinner() {
        ArrayList<String> stores = new ArrayList<>();
        for (int i = 0; i < App.stores.getData().size(); i++) {
            stores.add(App.stores.getData().get(i).getStoreName());
        }
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, stores);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.storesList.setAdapter(aa);
        binding.storesList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                App.product.setSelectedStore(App.stores.getData().get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                App.product.setSelectedStore(App.stores.getData().get(0));
            }
        });
//      =========================
        if (App.product.getSelectedStore() != null) {
            for (int i = 0; i < App.stores.getData().size(); i++) {
                if (App.product.getSelectedStore() == App.stores.getData().get(i))
                    binding.storesList.setSelection(i);
            }
        }
        ArrayList<String> priceType = new ArrayList<>();
        priceType.add(App.priceType.getPricesTypeName());

        ArrayAdapter bb = new ArrayAdapter(this, android.R.layout.simple_spinner_item, priceType);
        bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.priceType.setAdapter(bb);
        binding.priceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                App.product.setSelectedPriceType(App.priceType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                App.product.setSelectedPriceType(App.priceType);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void quantityButtons() {
        binding.plusItem.setOnClickListener(view -> {
            quantity++;
            if (!App.product.getSerial()) {
                if (binding.itemDiscPer.getText().toString().isEmpty()) {
                    binding.price.setText(String.format(Locale.US, "%.3f", measureUnit.getPrice() * quantity) + " جنيه");
                    binding.price2.setText(String.format(Locale.US, "%.3f", measureUnit.getPrice() * quantity) + " جنيه");
                    App.product.setNetPrice(measureUnit.getPrice() * quantity);
                } else {
                    binding.price.setText(String.format(Locale.US, "%.3f", measureUnit.getPrice() * quantity) + " جنيه");
                    binding.price2.setText(String.format(Locale.US, "%.3f", (measureUnit.getPrice() * quantity) - ((measureUnit.getPrice() * quantity) / 100) * Double.parseDouble(
                            binding.itemDiscPer.getText().toString()
                    )) + " جنيه");
                    if (!binding.itemDiscPer.getText().toString().isEmpty())
                        App.product.setNetPrice((measureUnit.getPrice() * quantity) - ((measureUnit.getPrice() * quantity) / 100) * Double.parseDouble(
                                binding.itemDiscPer.getText().toString()));
                    else {
                        App.product.setNetPrice((measureUnit.getPrice() * quantity));
                    }
                    Log.e("checkNet", String.valueOf(App.product.getNetPrice()));
                    binding.itemDiscVal.setText(String.format(Locale.US, "%.2f", ((measureUnit.getPrice() * quantity) / 100) * Double.parseDouble(
                            binding.itemDiscPer.getText().toString())) + "");
                }

                App.product.setQuantity(Float.parseFloat(String.format(Locale.US, "%.3f", quantity)));
                binding.totalQuantity.setText(String.format(Locale.US, "%.3f", quantity + bonus) + "");
                binding.textView26.setText(String.format(Locale.US, "%.3f", quantity) + "");
                App.product.setPriceTotal(Double.parseDouble(String.format(Locale.US, "%.2f", measureUnit.getPrice() * quantity)));
                App.product.setActualQuantity(Float.parseFloat(String.format(Locale.US, "%.3f", quantity)));
                App.product.setBonusQuantity(Float.parseFloat(String.format(Locale.US, "%.3f", bonus)));
                binding.price3.setText("نسبة: " + String.format(Locale.ENGLISH, "%.2f", Double.parseDouble(App.product.getItemTax())) + "% ");
                binding.price4.setText("قيمة: " + String.format(Locale.ENGLISH, "%.2f",
                        (App.product.getNetPrice() / 100 * Double.parseDouble(App.product.getItemTax()))) + "جنيه ");
            }
        });
        binding.minusItem.setOnClickListener(view -> {
            if (!App.product.getSerial()) {
                if (quantity != 1) {
                    quantity--;
                }
                if (binding.itemDiscPer.getText().toString().isEmpty()) {
                    binding.price.setText(String.format(Locale.US, "%.3f", measureUnit.getPrice() * quantity) + " جنيه");
                    binding.price2.setText(String.format(Locale.US, "%.3f", measureUnit.getPrice() * quantity) + " جنيه");
                    App.product.setNetPrice(measureUnit.getPrice() * quantity);
                } else {
                    binding.price.setText(String.format(Locale.US, "%.3f", measureUnit.getPrice() * quantity) + " جنيه");
                    binding.price2.setText(String.format(Locale.US, "%.3f", (measureUnit.getPrice() * quantity) - ((measureUnit.getPrice() * quantity) / 100) * Double.parseDouble(
                            binding.itemDiscPer.getText().toString()
                    )) + " جنيه");
                    if (!binding.itemDiscPer.getText().toString().isEmpty())
                        App.product.setNetPrice((measureUnit.getPrice() * quantity) - ((measureUnit.getPrice() * quantity) / 100) * Double.parseDouble(
                                binding.itemDiscPer.getText().toString()));
                    else {
                        App.product.setNetPrice((measureUnit.getPrice() * quantity));
                    }
                    binding.itemDiscVal.setText(String.format(Locale.US, "%.2f", ((measureUnit.getPrice() * quantity) / 100) * Double.parseDouble(
                            binding.itemDiscPer.getText().toString())) + "");
                }
                binding.price3.setText("نسبة: " + String.format(Locale.ENGLISH, "%.2f", Double.parseDouble(App.product.getItemTax())) + "% ");
                binding.price4.setText("قيمة: " + String.format(Locale.ENGLISH, "%.2f",
                        (App.product.getNetPrice() / 100 * Double.parseDouble(App.product.getItemTax()))) + "جنيه ");
//            binding.textView26.setText(quantity+"");
                if (binding.textView26.getText().toString().isEmpty())
                    quantity = Float.parseFloat(binding.textView26.getText().toString());
                binding.textView26.setText(String.format(Locale.US, "%.3f", quantity) + "");
                App.product.setQuantity(Float.parseFloat(String.format(Locale.US, "%.3f", quantity)));
                binding.totalQuantity.setText(String.format(Locale.US, "%.3f", quantity + bonus) + "");
                App.product.setPriceTotal(Double.parseDouble(String.format(Locale.US, "%.2f", measureUnit.getPrice() * quantity)));
                App.product.setActualQuantity(Float.parseFloat(String.format(Locale.US, "%.3f", quantity)));
                App.product.setBonusQuantity(Float.parseFloat(String.format(Locale.US, "%.3f", bonus)));
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void bonusButtons() {
        binding.plusBounus.setOnClickListener(view -> {
            if (!App.product.getSerial()) {
                bonus++;
                binding.totalQuantity.setText(quantity + bonus + "");
                binding.bounusQuantity.setText(bonus + "");
                App.product.setQuantity(quantity + bonus);
                App.product.setBonusQuantity(bonus);
            }
        });
        binding.minusBounus.setOnClickListener(view -> {
            if (!App.product.getSerial()) {
                if (bonus > 0) {
                    bonus--;
                }
                if (binding.bounusQuantity.getText().toString().isEmpty())
                    bonus = Float.parseFloat(binding.textView26.getText().toString());
                binding.totalQuantity.setText(quantity + bonus + "");
                binding.bounusQuantity.setText(bonus + "");
                App.product.setQuantity(quantity + bonus);
                App.product.setBonusQuantity(bonus);
            }
        });
    }

    public void quantityWatchers() {
        binding.textView26.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                charSequence.toString().replaceAll(".","");
                if (!charSequence.toString().isEmpty()&&!charSequence.toString().equals(".")) {
                    try {
                        quantity = Float.parseFloat(charSequence.toString());
                    } catch (Exception e) {
                        System.out.println("Error " + e.getMessage());
                        new AlertDialog.Builder(ProductDetails.this).
                                setTitle("خطأ!")
                                .setMessage("لقد وصلت الى اقصى كميه ممكنه!")
                                .setPositiveButton("حسنا", (dialogInterface, f) -> dialogInterface.dismiss());
                    }
                    ;
                } else {
                    quantity = 1;
                }
                App.product.setQuantity(Float.parseFloat(String.format(Locale.US, "%.3f", quantity + bonus)));
                App.product.setBonusQuantity(Float.parseFloat(String.format(Locale.US, "%.3f", bonus)));
                App.product.setPriceTotal(Double.parseDouble(String.format(Locale.US, "%.2f", measureUnit.getPrice() * quantity)));
                App.product.setActualQuantity(Float.parseFloat(String.format(Locale.US, "%.3f", quantity)));

                if (binding.itemDiscPer.getText().toString().isEmpty()) {
                    binding.price.setText(String.format(Locale.US, "%.3f", measureUnit.getPrice() * quantity) + " جنيه");
                    binding.price2.setText(String.format(Locale.US, "%.3f", measureUnit.getPrice() * quantity) + " جنيه");
                    App.product.setNetPrice(measureUnit.getPrice() * quantity);
                } else {
                    binding.price.setText(String.format(Locale.US, "%.3f", measureUnit.getPrice() * quantity) + " جنيه");
                    binding.price2.setText(String.format(Locale.US, "%.3f", (measureUnit.getPrice() * quantity) - ((measureUnit.getPrice() * quantity) / 100) * Double.parseDouble(
                            binding.itemDiscPer.getText().toString()
                    )) + " جنيه");
                    App.product.setNetPrice((measureUnit.getPrice() * quantity) - ((measureUnit.getPrice() * quantity) / 100) * Double.parseDouble(
                            binding.itemDiscPer.getText().toString()));
                    binding.itemDiscVal.setText(String.format(Locale.US, "%.2f", ((measureUnit.getPrice() * quantity) / 100) * Double.parseDouble(
                            binding.itemDiscPer.getText().toString())) + "");
                }
                binding.price3.setText("نسبة: " + String.format(Locale.ENGLISH, "%.2f", Double.parseDouble(App.product.getItemTax())) + "% ");
                binding.price4.setText("قيمة: " + String.format(Locale.ENGLISH, "%.2f",
                        (App.product.getNetPrice() / 100 * Double.parseDouble(App.product.getItemTax()))) + "جنيه ");
                binding.totalQuantity.setText(String.format(Locale.US, "%.3f", quantity + bonus) + "");

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        binding.bounusQuantity.addTextChangedListener((new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()&&!charSequence.toString().equals(".")) {
//                   charSequence.toString().replaceAll(".","");
                    bonus = Float.parseFloat(charSequence.toString());
                    binding.totalQuantity.setText(quantity + bonus + "");
                } else {
                    bonus = 0;
                }
                binding.totalQuantity.setText(String.format(Locale.US, "%.3f", quantity + bonus) + "");
                App.product.setQuantity(Float.parseFloat(String.format(Locale.US, "%.3f", quantity + bonus)));
                App.product.setBonusQuantity(Float.parseFloat(String.format(Locale.US, "%.3f", bonus)));
                App.product.setPriceTotal(Double.parseDouble(String.format(Locale.US, "%.2f", measureUnit.getPrice() * quantity)));
                App.product.setActualQuantity(Float.parseFloat(String.format(Locale.US, "%.3f", quantity)));
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        }));
    }

    public void datePicker() {
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };
        binding.expirePicker.setOnClickListener(view -> {
            new DatePickerDialog(this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateValidation = true;
        App.product.setSelectedExpireDate(sdf.format(myCalendar.getTime()));
        binding.expirePicker.setText(sdf.format(myCalendar.getTime()));
    }

    private void addButton() {
        binding.button.setOnClickListener(view -> {
            App.product.setUserNote(binding.productNote.getText().toString());
            App.product.setPriceTotal(measureUnit.getPrice() * quantity);
            App.product.setTotalTax((App.product.getNetPrice() / 100 * Double.parseDouble(App.product.getItemTax())));
            if (App.product.getSerial() && !binding.serial.getText().toString().isEmpty()) {
                App.product.setSelectedSerial(binding.serial.getText().toString());
            }
            if (App.product.getExpireDate() && !dateValidation) {
                binding.expirePicker.setError(getString(R.string.date_error));
            } else if (App.product.getSerial() && binding.serial.getText().toString().isEmpty()) {
                binding.serial.setError(getString(R.string.serial_error));
            } else {
                if (App.currentUser.getAllowStoreMinus() == 3) {
                    if (!App.isEditing) {
                        App.selectedProducts.add(App.product);
                        binding.button.setClickable(false);
                        if (App.product.getSelectedUnit().getSpecialDiscFound() == 1) {
                            App.specialDiscount = 1;
                        }
                        startActivity(new Intent(this, AddProducts.class));
                    } else {
                        App.selectedProducts.set(App.editingPos, App.product);
                        binding.button.setClickable(false);
                        if (App.product.getSelectedUnit().getSpecialDiscFound() == 1) {
                            App.specialDiscount = 1;
                        }
                        startActivity(new Intent(this, AddProducts.class));
                        App.isEditing = false;
                        App.editingPos = 0;
                    }
                    finish();
                } else {
                    minusCheck();
                    checkoutVM.checkItemMutableLiveData.observe(this, response -> {
                        binding.progress.setVisibility(View.GONE);
                        if (response.getStatus() == 0 && App.currentUser.getAllowStoreMinus() == 2) {
                            String error = response.getMessage();
                            String errorTitle = "نقص فالمخزن";
                            if (response.getMessage().equals("Not saved ... please save again")) {
                                error = "لا يوجد الكمية الكافية من هذا الصنف";
                            }
                            if (response.getMessage().equals("Invoice not saved: Data redundancy.") || response.getMessage().equals("WARNING: Duplicate invoice data.")) {
                                errorTitle = "تكرار بيانات";
                            }
                            new android.app.AlertDialog.Builder(this).setTitle(errorTitle)
                                    .setMessage(error)
                                    .setCancelable(false)
                                    .setPositiveButton("متابعة", (dialogInterface, i) -> {
                                        if (!App.isEditing) {
                                            binding.button.setClickable(false);
                                            App.selectedProducts.add(App.product);
                                            if (App.product.getSelectedUnit().getSpecialDiscFound() == 1) {
                                                App.specialDiscount = 1;
                                            }
                                            startActivity(new Intent(this, AddProducts.class));
                                        } else {
                                            App.selectedProducts.set(App.editingPos, App.product);
                                            binding.button.setClickable(false);
                                            if (App.product.getSelectedUnit().getSpecialDiscFound() == 1) {
                                                App.specialDiscount = 1;
                                            }
                                            startActivity(new Intent(this, AddProducts.class));
                                            App.isEditing = false;
                                            App.editingPos = 0;
                                        }
                                        finish();
                                    }).setNegativeButton("إلغاء", (dialogInterface, i) -> {
                                        checkoutVM.checkItemMutableLiveData = new MutableLiveData<>();
                                        dialogInterface.dismiss();
                                    }).show();
                        } else if (response.getStatus() == 0 && App.currentUser.getAllowStoreMinus() == 1) {
                            String error = response.getMessage();
                            String errorTitle = "نقص فالمخزن";
                            if (response.getMessage().equals("Not saved ... please save again")) {
                                error = "لا يوجد الكمية الكافية من هذا الصنف";
                            }
                            if (response.getMessage().equals("Invoice not saved: Data redundancy.") || response.getMessage().equals("WARNING: Duplicate invoice data.")) {
                                errorTitle = "تكرار بيانات";
                            }
                            new android.app.AlertDialog.Builder(this).
                                    setTitle(errorTitle)
                                    .setMessage(error)
                                    .setCancelable(false)
                                    .setNegativeButton("إلغاء", (dialogInterface, i) -> {
                                        checkoutVM.checkItemMutableLiveData = new MutableLiveData<>();
                                        dialogInterface.dismiss();
                                    }).show();
                        } else {
                            if (!App.isEditing) {
                                App.selectedProducts.add(App.product);
                                binding.button.setClickable(false);
                                if (App.product.getSelectedUnit().getSpecialDiscFound() == 1) {
                                    App.specialDiscount = 1;
                                }
                                startActivity(new Intent(this, AddProducts.class));
                            } else {
                                App.selectedProducts.set(App.editingPos, App.product);
                                binding.button.setClickable(false);
                                if (App.product.getSelectedUnit().getSpecialDiscFound() == 1) {
                                    App.specialDiscount = 1;
                                }
                                startActivity(new Intent(this, AddProducts.class));
                                App.isEditing = false;
                                App.editingPos = 0;
                            }
                            finish();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, AddProducts.class));
        finish();
    }

    public void minusCheck() {
        binding.progress.setVisibility(View.VISIBLE);
        ArrayList<Long> ItemBranchISN = new ArrayList<>();
        ArrayList<Long> ItemISN = new ArrayList<>();
        ArrayList<Long> PriceTypeBranchISN = new ArrayList<>();
        ArrayList<Long> PriceTypeISN = new ArrayList<>();
        ArrayList<Long> StoreBranchISN = new ArrayList<>();
        ArrayList<Long> StoreISN = new ArrayList<>();
        ArrayList<Float> BasicQuantity = new ArrayList<>();
        ArrayList<Float> BonusQuantity = new ArrayList<>();
        ArrayList<Float> TotalQuantity = new ArrayList<>();
        ArrayList<Double> Price = new ArrayList<>();
        ArrayList<Long> MeasureUnitBranchISN = new ArrayList<>();
        ArrayList<Long> MeasureUnitISN = new ArrayList<>();
        ArrayList<Long> BasicMeasureUnitBranchISN = new ArrayList<>();
        ArrayList<Long> BasicMeasureUnitISN = new ArrayList<>();
        ArrayList<String> ItemSerial = new ArrayList<>();
        ArrayList<String> ExpireDate = new ArrayList<>();
        ArrayList<Long> ColorBranchISN = new ArrayList<>();
        ArrayList<Long> ColorISN = new ArrayList<>();
        ArrayList<Long> SizeBranchISN = new ArrayList<>();
        ArrayList<Long> SizeISN = new ArrayList<>();
        ArrayList<Long> SeasonBranchISN = new ArrayList<>();
        ArrayList<Long> SeasonISN = new ArrayList<>();
        ArrayList<Long> Group1BranchISN = new ArrayList<>();
        ArrayList<Long> Group1ISN = new ArrayList<>();
        ArrayList<Long> Group2BranchISN = new ArrayList<>();
        ArrayList<Long> Group2ISN = new ArrayList<>();
        ArrayList<String> LineNotes = new ArrayList<>();
        ArrayList<Double> netPrices = new ArrayList<>();
        ArrayList<Double> basicMeasureUnitQuantity = new ArrayList<>();
        ArrayList<Boolean> expireDateBool = new ArrayList<>();
        ArrayList<Boolean> colorsBool = new ArrayList<>();
        ArrayList<Boolean> sizesBool = new ArrayList<>();
        ArrayList<Boolean> serialBool = new ArrayList<>();
        ArrayList<Boolean> seasonsBool = new ArrayList<>();
        ArrayList<Boolean> group1Bool = new ArrayList<>();
        ArrayList<Boolean> group2Bool = new ArrayList<>();
        ArrayList<Boolean> serviceItem = new ArrayList<>();
        ArrayList<Double> itemTax = new ArrayList<>();//percentage
        ArrayList<Double> itemTaxValue = new ArrayList<>();//value
        ArrayList<String> itemName = new ArrayList<>();//value
        ArrayList<Double> discount1 = new ArrayList<>();

        itemTax.add(Double.parseDouble(App.product.getItemTax()));
        itemTaxValue.add(((App.product.getSelectedUnit().getPrice() / 100) * Double.parseDouble(App.product.getItemTax())) * App.product.getActualQuantity());
        itemName.add(App.product.getItemName());
        discount1.add(App.product.getDiscount1());
        ItemBranchISN.add((long) App.product.getBranchISN());
        ItemISN.add((long) App.product.getItemISN());
        if (App.product.getSelectedPriceType() != null) {
            PriceTypeBranchISN.add((long) App.product.getSelectedPriceType().getBranchISN());
            PriceTypeISN.add((long) App.product.getSelectedPriceType().getPricesType_ISN());
        } else {
            PriceTypeBranchISN.add(0L);
            PriceTypeISN.add(0L);
        }
        if (App.product.getSelectedStore() != null) {
            StoreBranchISN.add((long) App.product.getSelectedStore().getBranchISN());
            StoreISN.add((long) App.product.getSelectedStore().getStore_ISN());
        } else {
            StoreBranchISN.add(0L);
            StoreISN.add(0L);
        }
        BasicQuantity.add(App.product.getActualQuantity());
        TotalQuantity.add(App.product.getQuantity());
        BonusQuantity.add(App.product.getBonusQuantity());
        Price.add(App.product.getPriceItem());
        netPrices.add(App.product.getNetPrice());
        MeasureUnitBranchISN.add((long) App.product.getSelectedUnit().getMeasureUnitBranchISN());
        MeasureUnitISN.add((long) App.product.getSelectedUnit().getMeasureUnitISN());
        basicMeasureUnitQuantity.add(App.product.getSelectedUnit().getBasicUnitQuantity());
        serviceItem.add(App.product.getServiceItem());
        BasicMeasureUnitBranchISN.add((long) App.product.getBasicMeasureUnit().getMeasureUnitBranchISN());
        BasicMeasureUnitISN.add((long) App.product.getBasicMeasureUnit().getMeasureUnitISN());
        if (App.product.getSelectedColor() != null) {
            ColorBranchISN.add((long) App.product.getSelectedColor().getBranchISN());
            ColorISN.add((long) App.product.getSelectedColor().getStoreColorISN());
            colorsBool.add(true);
        } else {
            colorsBool.add(false);
            ColorBranchISN.add(0L);
            ColorISN.add(0L);
        }
        if (App.product.getSelectedSize() != null) {
            SizeBranchISN.add((long) App.product.getSelectedSize().getBranchISN());
            SizeISN.add((long) App.product.getSelectedSize().getStoreSizeISN());
            sizesBool.add(true);
        } else {
            sizesBool.add(false);
            SizeBranchISN.add(0L);
            SizeISN.add((long) 0);
        }
        if (App.product.getSelectedSeason() != null) {
            seasonsBool.add(true);
            SeasonBranchISN.add((long) App.product.getSelectedSeason().getBranchISN());
            SeasonISN.add((long) App.product.getSelectedSeason().getStoreSeasonISN());
        } else {
            seasonsBool.add(false);
            SeasonBranchISN.add(0L);
            SeasonISN.add(0L);
        }
        if (App.product.getSelectedGroup1() != null) {
            seasonsBool.add(true);
            Group1BranchISN.add((long) App.product.getSelectedGroup1().getBranchISN());
            Group1ISN.add((long) App.product.getSelectedGroup1().getStoreGroup1ISN());
        } else {
            seasonsBool.add(false);
            Group1BranchISN.add(0L);
            Group1ISN.add(0L);
        }
        if (App.product.getSelectedGroup2() != null) {
            group2Bool.add(true);
            Group2BranchISN.add((long) App.product.getSelectedGroup2().getBranchISN());
            Group2ISN.add((long) App.product.getSelectedGroup2().getStoreGroup2ISN());
        } else {
            group2Bool.add(false);
            Group2BranchISN.add(0L);
            Group2ISN.add(0L);
        }
        LineNotes.add(App.product.getUserNote());
//            ExpireDate.add(App.product.getSelectedExpireDate());
        if (App.product.getExpireDate()) {
            ExpireDate.add(App.product.getSelectedExpireDate());
            expireDateBool.add(true);
        } else {
            ExpireDate.add("");
            expireDateBool.add(false);
        }
        if (App.product.getSerial()) {
            ItemSerial.add(App.product.getSelectedSerial());
            serialBool.add(true);
        } else {
            ItemSerial.add("");
            serialBool.add(false);
        }
        checkoutVM.checkItem(uuid,
                ItemBranchISN, ItemISN, PriceTypeBranchISN, PriceTypeISN, StoreBranchISN, StoreISN,
                BasicQuantity, BonusQuantity, TotalQuantity, Price, MeasureUnitBranchISN, MeasureUnitISN, BasicMeasureUnitBranchISN, BasicMeasureUnitISN, ItemSerial,
                ExpireDate, ColorBranchISN, ColorISN, SizeBranchISN, SizeISN, SeasonBranchISN, SeasonISN, Group1BranchISN, Group1ISN, Group2BranchISN, Group2ISN, LineNotes,
                netPrices, basicMeasureUnitQuantity, expireDateBool, colorsBool, seasonsBool, sizesBool, serialBool, group1Bool, group2Bool, serviceItem, itemTax, itemTaxValue,
                itemName, discount1, App.currentUser.getAllowStoreMinus(), allowStoreMinusConfirm);
        Log.e("checkout", " checkinnnggg");
    }
}

