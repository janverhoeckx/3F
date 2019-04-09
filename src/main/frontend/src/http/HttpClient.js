import * as request from "request-promise-native";

export class HttpClient {

    get(url) {
        return request(url);
    }

}
