import {D3Fractalizer} from './3f/D3Fractalizer';
import './style/default.css';
import data from './examples/webapp.json';

let fractalizer = new D3Fractalizer(data);

setTimeout(() => {
    data.children[1].okay = false;
    data.children[0].okay = false;
    data.children[0].children[0].okay = false;

    console.log(data);

    fractalizer.updateWithData(data);
}, 5000);
