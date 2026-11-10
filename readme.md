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


###

Kodefeedback – Infinite Loopers (Mark)

Hej Mark

Du har lavet en rigtig fin løsning, og det er positivt, at du allerede eksperimenterer med MySQL og JDBC, selvom vi endnu ikke har gennemgået databaseprogrammering i Java.

Du viser en god forståelse af arv og polymorfi med Character, Jedi, Sith, Droid og CloneTrooper. Du bruger også @Override, List<Character> og try-with-resources fornuftigt.

Min største kritik handler om arkitekturen. Du har fået programmet til at fungere, men nu skal du arbejde med, hvordan koden organiseres, så den bliver nemmere at vedligeholde, teste og udvide – vi kommer ind på der senere i undervinsningen, men….

1. Arkitektur – Hver klasse skal have et ansvar

I øjeblikket har din Main-klasse for mange opgaver. Den håndterer både SQL, databaseforbindelser, ResultSet, oprettelse af objekter og programflow.

Det virker, men hvad sker der, hvis du senere vil tilføje en brugergrænseflade eller ændre databasen?

Jeg vil anbefale, at du undersøger en simpel lagdelt arkitektur:

Main -> CharacterService -> CharacterRepository -> Db -> MySQL

Dine modelklasser (Character, Jedi, Sith osv.) bruges af både service- og repositorylaget, men skal ikke kende noget til databasen.

Hvad skal de forskellige lag gøre?

    Main (præsentation/programflow): Starter programmet, kalder serviceklassen og udskriver resultater. Den skal ikke indeholde SQL.
    CharacterService (forretningslogik): Håndterer programmets funktionalitet, eksempelvis at hente karakterer, filtrere dem eller gennemføre en kamp. Den kalder repositorylaget, når der skal hentes data.
    CharacterRepository (dataadgang): Ved, hvordan karakterer hentes fra databasen. Den udfører SQL og omsætter ResultSet til Java-objekter.
    Db (databaseforbindelse): Opretter forbindelsen til MySQL. Klassen skal ikke kende programmets karakterer.
    Modelklasser: Beskriver karakterernes egenskaber og adfærd. Jedi skal eksempelvis vide, hvordan en Jedi udfører action(), men ikke hvordan en Jedi hentes fra MySQL.

Det kaldes Separation of Concerns – at adskille ansvarsområder.

2. CharacterRepository – Flyt SQL ud af Main

Du har allerede oprettet en Db-klasse, hvilket er et godt første skridt.

Men din dbQuery() og processResults() ligger stadig i Main.

Jeg vil foreslå en CharacterRepository, som eksempelvis har metoden:

public List<Character> findAll() throws SQLException



Metoden skal:

    Hente en databaseforbindelse via Db.
    Udføre SQL-forespørgslen.
    Omsætte rækker fra ResultSet til Character-objekter.
    Returnere en liste med karaktererne.

Det betyder, at resten af programmet ikke skal kende noget til SQL eller ResultSet.

Overvej: Hvis du senere vil gemme karaktererne i en JSON-fil i stedet for MySQL, hvilke klasser skal så ændres?

3. CharacterService – Hvorfor endnu et lag?

Når du har et repository, kan du oprette en CharacterService.

Den kunne eksempelvis indeholde:

public List<Character> getAllCharacters() {
return repository.findAll();
}



Dette er kun princippet; du skal selvfølgelig også tage stilling til fejlhåndtering.

Lige nu gør metoden ikke meget andet end at videresende et kald. Derfor er servicelaget ikke strengt nødvendigt i dit nuværende program.

Men forestil dig, at du senere vil:

    Hente alle Jedi-karakterer.
    Kontrollere, at en karakter har et gyldigt level.
    Beregne resultatet af en kamp mellem to karakterer.

Så er CharacterService et naturligt sted til denne logik.

Det vigtige er, at du ikke opretter ekstra lag bare for at have dem. Du skal kunne forklare, hvilket ansvar hvert lag har.

4. Modelklasser og indkapsling

I Character.java har du:

String name;
int age;
int level;



Du bør overveje at gøre felterne private, så andre klasser ikke kan ændre objektets tilstand direkte.

Du har også gjort Character abstrakt, men metoden action() har stadig en standardimplementering.

Når dine underklasser alle implementerer deres egen handling, kunne du i stedet skrive:

public abstract void action();

Så bliver underklasserne tvunget til at implementere metoden.

Det giver en tydeligere kontrakt mellem superklassen og dens underklasser.

5. Best practices og fejlhåndtering

Der er yderligere nogle ting, jeg gerne vil have dig til at overveje:

    Databasekonfiguration: Du har hardcodet brugernavn og password i Db. Undersøg, hvordan du kan anvende miljøvariabler i stedet.
    Databaseforbindelser: testConnection() åbner en ekstra forbindelse, hver gang dbQuery() kaldes. Er det nødvendigt?
    Fejlhåndtering: Hvis databaseforespørgslen fejler, skriver programmet en fejl og fortsætter med en tom liste. Hvordan kan du skelne mellem en tom database og en databasefejl?
    Testbarhed: Hvordan vil du teste oprettelsen af Jedi og Sith uden at have MySQL kørende?
    Single Responsibility Principle: Din addClassesToList() både opretter objekter og tilføjer dem til en liste. Kunne objektoprettelsen have sin egen metode?

6. Det vil jeg gerne have dig til at arbejde videre med

Du skal ikke tilføje flere funktioner endnu. Brug i stedet tiden på at forbedre den kode, du allerede har.

Prioritet 1 – Arkitektur

Flyt SQL og ResultSet-behandling fra Main til CharacterRepository. Undersøg derefter, om en CharacterService giver mening, og hvilken funktionalitet den skal have.

Prioritet 2 – OOP og indkapsling

Forbedr Character og underklasserne. Overvej adgangsmodifikatorer, abstrakte metoder og ansvarsfordeling.

Prioritet 3 – Test og fejlhåndtering

Lav mindst én automatiseret test, der ikke kræver en database, og forbedr håndteringen af databasefejl.

Spørgsmål til refleksion

    Hvorfor bør Main ikke indeholde SQL, og hvornår giver det mening at anvende et servicelag?
    Hvad er forskellen på en modelklasse, en serviceklasse og et repository?
    Hvis du senere laver en JavaFX-brugergrænseflade, hvilke dele af din nuværende kode vil du så kunne genbruge uden ændringer?

Samlet vurdering

Du er fagligt foran på flere områder, Mark. Derfor vil jeg også stille større krav til din arkitektur og kodekvalitet.

Du har allerede vist, at du kan få Java og MySQL til at arbejde sammen. Den næste udfordring er at bygge et system, hvor de enkelte dele har tydelige ansvarsområder og kan udvikles og testes uafhængigt af hinanden.
