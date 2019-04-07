import * as request from "request-promise-native";

export class HttpClient {

    get(url) {
        return request('http://localhost:8080' + url);
    }

}
