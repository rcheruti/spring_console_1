@echo off

set "params= "
set "params=%params% -Xms24m -Xmx120m -XX:MaxMetaspaceSize=70m "
set "params=%params% -XX:MaxHeapFreeRatio=2 -XX:MinHeapFreeRatio=1 "
set "params=%params% -XX:+UnlockCommercialFeatures "
set "params=%params% -XX:+FlightRecorder "
rem -XX:StartFlightRecording=duration=10s,filename=myrecording.jfr 

start java %params% -jar .\target\spring_console_1.jar
