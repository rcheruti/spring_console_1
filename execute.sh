
params=""
params="$params -Xms24m -Xmx120m -XX:MaxMetaspaceSize=70m "
params="$params -XX:MaxHeapFreeRatio=2 -XX:MinHeapFreeRatio=1 "
#params="$params -XX:+UnlockCommercialFeatures "
#params="$params -XX:+FlightRecorder "
params="$params -XX:+UseG1GC "
params="$params -XX:ReservedCodeCacheSize=8m "
params="$params -XX:CompileThreshold=250 "
# -XX:StartFlightRecording=duration=10s,filename=myrecording.jfr 
# -XX:CompileThreshold=500 # Qtd de chamadas de um método para que seja compilado
# -XX:ReservedCodeCacheSize=8m # Limite de memoria do Cache de compilados
# -XX:+UseG1GC

java $params -jar ./target/spring_console_1.jar -h

# Opções JVM:
# https://www.oracle.com/technetwork/java/javase/tech/vmoptions-jsp-140102.html
