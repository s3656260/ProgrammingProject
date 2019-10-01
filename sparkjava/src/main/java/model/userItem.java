package model;

public class userItem {

    public void set_Money(double _Money) {
        this._Money = _Money;
    }

    private double _Money;

    public userItem(double money){
        _Money = money;
    }

    public double get_Money() {
        return _Money;
    }
    public void rmv_Money(double money){
        _Money = _Money - money;
    }
    public void add_money(double money){
        _Money = _Money + money;
    }
}
