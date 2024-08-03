package com.dataflowstores.dataflow.ui.adapters;

import com.dataflowstores.dataflow.pojo.product.ProductData;

public interface CartItemListener {
    void onDeleteItem(ProductData productData);
}
