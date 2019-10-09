package model;

public class userItem {
    private double _Money;
    private String _user_id;

    public void set_Money(double _Money) {
        this._Money = _Money;
    }
    public double get_Money() {
        return _Money;
    }

    public String get_user_id() {
        return _user_id;
    }

    public userItem(double money, String id){
        _Money = money;
        _user_id = id;
    }


    public void rmv_Money(double money){
        _Money = _Money - money;
    }

}
