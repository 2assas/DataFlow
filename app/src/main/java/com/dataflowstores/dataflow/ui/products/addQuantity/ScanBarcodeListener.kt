package com.dataflowstores.dataflow.ui.products.addQuantity

interface ScanBarcodeListener {
    fun onBarcodeScanned(barcode: String)
    fun dismissScanner()
}
