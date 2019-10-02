package model;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class transaction {
    private String _UserID;
    private String _Symbol;
    private String _Type;
    private int _Amount;
    private Timestamp _DTime;

    public String get_UserID() {
        return _UserID;
    }

    public String get_Symbol() {
        return _Symbol;
    }

    public String get_Type() {
        return _Type;
    }

    public int get_Amount() {
        return _Amount;
    }

    public Timestamp get_DTime() {
        return _DTime;
    }

    public transaction(String userid,String symbol, String type, int amount, Timestamp dTime) {
        this._UserID = userid;
        this._Symbol = symbol;
        this._Type = type;
        this._Amount = amount;
        this._DTime = dTime;
    }


}
