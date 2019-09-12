export default class Client {

    getTop() {
        var xhttp = new XMLHttpRequest();
        var myObj = null;
        xmlhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
              myObj = JSON.parse(this.responseText);
        xhttp.open("GET", "http://localhost:4567/test/top", true);
        xhttp.send();
        return(myObj)
    }

    /*numbers(){
        let result = new Promise((resolve, reject) => {
            let request = new XMLHttpRequest();
            request.open("GET", "http://localhost:4567/test/top");
            request.onreadystatechange = () => {
                let raw = request.responseText;
                let objectified = JSON.parse(raw);
                resolve(objectified);
            }
            request.send();


        });

        return result;
    }*/
    /*const myJson = [
            {"symbol":"GE","company":"General Electric Co.","price":"7.93"},
            {"symbol":"MO","company":"Altria Group, Inc.","price":"45.25"},
            {"symbol":"CHK","company":"Chesapeake Energy Corp.","price":"1.39"},
            {"symbol":"AMD","company":"Advanced Micro Devices, Inc.","price":"30.2"},
            {"symbol":"BAC","company":"Bank of America Corp.","price":"26.47"},
            {"symbol":"PM","company":"Philip Morris International, Inc.","price":"71.7"},
            {"symbol":"T","company":"AT&T, Inc.","price":"34.72"},
            {"symbol":"SNAP","company":"Snap, Inc.","price":"15.51"},
            {"symbol":"NLY","company":"Annaly Capital Management, Inc.","price":"8.42"},
            {"symbol":"ECA","company":"Encana Corp.","price":"4.31"}];*/
    
}