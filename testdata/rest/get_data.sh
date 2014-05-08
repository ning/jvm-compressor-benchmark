#!/bin/bash
#Fetch sample REST data for use in benchmarking

JSON='-H Accept:application/json'
XML='-H Accept:application/xml'

#Canadian seismography API: http://data.gc.ca/data/en/dataset/38f2f053-f2c2-4370-8536-cd0cabff5b2e
curl $JSON -o ./seismo-index.json "http://www.earthquakescanada.nrcan.gc.ca/api/seismic-stations/index.json"
curl $XML -o ./seismo-index.xml "http://www.earthquakescanada.nrcan.gc.ca/api/seismic-stations/index.xml"
curl $JSON -o seismo-station.json "http://www.earthquakescanada.nrcan.gc.ca/api/seismic-stations/TOBO.json"
curl $XML -o seismo-station.xml "http://www.earthquakescanada.nrcan.gc.ca/api/seismic-stations/TOBO.xml"

#USA Jobs data https://data.usajobs.gov/Rest
curl $JSON -o usajobs-small.json "https://data.usajobs.gov/api/jobs?series=2210"
curl $XML -o usajobs-small.xml "https://data.usajobs.gov/api/jobs?series=2210"
curl $JSON -o usajobs-big.json "https://data.usajobs.gov/api/jobs?country=United%20States&NumberOfJobs=250"
curl $XML -o usajobs-big.xml "https://data.usajobs.gov/api/jobs?country=United%20States&NumberOfJobs=250"

#Angellist REST APIs: https://angel.co/api/spec/jobs#GET_jobs
#Note: test if gives XML
curl $JSON -o angel-startups.json "https://api.angel.co/1/startups/6702"
curl $JSON -o angel-status.json "https://api.angel.co/1/status_updates?startup_id=6702"
curl $JSON -o angel-search.json "https://api.angel.co/1/search?query=Technology&type=Startup"
curl $JSON -o angel-big.json "https://api.angel.co/1/jobs"

#Nasa Open Data http://data.nasa.gov/api-info/
curl $JSON -o nasa-category.json "http://data.nasa.gov/api/get_category_index/"
curl $JSON -o nasa-tag.json "http://data.nasa.gov/api/get_tag_datasets/?slug=apollo&slug=imagery"
curl $JSON -o nasa-big.json "http://data.nasa.gov/api/get_recent_datasets/?count=100"

#Open Weather Data: http://openweathermap.org/API
curl $JSON -o weather-small.json "http://api.openweathermap.org/data/2.5/forecast/daily?id=524901&lang=zh_cn"
curl $XML -o weather-small.xml "http://api.openweathermap.org/data/2.5/forecast/daily?id=524901&lang=zh_cn&mode=xml"
curl $JSON -o weather-big.json "http://api.openweathermap.org/data/2.5/forecast?id=524901"
curl $XML -o weather-big.xml "http://api.openweathermap.org/data/2.5/forecast?id=524901&mode=xml"
