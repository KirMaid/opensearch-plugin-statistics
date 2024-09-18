FROM opensearchproject/opensearch:3.0.0-SNAPSHOT

RUN apt-get update && apt-get install -y \
    openjdk-11-jdk \
    gradle \
    && rm -rf /var/lib/apt/lists/*

COPY ./opensearch-plugin-statistics /usr/src/opensearch-plugin-statistics

WORKDIR /usr/src/opensearch-plugin-statistics

ARG OPENSEARCH_VERSION=3.0.0-SNAPSHOT

RUN gradle wrapper

RUN ./gradlew dependencies --refresh-dependencies

RUN ./gradlew clean check integTest -Dopensearch.version=$OPENSEARCH_VERSION

RUN ./gradlew bundlePlugin -Dopensearch.version=$OPENSEARCH_VERSION

RUN /usr/share/opensearch/bin/opensearch-plugin install file:///usr/src/opensearch-plugin/build/distributions/statistics-*.zip

EXPOSE 9200 9600

CMD ["/usr/share/opensearch/opensearch-docker-entrypoint.sh"]