# ALA Sensitive Data Service [![Build Status](https://travis-ci.org/AtlasOfLivingAustralia/ala-sensitive-data-service.svg?branch=master)](https://travis-ci.org/AtlasOfLivingAustralia/ala-sensitive-data-service)

This priovides a set of web services for sensitive data evaluation, using the `sds` library.
It consists of three components. all with maven groupId `au.org.ala.sds`:

* `ala-sensitive-data-core` A core library containing common objects
* `ala-sensitive-data-client` A client library that can be linked into other applications and which accesses the web services
* `ala-sensitive-data-server` A server application that can be used for sensitive data searches

## How to start the ALASensitiveDataService application

1. Run `mvn clean install` to build your application
1. Download a pre-built name matching index (e.g https://archives.ala.org.au/archives/nameindexes/latest/namematching-20200214.tgz), and untar in `/data/lucene` This will create a `/data/lucene/namematching-20200214` directory.
1. cd to the `server` subdirectory
1. Start the application with `java -jar target/ala-sensitive-data-server-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:9190`
1. Test with `http://localhost:9189/api/isSensitive?scientificName=Caladenia+actensis`. The response should look similar to:

```json
true
```

### Web Services

To see complete documentation of the webservices available enter url `http://localhost:9189`

### Health Check

To see your applications health enter url `http://localhost:9190/healthcheck`

### Configuration

The senstivie data service uses a YAML configuration file with a number of possible entries.
Most of these entries have suitable defaults.

| | | | Description | Example | Default |
| --- | --- | --- | --- | --- | --- |
| logging | | | Logging configuration, see https://www.dropwizard.io/en/latest/manual/configuration.html for documentation | | |
| server | | | Server configuration, see https://www.dropwizard.io/en/latest/manual/configuration.html for documentation | | |
| search | | | Search configuration | | |
| | index | | The path of the index directory | | `/data/lucene/namematching` |
| | speciesUrl | | URL of the sensitive species configuration (XML) |  | `https://sds.ala.org.au/sensitive-species-data.xml` | 
| | zonesUrl | | URL of the sensitivity zones configuration (XML) |  | `https://sds.ala.org.au/sensitivity-zones.xml` | 
| | categoriesUrl | | URL of the sensitivity categories configuration (XML) |  | `https://sds.ala.org.au/sensitivity-categories.xml` | 
| | layersUrl | | URL of the geospatial layers configuration (JSON) |  | `https://sds.ala.org.au/ws/layers` | 
| | layers | | A list of layer identifiers if not configured via URL  | - "cl22" - "cl23"  |  | 
| | layersServiceUrl | | URL of web service for layer tests |  | `https://spatial.ala.org.au/layers-service` | 


## Building the docker image

Change directory to the `ala-sensitive-data-server` module.

```shell script
docker build -f docker/Dockerfile . -t ala-sensitive-data-service:v20200214-1
```

to use ALA namematching index and for use the GBIF backbone:

```shell script
docker build -f docker/Dockerfile . -t  ala-sensitive-data-service:v20200214-1 --build-arg ENV=gbif-backbone
```
