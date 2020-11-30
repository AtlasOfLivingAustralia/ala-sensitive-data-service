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
1. Start the application with `java -jar target/ala-sensitive-data-server-1.1-SNAPSHOT.jar server config.yml`
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
| conservation | | | Conservation testing configuration | | |
| | index | | The path of the index directory | | `/data/lucene/namematching` |
| | speciesUrl | | URL of the sensitive species configuration (XML) |  | `https://sds.ala.org.au/sensitive-species-data.xml` | 
| | zonesUrl | | URL of the sensitivity zones configuration (XML) |  | `https://sds.ala.org.au/sensitivity-zones.xml` | 
| | categoriesUrl | | URL of the sensitivity categories configuration (XML) |  | `https://sds.ala.org.au/sensitivity-categories.xml` | 
| | layersUrl | | URL of the geospatial layers configuration (JSON) |  | `https://sds.ala.org.au/ws/layers` | 
| | layers | | A list of layer identifiers if not configured via URL  | - "cl22" - "cl23"  |  | 
| | layersServiceUrl | | URL of web service for layer tests |  | `https://spatial.ala.org.au/layers-service` | 
| | cache | | Cache configuration | | |
| | | entryCapacity | Number of cache entries | 10000 | |
| | | enableJmx | Enable JMX monitoring | true | |
| | generalisations | | A list of generalisations to apply to sensitive data | | Defaults to a list of common Darwin Core location and date fields |
| | | action | The generalisation type. One of clear, retain, latLong, message, add | | |
| | | field | The field to generalise | dwc:locality | |
| | | * | More generalisation options, see below | | |

### Generalisations

Fields can be either full URIS, eg `	http://rs.tdwg.org/dwc/terms/sex` 
prefixed names eg `dwc:sex` or plain terms such as `sex`.

| Action | Parameter | Description | Example |
| --- | --- | --- | --- |
| add | | Add an integer amount to a field's value | | 
| | field | The field to add to | dwc:coordinateUncertaintyInMeters |
| | retainUnparsable | If the fields contents can't be parsed as an integer, leave it alone. Otherwise treat it as 0 and add to that | true |
| | useSensitivity | Use the value of the associate sensitivity instance generalisation (eg 10km) in meters to add to the value | true |
| | add | A plain amount to add to the value | 100 |
| clear | | Clear any existing value, setting it to null | |
| | field | The field to clear | dwc:locality |
| latLong | Apply the generalisation in a sensitivity instance to th latitude and longitude | |
| | latitudeField | The latitude field | dwc:decimalLatitude |
| | longitudeField | The longitude field | dwc:decimalLongitude |
| message | Format and add a message to the field | |
| | field | The field to add to | dwc:dataGeneralisations |
| | message | The message to add | Sensitive in {2} |
| | append | Append to, rather than replace, existing data | true |
| retain | | Retain (include unaltered) a field. This action can be used to ensure information is passed but not altered | |
| | field | The field to retain | dwc:scientificName |

#### LatLong Generalisation

The latLong generalistion is used to obscure the position of a record.
A location is passed in as a lat/long pair and generalised as a pair.
Generalisations are usually given as distances (eg. 20km) and the amount a
lonmgitude has to change to generalise to that distance depends on the latitude.

#### Message Generalisation

Messages use the `java.text.MessageFormat` format.
There are five parameters that can be inserted into the message.

 * {0} Authority name
 * {1} Category name (value)
 * {2} Zone name
 * {3} Reason string
 * {4} Remarks string
 
#### Missing Generalisations

Grid references, both UK/Irish grid references and
UTM Easting and Northing are not yet generalised in the
same way that lat/long are.

## Building the docker image

Change directory to the `ala-sensitive-data-server` module.

```shell script
docker build -f docker/Dockerfile . -t ala-sensitive-data-service:v20200214-2
```

to use ALA namematching index and for use the GBIF backbone:

```shell script
docker build -f docker/Dockerfile . -t  ala-sensitive-data-service:v20200214-2 --build-arg ENV=gbif-backbone
```
