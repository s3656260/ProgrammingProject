package model;

import controller.apiService;

import java.util.ArrayList;
import java.util.List;

public class popularShares {
    private List<String> _topShareSymb;
    private List<shareItem> _topShares;
    private apiService api;
    public popularShares(){
        _topShareSymb = new ArrayList<>();
        _topShares = new ArrayList<>();
        api = new apiService();
        generateList();
        genTopShares();
    }
    public List<shareItem> getList(){
        return _topShares;
    }
    private void generateList(){
        _topShareSymb.add("aapl");
        _topShareSymb.add("amd");
        _topShareSymb.add("tvix");
        _topShareSymb.add("lyft");
        _topShareSymb.add("csco");
        _topShareSymb.add("msft");
        _topShareSymb.add("mu");
        _topShareSymb.add("intc");
        _topShareSymb.add("qqq");
        _topShareSymb.add("nvda");
        _topShareSymb.add("siri");
        _topShareSymb.add("znga");
    }
    private void genTopShares(){
        shareItem itm;
        for (String str: _topShareSymb) {
            itm = new shareItem(str);
            itm.updateStock(api);
            _topShares.add(itm);
        }
    }
}
