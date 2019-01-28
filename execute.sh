
params=""
params="$params -Xms24m -Xmx120m -XX:MaxMetaspaceSize=70m "
params="$params -XX:MaxHeapFreeRatio=2 -XX:MinHeapFreeRatio=1 "
#params="$params -XX:+UnlockCommercialFeatures "
#params="$params -XX:+FlightRecorder "
# -XX:StartFlightRecording=duration=10s,filename=myrecording.jfr 

java $params -jar ./target/spring_console_1.jar -h
