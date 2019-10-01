package model;

import com.google.gson.JsonObject;
import controller.apiService;
import pl.zankowski.iextrading4j.api.stocks.Quote;

public class shareItem {
    private String _symbol;
    private String _name;
    private String _price;
    private int _amount;

    public int get_amount() { return _amount; }
    public String get_price() {
        return _price;
    }
    public String get_name() {
        return _name;
    }
    public String getSymbol(){
        return _symbol;
    }

    public shareItem(String symbol){
        _symbol = symbol;
    }
    public shareItem(String symbol,String name,String price){
        _symbol = symbol;
        _name = name;
        _price = price;
    }
    public shareItem(String symbol,String price){
        _symbol = symbol;
        _price = price;
    }
    public shareItem(String symbol, int amount){
        _symbol = symbol;
        _amount = amount;
    }

    public void updateStock(apiService api){
        shareItem quote = api.getBySymb(_symbol);
        _price = (quote.get_price());
        _name = quote.get_name();
        System.out.println(_name);
    }

    public JsonObject toJson(){
        JsonObject res = new JsonObject();
        res.addProperty("symbol",_symbol);
        res.addProperty("company",_name);
        res.addProperty("price",_price);
        res.addProperty("uAmount",0);
        return res;
    }
    @Override
    public String toString(){
        return "Sym:" + _symbol + " Company:" + _name + " Price:" + _price;
    }
}
