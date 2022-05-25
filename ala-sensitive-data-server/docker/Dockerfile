FROM ubuntu:focal

ARG ENV

ARG DEBIAN_FRONTEND=noninteractive

# Install initial dependencies
RUN apt-get -y update && \
        apt-get install -y -q add-apt-key software-properties-common

ARG APT_KEY_DONT_WARN_ON_DANGEROUS_USAGE=DontWarn

# Add the apt.gbif.es repo and gpg key
RUN echo "deb [arch=amd64] https://apt.gbif.es/ bionic main" > /etc/apt/sources.list.d/ala.list && \
        apt-key adv --keyserver keyserver.ubuntu.com --recv-keys F697D8D2ADB9E24A

# Install main dependencies
RUN apt-get install -y -q openjdk-8-jdk tar curl

RUN echo "ala-sensitive-data-service ala-sensitive-data-service/sds-url string https://sds.ala.org.au" | debconf-set-selections
RUN echo "ala-sensitive-data-service ala-sensitive-data-service/spatial-url string https://spatial.ala.org.au" | debconf-set-selections
RUN echo "ala-sensitive-data-service ala-sensitive-data-service/layers-url string https://archives.ala.org.au/archives/layers/sds-layers.tgz" | debconf-set-selections
RUN echo "ala-sensitive-data-service ala-sensitive-data-service/source string https://archives.ala.org.au/archives/nameindexes/latest/namematching-20200214.tgz" | debconf-set-selections
RUN echo "ala-sensitive-data-service ala-sensitive-data-service/sha1 string f9e42325db8d41ca0c8afeb6aeadf670978a13ac" | debconf-set-selections

# Update the next commented date to force a new docker hub build with an updated deb version
RUN apt-get -y update && \
        apt-get install -y -q ala-namematching-service ala-sensitive-data-service && \
        rm -rf /var/lib/apt/lists/* && \
        apt-get clean # 202111151821

EXPOSE 9189
EXPOSE 9190

CMD ["java", "-jar", "/opt/atlas/ala-sensitive-data-service/ala-sensitive-data-server.jar", "server", "/data/ala-sensitive-data-service/config/config.yml"]
