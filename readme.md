### Hvorfor er polymorfi smart, når du arbejder med lister af objekter?

### Hvad hvis du havde 20 forskellige karakterklasser – ville du så skrive 20 forskellige loops?






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