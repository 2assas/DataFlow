package com.dataflowstores.dataflow.ui.reports.supplierSalesReport;

import static com.dataflowstores.dataflow.App.theme;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.ViewModels.InvoiceViewModel;
import com.dataflowstores.dataflow.databinding.ItemSalesReportBinding;
import com.dataflowstores.dataflow.pojo.financialReport.ReportBody;
import com.dataflowstores.dataflow.pojo.report.Branches;
import com.dataflowstores.dataflow.pojo.report.DataItem;
import com.dataflowstores.dataflow.pojo.report.WorkersResponse;
import com.dataflowstores.dataflow.pojo.users.CustomerData;
import com.dataflowstores.dataflow.pojo.workStation.BranchData;
import com.dataflowstores.dataflow.ui.BaseActivity;
import com.dataflowstores.dataflow.ui.SplashScreen;
import com.dataflowstores.dataflow.ui.fragments.BottomSheetFragment;
import com.dataflowstores.dataflow.ui.listeners.MyDialogCloseListener;
import com.dataflowstores.dataflow.ui.reports.ReportViewModel;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class SupplierSalesReport extends BaseActivity implements MyDialogCloseListener {
    ItemSalesReportBinding binding;
    ReportBody reportBody = new ReportBody();
    ReportViewModel reportVM;
    Branches branches;
    WorkersResponse workersResponse;
    BranchData selectedBranch;
    DataItem selectedWorker;
    String currentDate;
    Calendar myCalendar = Calendar.getInstance();
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH); //Date and time
    String uuid;
    String startTime, endTime, shiftNum;
    private String workDayStart, workDayEnd;
    CustomerData customerData;

    String workerCISN, WorkerCBranchISN;
    InvoiceViewModel invoiceViewModel;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            startActivity(new Intent(this, SplashScreen.class));
            finishAffinity();
        } else {
            binding = DataBindingUtil.setContentView(this, R.layout.item_sales_report);
            reportVM = new ViewModelProvider(this).get(ReportViewModel.class);
            invoiceViewModel = new ViewModelProvider(this).get(InvoiceViewModel.class);
            uuid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            initViews();
        }
    }

    void initViews() {
        if (App.currentUser.getPermission() == 1)
            binding.progress.setVisibility(View.VISIBLE);
        binding.clientText.setText("المورد");
        binding.title.setText("تقرير مشتريات الأصناف");
        binding.back.setOnClickListener(v -> finish());
        reportVM.toastErrorMutableLiveData.observe(this, s -> Toast.makeText(this, s, Toast.LENGTH_LONG).show());
        observers();
        collectData();
        fillDates();
        searchButtons();
    }

    void fillDates() {
        Calendar calendar = Calendar.getInstance();
        String date = sdf.format(calendar.getTime());
        binding.startTime.setText(date);
        binding.endTime.setText(date);
        binding.workStartTime.setText(date);
        binding.workEndTime.setText(date);
    }

    void observers() {
        reportVM.branchesMutableLiveData.observe(this, this::branchSpinner);
        reportVM.workersResponseMutableLiveData.observe(this, this::workerSpinner);
        reportVM.supplierSalesResponseMutableLiveData.observe(this, itemSalesResponse -> {
            binding.progress.setVisibility(View.GONE);
            binding.reportButton.setEnabled(true);
            if (itemSalesResponse.getStatus() == 1) {
                Intent intent = new Intent(this, SupplierSalesPrintScreen.class);
                intent.putExtra("itemSalesData", (Serializable) itemSalesResponse.getData());

                if (binding.intervalCheckBox.isChecked()) {
                    intent.putExtra("startTime", startTime);
                    intent.putExtra("endTime", endTime);
                }
                if (binding.clientCheckBox.isChecked()) {
                    if (App.customer != null && !binding.getClient.getText().toString().isEmpty()) {
                        intent.putExtra("clientName", App.customer.getDealerName());
                    }
                }
                if (App.currentUser.getPermission() == 0) {
                    intent.putExtra("userName", App.currentUser.getWorkerName());
                } else if (binding.userCheckbox.isChecked()) {
                    intent.putExtra("userName", selectedWorker.getWorkerName());
                }
                if (App.currentUser.getPermission() == 1) {
                    intent.putExtra("branch", selectedBranch.getBranchName());
                } else
                    intent.putExtra("branch", binding.branchName.getText().toString());

                if (binding.workDateCheckbox.isChecked()) {
                    intent.putExtra("workdayStart", workDayStart);
                    intent.putExtra("workdayEnd", workDayEnd);
                }
                if (binding.shiftsCheckbox.isChecked()) {
                    intent.putExtra("shiftNum", binding.shiftISN.getText().toString());
                }
                if (binding.clientCheckBox.isChecked()) {
                    if (App.customer == null) {
                        intent.putExtra("shiftNum", binding.shiftISN.getText().toString());

                    }
                }
                if (itemSalesResponse.getData().size() > 0) {
                    intent.putExtra("dataList", (Serializable) itemSalesResponse.getData());
                    binding.progress.setVisibility(View.GONE);
                    startActivity(intent);
                }


            } else {
                Toast.makeText(this, "لا توجد نتائج!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void searchButtons() {
        binding.getClient.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (!binding.getClient.getText().toString().isEmpty()) {
                    performSearch();
                    return true;
                } else
                    return false;
            }
            return false;
        });
        binding.searchClient.setOnClickListener(view -> {
            performSearch();
        });
    }

    private void performSearch() {
        if (App.isNetworkAvailable(this))
            invoiceViewModel.getSupplier(uuid, binding.getClient.getText().toString(), App.currentUser.getWorkerBranchISN(), App.currentUser.getWorkerISN());
        else {
            App.noConnectionDialog(this);
        }
        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        if (App.customer != null && App.customer.getDealerName() != null) {
            binding.getClient.setText(App.customer.getDealerName());
            customerData = App.customer;
        }
    }

    void collectData() {
        if (App.currentUser.getPermission() == 0) {
            reportBody = new ReportBody();
            binding.userName.setText(App.currentUser.getWorkerName());
            binding.branchSpinner.setVisibility(View.GONE);
            binding.branchName.setVisibility(View.VISIBLE);
            binding.branchName.setText(App.currentUser.getBranchName());

            //worker
            binding.userCheckbox.setChecked(true);
            binding.userCheckbox.setEnabled(false);
            binding.userSpinner.setEnabled(false);

            //FromTo
            binding.intervalCheckBox.setEnabled(false);

            //workDay
            binding.workDateCheckbox.setChecked(true);
            binding.workDateCheckbox.setEnabled(false);
            currentDate = sdf.format(Calendar.getInstance().getTime());
            binding.workStartTime.setText(currentDate);
            binding.workEndTime.setText(currentDate);

            if (binding.intervalCheckBox.isChecked()) {
                startTime = binding.startTime.getText().toString();
                endTime = binding.endTime.getText().toString();
            } else {
                startTime = null;
                endTime = null;
            }
            //shift
            binding.shiftsCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    binding.shiftISN.setVisibility(View.VISIBLE);
                } else {
                    binding.shiftISN.setVisibility(View.GONE);
                }
            });
            binding.shiftsCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    binding.shiftISN.setVisibility(View.VISIBLE);
                } else {
                    binding.shiftISN.setVisibility(View.GONE);
                }
            });


            //showReport
            binding.reportButton.setOnClickListener(v -> {
                if (App.isNetworkAvailable(this)) {
                    if (binding.clientCheckBox.isChecked()) {
                        if (App.customer == null) {
                            binding.getClient.setError(getString(R.string.select_customer_error));
                            Toast.makeText(this, getString(R.string.select_customer_toast), Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            customerData = App.customer;
                        }
                    } else {
                        customerData = null;
                    }
                    binding.progress.setVisibility(View.VISIBLE);
                    binding.reportButton.setEnabled(false);
                    if (binding.shiftsCheckbox.isChecked()) {
                        if (!binding.shiftISN.getText().toString().isEmpty()) {
                            shiftNum = binding.shiftISN.getText().toString();
                        }
                    } else {
                        shiftNum = null;
                    }
                    workDayStart = binding.workStartTime.getText().toString();
                    workDayEnd = binding.workEndTime.getText().toString();
                    binding.progress.setVisibility(View.VISIBLE);
                    reportVM.getItemSalesReport(uuid, App.currentUser.getBranchISN(),
                            workDayStart, workDayEnd, shiftNum, App.currentUser.getWorkerBranchISN(),
                            String.valueOf(App.currentUser.getWorkerISN()), startTime, endTime, App.currentUser.getVendorID(), String.valueOf(App.currentUser.getWorkerISN()), String.valueOf(App.currentUser.getWorkerBranchISN())
                            , customerData == null ? null : customerData.getDealerType()
                            , customerData == null ? null : (long) customerData.getBranchISN()
                            , customerData == null ? null : customerData.getDealer_ISN());
                }
            });
        }
        else {
            binding.shiftISN.setVisibility(View.VISIBLE);
            reportVM.getBranches(uuid);
            reportVM.getWorkers(uuid);
            binding.startTime.setOnClickListener(v -> dateTimePicker(binding.startTime));
            binding.endTime.setOnClickListener(v -> dateTimePicker(binding.endTime));
            binding.workStartTime.setOnClickListener(v -> datePicker(binding.workStartTime));
            binding.workEndTime.setOnClickListener(v -> datePicker(binding.workEndTime));
            binding.workDateCheckbox.setChecked(true);
            binding.reportButton.setOnClickListener(v -> {
                if (App.isNetworkAvailable(this)) {
                    if (binding.clientCheckBox.isChecked()) {
                        if (App.customer == null) {
                            binding.getClient.setError(getString(R.string.select_customer_error));
                            Toast.makeText(this, getString(R.string.select_customer_toast), Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            customerData = App.customer;
                        }
                    } else {
                        customerData = null;
                    }

                    reportBody = new ReportBody();
                    reportBody.setBranchISN(selectedBranch.getBranchISN());
                    if (binding.shiftsCheckbox.isChecked()) {
                        if (!binding.shiftISN.getText().toString().isEmpty()) {
                            reportBody.setShiftISN(binding.shiftISN.getText().toString());
                            shiftNum = binding.shiftISN.getText().toString();
                        }
                    } else {
                        shiftNum = null;
                    }
                    selectedWorker = workersResponse.getData().get(binding.userSpinner.getSelectedItemPosition());

                    if (binding.userCheckbox.isChecked()) {
                        reportBody.setWorker_ISN(selectedWorker.getWorkerISN());
                    } else {
                        selectedWorker = null;
                    }
                    if (binding.intervalCheckBox.isChecked()) {
                        if (!binding.startTime.getText().toString().isEmpty()) {
                            reportBody.setFrom(binding.startTime.getText().toString());
                            startTime = binding.startTime.getText().toString();
                        }
                        if (!binding.endTime.getText().toString().isEmpty()) {
                            reportBody.setTo(binding.endTime.getText().toString());
                            endTime = binding.endTime.getText().toString();
                        }
                    } else {
                        startTime = null;
                        endTime = null;
                    }
                    if (binding.workDateCheckbox.isChecked()) {
                        if (!binding.workStartTime.getText().toString().isEmpty()) {
                            reportBody.setFromWorkday(binding.workStartTime.getText().toString());
                            workDayStart = binding.workStartTime.getText().toString();
                        }
                        if (!binding.workEndTime.getText().toString().isEmpty()) {
                            reportBody.setToWorkday(binding.workEndTime.getText().toString());
                            workDayEnd = binding.workEndTime.getText().toString();
                        }
                    } else {
                        workDayStart = null;
                        workDayEnd = null;
                    }
                    binding.progress.setVisibility(View.VISIBLE);
                    reportVM.getSupplierSalesReport(uuid, selectedBranch.getBranchISN(),
                            workDayStart, workDayEnd, shiftNum, App.currentUser.getWorkerBranchISN(),
                            String.valueOf(App.currentUser.getWorkerISN()), startTime, endTime, App.currentUser.getVendorID(), selectedWorker == null ? null : selectedWorker.getWorkerISN(), selectedWorker == null ? null : selectedWorker.getBranchISN()
                            , customerData == null ? null : customerData.getDealerType()
                            , customerData == null ? null : (long) customerData.getBranchISN()
                            , customerData == null ? null : customerData.getDealer_ISN()
                    );
                }
            });

        }

        if (App.currentUser.getMobilePreviousDatesInReports() == 1) {
            binding.workStartTime.setOnClickListener(v -> datePicker(binding.workStartTime));
            binding.workEndTime.setOnClickListener(v -> datePicker(binding.workEndTime));
        }
    }

    public void dateTimePicker(TextView tv) {
        DatePickerDialog.OnDateSetListener dateTime;
        myCalendar = Calendar.getInstance();
        TimePickerDialog.OnTimeSetListener timeSetListener = (view, hourOfDay, minute) -> {
            myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            myCalendar.set(Calendar.MINUTE, minute);
            updateDateTime(tv);
        };
        dateTime = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            new TimePickerDialog(this, timeSetListener, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show();
        };

        tv.setOnClickListener(v -> {
            new DatePickerDialog(this, dateTime, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    public void datePicker(TextView tv) {
        myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date;
        date = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(tv);
        };
        tv.setOnClickListener(v -> {
            new DatePickerDialog(this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void updateLabel(TextView tv) {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        tv.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateDateTime(TextView tv) {
        String myFormat = "dd-MM-yyyy HH:mm"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        tv.setText(sdf.format(myCalendar.getTime()));
    }

    void branchSpinner(Branches branches) {
        this.branches = branches;
        checkLoading();
        ArrayList<String> branchList = new ArrayList<>();
        int position = 0;
        for (int i = 0; i < branches.getBranchData().size(); i++) {
            branchList.add(branches.getBranchData().get(i).getBranchName());
            if (App.currentUser.getBranchISN() == branches.getBranchData().get(i).getBranchISN()) {
                position = i;
            }
        }
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, branchList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.branchSpinner.setAdapter(aa);
        selectedBranch = branches.getBranchData().get(0);
        binding.branchSpinner.setSelection(position);
        int finalPosition = position;
        binding.branchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedBranch = branches.getBranchData().get(i);
                binding.shiftISN.setText("");
                binding.shiftsCheckbox.setChecked(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedBranch = branches.getBranchData().get(finalPosition);
            }
        });
    }

    void workerSpinner(WorkersResponse workersResponse) {
        this.workersResponse = workersResponse;
        checkLoading();
        ArrayList<String> workers = new ArrayList<>();
        for (int i = 0; i < workersResponse.getData().size(); i++) {
            workers.add(workersResponse.getData().get(i).getWorkerName());
        }
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, workers);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.userSpinner.setAdapter(aa);
        binding.userSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedWorker = workersResponse.getData().get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedWorker = workersResponse.getData().get(0);
            }
        });
    }

    void checkLoading() {
        if (branches != null && workersResponse != null) {
            binding.progress.setVisibility(View.GONE);
        }
    }
}
