import {D3Fractalizer} from './3f/D3Fractalizer';
import './style/default.css';
import {HttpClient} from "./http/HttpClient";

let client = new HttpClient();

client.get('/fitnessfunction').then((data) => {
    new D3Fractalizer(JSON.parse(data));
});
