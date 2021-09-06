FROM maven:3.6.3-openjdk-15 as builder

ENV APP_SRC_FOLDER=/usr/src/app
RUN mkdir -p "${APP_SRC_FOLDER}"

COPY src "${APP_SRC_FOLDER}/src"
COPY pom.xml "${APP_SRC_FOLDER}"
RUN mvn -q -B -f "${APP_SRC_FOLDER}/pom.xml" clean install

FROM adoptopenjdk/openjdk15:jre-15.0.2_7-alpine

ENV HOME /home/kvendingoldo
ENV USER kvendingoldo

RUN mkdir -p "${HOME}/app"
COPY --from=builder /usr/src/app/target/ami-*.jar "${HOME}/app/app.jar"
WORKDIR "${HOME}"

ENTRYPOINT ["java", "-jar", "app/app.jar"]



