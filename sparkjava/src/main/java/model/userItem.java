package model;

public class userItem {

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

}
