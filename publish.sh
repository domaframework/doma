#!/bin/bash

if [[ $TRAVIS_PULL_REQUEST == "false" ]] && [[ $TRAVIS_BRANCH == "master" ]]; then
    echo $PASSPHRASE | gpg --output $SIGNING_SECRETKEYRINGFILE --batch --passphrase-fd 0 --decrypt encrypted-maven.gpg
    ./gradlew uploadArchives -s \
        -PsonatypeUsername=$SONATYPE_USERNAME \
        -PsonatypePassword=$SONATYPE_PASSWORD \
        -Psigning.keyId=$SIGNING_KEYID \
        -Psigning.password=$SIGNING_PASSWORD \
        -Psigning.secretKeyRingFile=$SIGNING_SECRETKEYRINGFILE
fi
