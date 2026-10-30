# Resmi Ubuntu ve OpenJDK ortamı (x86_64 mimarisiyle)
FROM --platform=linux/amd64 eclipse-temurin:17-jdk

# Çalışma dizinini ayarla
WORKDIR /app

# Proje dosyalarını içeri kopyala
COPY src/ ./src/
COPY input/ ./input/
COPY output/ ./output/

# Java kodlarını derle
RUN javac src/*.java

# Varsayılan komut: test döngüsünü çalıştır
CMD ["bash", "-c", "for i in 1 2 3; do echo '===== TEST '$i' ====='; time java -cp src Main input/input$i.txt out$i.txt; diff -w out$i.txt output/output$i.txt && echo 'SONUC: BIREBIR AYNI' || echo 'SONUC: FARK VAR'; done"]