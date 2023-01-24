package com.dataflowstores.dataflow.ui.reports.financialReport;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
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
import com.dataflowstores.dataflow.databinding.FinancialReportBinding;
import com.dataflowstores.dataflow.pojo.financialReport.PaymentMethods;
import com.dataflowstores.dataflow.pojo.financialReport.ReportBody;
import com.dataflowstores.dataflow.pojo.report.Branches;
import com.dataflowstores.dataflow.pojo.report.DataItem;
import com.dataflowstores.dataflow.pojo.report.WorkersResponse;
import com.dataflowstores.dataflow.pojo.settings.Banks;
import com.dataflowstores.dataflow.pojo.settings.BanksData;
import com.dataflowstores.dataflow.pojo.settings.SafeDeposit;
import com.dataflowstores.dataflow.pojo.settings.SafeDepositData;
import com.dataflowstores.dataflow.pojo.workStation.BranchData;
import com.dataflowstores.dataflow.ui.SplashScreen;
import com.dataflowstores.dataflow.ui.reports.ReportViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class FinancialReport extends AppCompatActivity {
    FinancialReportBinding binding;
    ReportBody reportBody = new ReportBody();
    ReportViewModel reportVM;
    Branches branches;
    Banks banks;
    SafeDeposit safeDeposit;
    WorkersResponse workersResponse;
    BranchData selectedBranch;
    BanksData selectedBank;
    DataItem selectedWorker;
    SafeDepositData selectedSafeDeposit;
    int cash = 1;
    int check = 1;
    int credit = 1;
    String currentDate;
    Calendar myCalendar = Calendar.getInstance();
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH); //Date and time
    String uuid;
    String startTime, endTime;
    private String workDayStart, workDayEnd;


    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            startActivity(new Intent(this, SplashScreen.class));
            finishAffinity();
        }else{
        binding = DataBindingUtil.setContentView(this, R.layout.financial_report);
        reportVM = new ViewModelProvider(this).get(ReportViewModel.class);
        uuid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        initViews();
        }
    }

    void initViews() {
        binding.progress.setVisibility(View.VISIBLE);
        binding.back.setOnClickListener(v -> finish());
        reportVM.toastErrorMutableLiveData.observe(this, s -> Toast.makeText(this, s, Toast.LENGTH_LONG).show());
        observers();
        collectData();
        fillDates();
    }
    void fillDates(){
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
        reportVM.safeDepositMutableLiveData.observe(this, this::safeDepositSpinner);
        reportVM.banksMutableLiveData.observe(this, this::banksSpinner);
        reportVM.financialReportResponseMutableLiveData.observe(this, financialReportResponse -> {
            binding.progress.setVisibility(View.GONE);
            if (financialReportResponse.getStatus() == 1) {
                Intent intent = new Intent(this, FinancialReportPrintScreen.class);
                App.financialReportData = financialReportResponse.getData();
                if (binding.safeDepositCheckbox.isChecked())
                    intent.putExtra("safeDeposit", selectedSafeDeposit);
                if (binding.intervalCheckBox.isChecked()) {
                    intent.putExtra("startTime", startTime);
                    intent.putExtra("endTime", endTime);
                }
                if (binding.bankCheckbox.isChecked()) {
                    intent.putExtra("bank", selectedBank.getBankName());
                }
                if (App.currentUser.getPermission()==0) {
                    intent.putExtra("userName", App.currentUser.getWorkerName());
                }else if(binding.userCheckbox.isChecked()) {
                    intent.putExtra("userName", selectedWorker.getWorkerName());
                }
                intent.putExtra("cash", String.valueOf(cash));
                intent.putExtra("credit", String.valueOf(credit));
                intent.putExtra("check", String.valueOf(check));
                if(App.currentUser.getPermission()==1) {
                    intent.putExtra("branch", selectedBranch.getBranchName());
                }else
                    intent.putExtra("branch", binding.branchName.getText().toString());

                if (binding.workDateCheckbox.isChecked()) {
                    intent.putExtra("workdayStart", workDayStart);
                    intent.putExtra("workdayEnd", workDayEnd);
                }
                startActivity(intent);
            }
        });
    }

    void collectData() {
        if (App.currentUser.getPermission() == 0) {
            reportBody = new ReportBody();
            reportVM.getSafeDeposit(App.currentUser.getBranchISN(), uuid,0);
            binding.userName.setText(App.currentUser.getWorkerName());
            binding.branchSpinner.setVisibility(View.GONE);
            binding.branchName.setVisibility(View.VISIBLE);
            binding.branchName.setText(App.currentUser.getBranchName());
            //bank
            binding.bankCheckbox.setEnabled(false);
            binding.bankSpinner.setEnabled(false);

            //safeDeposit
            binding.safeDepositCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    binding.safeDepositSpinner.setVisibility(View.VISIBLE);
                } else {
                    binding.safeDepositSpinner.setVisibility(View.GONE);
                }
            });
            //shift
            binding.shiftsCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    binding.shiftISN.setVisibility(View.VISIBLE);
                } else {
                    binding.shiftISN.setVisibility(View.GONE);
                }
            });

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

            //check, cash and credit.
            binding.checkCheckbox.setChecked(true);
            binding.checkCheckbox.setEnabled(false);
            binding.cashCheckBox.setChecked(true);
            binding.cashCheckBox.setEnabled(false);
            binding.creditCheckbox.setChecked(true);
            binding.creditCheckbox.setEnabled(false);

            //showReport
            binding.reportButton.setOnClickListener(v -> {
                reportBody = new ReportBody();
                fillCashierReport();
                reportVM.getFinancialReport(reportBody, uuid, App.currentUser.getCashierStoreBranchISN(), App.currentUser.getCashierStoreISN(), App.currentUser.getWorkerBranchISN());
            });


        } else {
            binding.safeDepositSpinner.setVisibility(View.VISIBLE);
            binding.shiftISN.setVisibility(View.VISIBLE);
            reportVM.getBranches(uuid);
            reportVM.getWorkers(uuid);
            binding.startTime.setOnClickListener(v -> dateTimePicker(binding.startTime));
            binding.endTime.setOnClickListener(v -> dateTimePicker(binding.endTime));
            binding.workStartTime.setOnClickListener(v -> datePicker(binding.workStartTime));
            binding.workEndTime.setOnClickListener(v -> datePicker(binding.workEndTime));
            binding.workDateCheckbox.setChecked(true);
            binding.reportButton.setOnClickListener(v -> {
                reportBody = new ReportBody();
                reportBody.setBranchISN(selectedBranch.getBranchISN());
                if (binding.safeDepositCheckbox.isChecked()) {
                    reportBody.setSafeDeposit_ISN(String.valueOf(selectedSafeDeposit.getSafeDeposit_ISN()));
                    reportBody.setSafeDepositBranchISN(String.valueOf(selectedSafeDeposit.getBranchISN()));
                }
                if (binding.shiftsCheckbox.isChecked())
                    if (!binding.shiftISN.getText().toString().isEmpty())
                        reportBody.setShiftISN(binding.shiftISN.getText().toString());
                if (binding.userCheckbox.isChecked())
                    reportBody.setWorker_ISN(selectedWorker.getWorkerISN());
                if (binding.intervalCheckBox.isChecked()) {
                    if (!binding.startTime.getText().toString().isEmpty()) {
                        reportBody.setFrom(binding.startTime.getText().toString());
                        startTime = binding.startTime.getText().toString();
                    }
                    if (!binding.endTime.getText().toString().isEmpty()) {
                        reportBody.setTo(binding.endTime.getText().toString());
                        endTime = binding.endTime.getText().toString();
                    }
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
                }

                if (binding.bankCheckbox.isChecked()) {
                    Log.e("falseAlarm", "false");
                    reportBody.setBankISN(String.valueOf(selectedBank.getBank_ISN()));
                    reportBody.setBankBranchISN(String.valueOf(selectedBank.getBranchISN()));
                }
                binding.cashCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        cash = 1;
                    } else {
                        cash = 0;
                    }
                });

                binding.checkCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        check = 1;
                    } else {
                        check = 0;
                    }
                });

                binding.creditCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        credit = 1;
                    } else {
                        credit = 0;
                    }
                });
                reportBody.setPaymentMethods(new PaymentMethods(check, credit, cash));
                reportVM.getFinancialReport(reportBody, uuid, App.currentUser.getCashierStoreBranchISN(), App.currentUser.getCashierStoreISN(), App.currentUser.getWorkerBranchISN());
            });

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

    void fillCashierReport() {
        reportBody.setBranchISN(App.currentUser.getBranchISN());
        if (binding.safeDepositCheckbox.isChecked()) {
            reportBody.setSafeDeposit_ISN(String.valueOf(selectedSafeDeposit.getSafeDeposit_ISN()));
            reportBody.setSafeDepositBranchISN(String.valueOf(selectedSafeDeposit.getBranchISN()));
        }
        reportBody.setFromWorkday(currentDate);
        reportBody.setToWorkday(currentDate);

        binding.cashCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                cash = 1;
            } else {
                cash = 0;
            }
        });

        binding.checkCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                check = 1;
            } else {
                check = 0;
            }
        });

        binding.creditCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                credit = 1;
            } else {
                credit = 0;
            }
        });

        reportBody.setPaymentMethods(new PaymentMethods(check, credit, cash));
        if (binding.shiftsCheckbox.isChecked() && !binding.shiftISN.getText().toString().isEmpty())
            reportBody.setShiftISN(binding.shiftISN.getText().toString());

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
                banks = null;
                safeDeposit = null;
                binding.safeDepositSpinner.setAdapter(null);
                binding.bankSpinner.setAdapter(null);
                binding.shiftISN.setText("");
                binding.safeDepositCheckbox.setChecked(false);
                binding.bankCheckbox.setChecked(false);
                binding.shiftsCheckbox.setChecked(false);
                reportVM.getBanks(selectedBranch.getBranchISN(), uuid);
                reportVM.getSafeDeposit(selectedBranch.getBranchISN(), uuid,0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedBranch = branches.getBranchData().get(finalPosition);
            }
        });
        reportVM.getBanks(selectedBranch.getBranchISN(), uuid);
        reportVM.getSafeDeposit(selectedBranch.getBranchISN(), uuid,0);
    }

    void safeDepositSpinner(SafeDeposit safeDeposit) {
        this.safeDeposit = safeDeposit;
        checkLoading();
        if (safeDeposit.getStatus() == 1) {
            ArrayList<String> safeDepositList = new ArrayList<>();
            for (int i = 0; i < safeDeposit.getData().size(); i++) {
                safeDepositList.add(safeDeposit.getData().get(i).getSafeDepositName());
            }
            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, safeDepositList);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.safeDepositSpinner.setAdapter(aa);
            binding.safeDepositSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedSafeDeposit = safeDeposit.getData().get(i);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    selectedSafeDeposit = safeDeposit.getData().get(0);
                }
            });
            for (int i = 0; i < safeDeposit.getData().size(); i++) {
                if (App.currentUser.getSafeDepositISN() == safeDeposit.getData().get(i).getSafeDeposit_ISN()
                        && App.currentUser.getSafeDepositBranchISN() == safeDeposit.getData().get(i).getBranchISN())
                    binding.safeDepositSpinner.setSelection(i);
            }

        }
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

    void banksSpinner(Banks banks) {
        this.banks = banks;
        checkLoading();
        if (banks.getStatus() == 1) {
            binding.bankSpinner.setVisibility(View.VISIBLE);
            binding.bankCheckbox.setEnabled(true);
            ArrayList<String> banksList = new ArrayList<>();
            for (int i = 0; i < banks.getData().size(); i++) {
                banksList.add(banks.getData().get(i).getBankName());
            }
            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, banksList);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.bankSpinner.setAdapter(aa);
            binding.bankSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedBank = banks.getData().get(i);
                    Log.e("checkBank", " selected " + i);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        } else {
            binding.bankSpinner.setVisibility(View.GONE);
            binding.bankCheckbox.setChecked(false);
            binding.bankCheckbox.setEnabled(false);
        }
    }

    void checkLoading() {
        if (App.currentUser.getPermission() == 1) {
            if (banks != null && branches != null && safeDeposit != null && workersResponse != null) {
                binding.progress.setVisibility(View.GONE);
            }
        } else {
            if (safeDeposit != null) {
                binding.progress.setVisibility(View.GONE);
            }
        }
    }
}
