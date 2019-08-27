export default class Client {

    numbers() {
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

    }
}