# regions-locations-task

## Project Description

This is a solution for the test assignment for Scala TravelTime internship by Arnas Bradauskas. Given an input of two files (regions.json and locations.json) the program matches all locations to the regions they are located in.

## Prerequisites
You need to have Scala and SBT installed on your machine to run this project. You can download them from the following websites:

* Scala: https://www.scala-lang.org/download/
* SBT: https://www.scala-sbt.org/download.html

## Installation

To install traveltime-internship-task, follow these steps:

Windows/Mac/Linux:

```bash
git clone https://github.com/arnasbr/regions-locations-task.git
cd regions-locations-task
```

## Usage
To run the application, use the this command:

```bash
sbt "run match --locations <pathToLocationsFile> --regions <pathToRegionsFile> --output <pathToOutputFile>"
```

## Docker
It is also possible to run the application using docker

```bash
docker pull arnasbr/regions-locations-task
docker run -it --rm -u 1000 -v path/to/inputDir:/app/input -v path/to/outputDir:/app/output arnasbr/regions-locations-task match --locations /app/input/locations.json --regions /app/input/regions.json --output /app/output/results.json
```

## Input file examples
[Locations](https://github.com/traveltime-dev/internship-task/blob/master/input/locations.json)

[Regions](https://github.com/traveltime-dev/internship-task/blob/master/input/regions.json)

## Output example
[Results](https://github.com/traveltime-dev/internship-task/blob/master/output/results.json)
