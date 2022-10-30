package com.dataflowstores.dataflow.pojo.invoice;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Invoice implements Serializable {

    @SerializedName("MoveHeader")
    MoveHeader moveHeader= new MoveHeader();
    @SerializedName("MoveLines")
    ArrayList<MoveLines> moveLines = new ArrayList<>();

    public MoveHeader getMoveHeader() {
        return moveHeader;
    }

    public void setMoveHeader(MoveHeader moveHeader) {
        this.moveHeader = moveHeader;
    }

    public ArrayList<MoveLines> getMoveLines() {
        return moveLines;
    }

    public void setMoveLines(ArrayList<MoveLines> moveLines) {
        this.moveLines = moveLines;
    }
}
