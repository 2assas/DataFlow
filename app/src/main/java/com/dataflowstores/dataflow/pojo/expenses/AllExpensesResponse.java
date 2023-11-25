package com.dataflowstores.dataflow.pojo.expenses;

public class AllExpensesResponse {
    MainExpResponse mainExpResponse;
    SubExpResponse subExpResponse;
    WorkerResponse workerResponse;

    public AllExpensesResponse(MainExpResponse mainExpResponse, SubExpResponse subExpResponse, WorkerResponse workerResponse) {
        this.mainExpResponse = mainExpResponse;
        this.subExpResponse = subExpResponse;
        this.workerResponse = workerResponse;
    }

    public MainExpResponse getMainExpResponse() {
        return mainExpResponse;
    }

    public SubExpResponse getSubExpResponse() {
        return subExpResponse;
    }

    public WorkerResponse getWorkerResponse() {
        return workerResponse;
    }
}
