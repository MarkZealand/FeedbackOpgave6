# Feedbackopgave 6 - Starwars klasser med arv + database forbindelse
- Mark

# Hvad projektet går ud på
Projektet er et simpelt system til at håndtere Star Wars karakterer. Systemet henter karakterer fra en  atabase og opretter (`Jedi`, `Sith`, `CloneTrooper`, `Droid`), som derefter vises og udfører deres handling via polymorfi.

## Hvordan programmet køres
Programmet køres gennem IntelliJ ved at trykke på "RUN" eller Shift+F10. 
Det kræver, at MySQL kører lokalt med en `characters` database og en `character_classes` tabel, samt at MySQL JDBC-driveren er tilføjet projektet.

## Hvordan programmet er opdelt i metoder
**Character** (abstrakt)
- Konstruktør
- `action()` – abstrakt handling, overrides i subklasserne
- `showInfo()` – viser navn, alder og level

**Jedi, Sith, CloneTrooper, Droid**
- Konstruktør
- `action()` – hver klasse har sin egen implementation

**Db**
- `get()` – returnerer en `Connection` til databasen

**Main**
- `main(String[] args)` – starter programmet
- `dbQuery(List<Character>)` – henter data fra databasen
- `testConnection()` – tester forbindelsen og udskriver driverinfo
- `processResults(ResultSet, List<Character>)` – læser rækker og kalder `addClassesToList`
- `addClassesToList(...)` – opretter det korrekte objekt ud fra `class_name`
- `loopThroughCharacterObjects(List<Character>)` – kalder `showInfo()` og `action()` på hver karakter

## Hvorfor denne opdeling
Hver klasse har ét ansvar: `Db` står for forbindelsen, `Character` og subklasserne for data og adfærd, og `Main` for flowet. Det gør koden nem at læse og udvide.

## Eksempel på en metode med parameter
`addClassesToList(...)` tager `className`, `characterName`, `characterAge`, `characterLevel` og en `List<Character>` som parametre og tilføjer det korrekte objekt til listen.

## Eksempel på en metode med returværdi
`Db.get()` returnerer en `Connection`, som bruges i `try-with-resources` i `Main`. `processResults(...)` returnerer ikke noget, men fylder listen via parameteren.

## Hvilke løkker bruges, og hvorfor
Vi bruger en `while (resultSet.next())` til at gennemgå alle rækker fra databasen, og en `for-each` i `loopThroughCharacterObjects` til at kalde `showInfo()` og `action()` på hver karakter. `try-with-resources` sørger for, at `Connection`, `Statement` og `ResultSet` lukkes automatisk.

## Ting vi har testet
* At forbindelsen til databasen virker, og at driverinfo udskrives
* At alle karakterer hentes og får den korrekte subklasse
* At polymorfi virker da `action()` giver forskelligt output pr. klasse
* At ukendte `class_name` værdier håndteres med en besked

## En fejl eller udfordring vi stødte på
Det var lidt af en udfordring at skifte Build System fra IntelliJ til Maven, men det lykkedes til sidst. I fremtiden er det nok hurtigere bare at oprette et nyt projekt og kopiere koden over.

Stødte også på en fejl, hvor `action()` ikke blev kaldt korrekt, men det blev løst ved at sikre, at alle subklasser overskrev metoden.

## Hvad vi har lært om klasser
Klasser kan bruges til mange ting. For eksempel var DB klassen i projektet du har vist i dit eksempel en fed løsning i stedet for at skulle oprette en ny forbindelse hver gang. 

## MySQL query

```
create database characters;

use characters;

create table character_classes (
id INT AUTO_INCREMENT PRIMARY KEY,
class_name VARCHAR(50) NOT NULL,
character_name VARCHAR(50) NOT NULL,
character_age INT,
character_level INT
);

INSERT INTO character_classes (class_name, character_name, character_age, character_level) VALUES
('CloneTrooper', 'CT-7567', 35, 10),
('CloneTrooper', 'CC-2224', 32, 15),
('CloneTrooper', 'CT-5555', 28, 12),
('Jedi', 'Luke Skywalker', 25, 70),
('Jedi', 'Obi-Wan Kenobi', 38, 85),
('Jedi', 'Yoda', 900, 100),
('Droid', 'R2-D2', 10, 40),
('Droid', 'C-3PO', 12, 20),
('Droid', 'BB-8', 6, 25),
('Sith', 'Darth Vader', 45, 95),
('Sith', 'Darth Maul', 35, 80),
('Sith', 'Count Dooku', 83, 88);
```