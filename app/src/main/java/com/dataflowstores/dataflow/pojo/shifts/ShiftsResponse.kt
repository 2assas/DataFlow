package com.dataflowstores.dataflow.pojo.shifts

data class ShiftsResponse (
    val status : Int,
    val message : String,
    val data: ShiftData
)

data class ShiftData (
    val LogIn_ShiftBranchISN : String,
    val LogIn_ShiftISN : Long
)