## Intro

## Prerequisites

```
sudo apt install debhelper
sudo apt install devscripts
```

## Build

You can generate a non-signed debian package via:

```bash
debuild -us -uc -b
```
in the parent of this directory. This will generate the deb file in the parent directory of this repository.

You can increase changelog version and comments with `dch` utility, like with `dch -i` that increase minor version.

Add to your .gitignore:
```
*/*.debhelper
*/*.debhelper.log
*.buildinfo
*.substvars
debian/files
debian/ala-sensitive-data-service
```

## Using systemd?

Just create here your systemad service like
debian/ala-sensitive-data-service.service
debian/ala-sensitive-data-service.conf

see ala-images debian package for details

## Looking for inspiration?

You can see [tomcat7-examples package source](https://salsa.debian.org/java-team/tomcat7/tree/master/debian) for inspiration of tomcat7 packages and also about how to create multiple debian packages from a same source repository.

Also `dbconfig-common` package have some samples in `/usr/share/doc/dbconfig-common/examples/` for mysql and postgresql debian configuration tasks for packages.

## Testing

You can test the generated package without install it with `piuparts` like:

```bash
sudo piuparts -D ubuntu -d xenial -d bionic ../ala-sensitive-data-service_1.1.1_all.deb
```
in this way you can also test it in different releases.

Read `/usr/share/doc/piuparts/README.*` for more usage samples.

## No interactive install

You can preseed this package with something similar to (if this package uses mysql):

```bash
echo "ala-sensitive-data-service ala-sensitive-data-service/mysql/admin-pass password $DB_ROOT_PWD" | debconf-set-selections && \
echo "ala-sensitive-data-service ala-sensitive-data-service/dbconfig-install boolean true" | debconf-set-selections && \
echo "ala-sensitive-data-service ala-sensitive-data-service/dbconfig-upgrade boolean true" | debconf-set-selections

cat > /etc/dbconfig-common/ala-sensitive-data-service.conf <<EOF
dbc_dbname='sensitive-data-service'
dbc_dbuser='sensitive-data-service'
dbc_dbpass='password'
EOF

export DEBCONF_FRONTEND=noninteractive
apt-get install -yq --force-yes ala-sensitive-data-service
```

Also you can install `dbconfig-no-thanks` to avoid db questions.
