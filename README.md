# Dimension Lock (Fabric, Minecraft 26.2)

Command: /dimension <dimension> lock|unlock   (dimension = nether | end)
Nur fuer Operatoren (Gamemaster). Zustand wird in config/dimensionlock.json gespeichert.

Bauen (JDK 25 noetig):
  gradle wrapper      (einmalig, falls kein gradlew vorhanden)
  ./gradlew build
Jar: build/libs/dimensionlock-1.0.0+26.2.jar  -> in den mods-Ordner (mit Fabric API)
