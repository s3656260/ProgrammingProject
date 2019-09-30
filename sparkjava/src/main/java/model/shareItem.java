package model;

import com.google.gson.JsonObject;
import controller.apiService;
import pl.zankowski.iextrading4j.api.stocks.Quote;

public class shareItem {
    private String _symbol;
    private String _name;
    private String _price;

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

    public void updateStock(apiService api){
        Quote quote = api.getBySymb(_symbol);
        _price = (quote.getLatestPrice()).toString();
        _name = quote.getCompanyName();
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
