FROM hseeberger/scala-sbt:11.0.7_1.3.13_2.12.12
WORKDIR /SE-Boger
RUN apt-get update && \ apt-get install -y sbt libxrender1 libxtst6 libxi6 WORKDIR /SE-Boger
ADD . /SE-Boger
CMD sbt run
