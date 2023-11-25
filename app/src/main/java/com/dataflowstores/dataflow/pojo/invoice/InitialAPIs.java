package com.dataflowstores.dataflow.pojo.invoice;

import com.dataflowstores.dataflow.pojo.settings.Banks;
import com.dataflowstores.dataflow.pojo.settings.PriceType;
import com.dataflowstores.dataflow.pojo.settings.SafeDeposit;
import com.dataflowstores.dataflow.pojo.settings.Stores;

public class InitialAPIs {
    Stores stores;
    Banks banks;
    SafeDeposit safeDeposit;
    PriceType priceType;

    public InitialAPIs(Stores stores, Banks banks, SafeDeposit safeDeposit, PriceType priceType) {
        this.stores = stores;
        this.banks = banks;
        this.safeDeposit = safeDeposit;
        this.priceType = priceType;
    }

    public Stores getStores() {
        return stores;
    }

    public Banks getBanks() {
        return banks;
    }

    public SafeDeposit getSafeDeposit() {
        return safeDeposit;
    }

    public PriceType getPriceType() {
        return priceType;
    }
}
