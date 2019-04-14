[![Build Status](https://travis-ci.org/janverhoeckx/3F.svg?branch=master)](https://travis-ci.org/janverhoeckx/3F)

# 3F
Fitness Function Fractalizer

An architectural fitness function, as defined in [Building Evolutionary Architectures](https://www.thoughtworks.com/books/building-evolutionary-architectures), 
provides an objective integrity assessment of some architectural characteristics, which may encompass existing verification criteria, such as 
unit testing, metrics, monitors, and so on. The Fitness Function Fractalizer provides a hierarchical view of the current status of the fitness 
functions that apply to your software architecture.

The view can be fully specified by using a simple json file.

The demo application uses [FURPS](https://en.wikipedia.org/wiki/FURPS) as the first level of fitness functions. FURPS is an acronym representing a 
model for classifying software quality attributes.

![alt text](3f-screenshot.png "3F Screenshot")

## Supported checks
### Simple HTTP health
Check http response from a webservice, or website. Supports some basic result validation.

### InfluxDB
Execute an [InfluxDB](https://github.com/influxdata/influxdb) query with a single numeric value response and check the result based on simple an 
expression (e.g. <, >, ==, and !=)


## Front-end

The frontend is build with Webpack. To create the project the tool [create-webpack-application](https://www.npmjs.com/package/create-webpack-application) is used.
 
### Developing the front-end
The frontend is build in Javascript and located in the 'src/main/frontend' folder. To start a live webserver and live file reloading, run:

```bash
npm run start
```

To make a build and install it in the Spring boot static resources folder, run:
```bash
npm run build
```
