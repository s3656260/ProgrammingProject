package model;

import com.google.gson.JsonObject;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class transaction {
    private String _UserID;
    private String _Symbol;
    private String _Type;
    private int _Amount;
    private Timestamp _DTime;
    private double _Value;

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

    public double get_Value() {
        return _Value;
    }

    public transaction(String userid, String symbol, String type, int amount, Timestamp dTime, double value) {
        this._UserID = userid;
        this._Symbol = symbol;
        this._Type = type;
        this._Amount = amount;
        this._DTime = dTime;
        this._Value = value;
    }

    public JsonObject toJson(){
        JsonObject res = new JsonObject();
        res.addProperty("symbol",_Symbol);
        res.addProperty("type",_Type);
        res.addProperty("amount",get_Amount());
        res.addProperty("value",_Value);
        res.addProperty("date/time",_DTime.toString());
        return res;
    }
}
