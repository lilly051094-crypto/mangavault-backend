FROM maven:3-eclipse-temurin-21-alpine AS build

WORKDIR /manga-reader-backend

COPY ./pom.xml /manga-reader-backend
COPY ./ /manga-reader-backend

RUN mvn dependency:go-offline || echo "don't care if fail, this is a cache"
RUN mvn dependency:resolve-plugins || echo "don't care if fail, this is a cache"
RUN mvn install
RUN mvn package -DskipTests


FROM eclipse-temurin:21-jdk

COPY --from=build /manga-reader-backend/target/*.jar /usr/src/app/manga-reader-backend.jar
WORKDIR /usr/src/app

ENV JAVA_TOOL_OPTIONS="-agentlib:jdwp=transport=dt_socket,address=*:5005,server=y,suspend=n"
EXPOSE 5005

CMD java $JAVA_OPTS -jar manga-reader-backend.jar
