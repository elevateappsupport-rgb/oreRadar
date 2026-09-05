# OreRadar

Ein Client-Side Fabric-Mod (Singleplayer geeignet): öffnet per Tastendruck
ein Suchmenü mit Blöcken (Erze, Ancient Debris, Gravel, Spawner, Vault, ...)
und zeigt eine lila Linie vom Spieler zum nächstgelegenen gefundenen Block.

## Fertige Jar bauen (ohne IntelliJ)
1. Dieses komplette Projekt in ein GitHub-Repository hochladen (alle Dateien
   inkl. .github-Ordner, gradlew, gradle-Ordner, build.gradle usw.)
2. Im Repo oben auf "Actions" klicken -> Build läuft automatisch
3. Nach ca. 2-4 Minuten (grünes Häkchen) auf den Lauf klicken -> unter
   "Artifacts" liegt die fertige .jar zum Download

## Standard-Taste
B (änderbar unter Optionen -> Steuerung -> "OreRadar")

## Einschränkung
Es werden nur Blöcke in geladenen Chunks gefunden (also im Umkreis, den dein
Spiel gerade simuliert/rendert). Für eine Suche über die komplette
Weltdatei (auch unbesuchte Bereiche) müsste man die .mca-Dateien der Welt
direkt einlesen - ein guter, aber deutlich fortgeschrittenerer nächster
Schritt.

## Config
`.minecraft/config/oreradar.json`:
```json
{
  "searchRadius": 24,
  "searchIntervalTicks": 20
}
```
