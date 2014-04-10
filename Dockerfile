FROM ubuntu:12.04
MAINTAINER Toshihiro Nakamura toshihiro.nakamura@gmail.com

# Install prerequisites
RUN apt-get update
RUN apt-get install -y python-software-properties software-properties-common

# Install java8
RUN add-apt-repository -y ppa:webupd8team/java
RUN apt-get update
RUN echo debconf shared/accepted-oracle-license-v1-1 select true | debconf-set-selections
RUN echo debconf shared/accepted-oracle-license-v1-1 seen true | debconf-set-selections
RUN apt-get install -y oracle-java8-installer

# Install tools
RUN apt-get install -y git

# End installation
RUN apt-get clean

# Copy project
ADD . /src

# Build project
CMD ["build"]
ENTRYPOINT ["/src/gradlew", "-b", "/src/build.gradle"]
