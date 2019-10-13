package model;

import controller.databaseService;

public class userItem {
    private double _Money;
    private String _user_id;
    private String _User_Name;
    private databaseService _Database;

    public String get_User_Name() {
        return _User_Name;
    }
    public void set_User_Name(String _User_Name) {
        this._User_Name = _User_Name;
    }
    public void set_Money(double _Money) {
        this._Money = _Money;
    }
    public double get_Money() {
        return _Money;
    }
    public String get_user_id() {
        return _user_id;
    }

    public userItem(double money, String id,databaseService Database){
        _Money = money;
        _user_id = id;
        _Database = Database;
    }
    public userItem(String userName, String userId){
        _user_id = userId;
        _User_Name = userName;
    }

    public void set_Database(databaseService _Database) {
        this._Database = _Database;
        //_Database.get
    }

    public void rmv_Money(double money){
        _Money = _Money - money;
    }
    public void add_money(double money){
        _Money = _Money + money;
    }
}
