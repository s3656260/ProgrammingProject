package model;

import controller.databaseService;

public class userItem {
    private double _Money;
    private String _user_id;
    private String _User_Name;
    private databaseService _Database;

    public void set_Money(double _Money) {
        this._Money = _Money;
        _Database.updateUserCurrency(_user_id,_Money);
    }
    public double get_Money() {
        return _Money;
    }

    public String get_user_id() {
        return _user_id;
    }

    public userItem(double money, String id,databaseService Database){
        _user_id = id;
        _Database = Database;
        this.set_Money(money);
    }

    public void set_Database(databaseService _Database) {
        this._Database = _Database;
        this._Money = _Database.getUserCurrency(this._user_id);
    }

    public void rmv_Money(double money){
        this.set_Money(_Money - money);
    }
    public void add_money(double money){
        this.set_Money(_Money + money);
    }
}
