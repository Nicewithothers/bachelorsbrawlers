# Bachelors Brawlers

# [Deployolt app](https://bachelors-brawlers.vercel.app/) 

# [Figjam](https://www.figma.com/file/T8vuk0yn9QViKt3rsscdAa/SZTE-S%26F?type=whiteboard&node-id=0-1) 
# [Figma](https://www.figma.com/file/SQiDAWsI3uVh048Ie3vKzC/SZTE-S%26F?type=design&node-id=0-1&mode=design) 

## Docker telepítése
1. Docker letöltése: https://www.docker.com/products/docker-desktop/
2. Telepítsd a dockert, lesz 2 checkbox a WSL erősen ajánlott
3. Indítsd újra a számítógéped
4. Elindítod a dockert és a projekt root mappából kiadod a docker compose up parancsot a terminálból
5. Ha esetleg pgadmin-t szeretnél használni a csatlakozási paraméterek a docker-compose.yml-ben találhatóak
6. Előfordulhat, hogy conflictol a port, így a docker-compose.yml-ben és a backend/src/main/resources/application.properties-ben a datasource url-ben is át kell írni a portot (ha van default postgres a gépen, akkor próbáld pl az 5431 portot)

## Prettier használata
A prettier alapvető használatához az **npx** kulcsszó fog kelleni, a lokális futtatás miatt.\
A parancs így fog kinézni:
```npx prettier [kapcsoló] [fájl(ok)]```\
Mi nekünk alapvetően 2 kapcsolót kell csak használni.
- --check: Ez a kapcsoló fogja nekünk ellenőrizni, hogy az adott fájl(ok) megfelelően vannak-e formázva.
- --write: Ez a kapcsoló pedig azért felelős, hogy a prettier segítségével formázzunk.

**Kiemelten fontos a használatuk, mivel a pipeline nem fogja továbbengedni a rosszul formázott fájlok
miatt a jobot, ezért figyeljetek rá!**
