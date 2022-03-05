CREATE DATABASE punkt_szczepien;
DROP DATABASE punkt_szczepien;
USE punkt_szczepien;

CREATE TABLE dane_pacjenta(
	pesel VARCHAR(11) Primary key,
	imie VARCHAR(100),
	nazwisko VARCHAR(100),
    nr_tel CHAR(9),
    data_urodzenia DATE
);

CREATE TABLE szczepionki(
	id INT PRIMARY KEY,
    nazwa VARCHAR(100)
);

CREATE TABLE terminy(
	termin DATETIME
);
    
CREATE TABLE rejestracja_na_szczepienie(
	id_szczepienia INT AUTO_INCREMENT PRIMARY KEY,
    id_szczepionki INT,
    FOREIGN KEY (id_szczepionki) REFERENCES szczepionki(id),
    termin_szczepienia DATETIME,
    id_pacjenta VARCHAR(11) ,
    FOREIGN KEY (id_pacjenta) REFERENCES dane_pacjenta(pesel),
    zaszczepiony BOOLEAN
);

CREATE TABLE szczepienia_zrealizowane(
	id_pacjenta VARCHAR(11) ,
    FOREIGN KEY (id_pacjenta) REFERENCES dane_pacjenta(pesel),
	id_szczepionki INT,
    FOREIGN KEY (id_szczepionki) REFERENCES szczepionki(id),
    id_szczepienia INT,
    FOREIGN KEY (id_szczepienia) REFERENCES rejestracja_na_szczepienie(id_szczepienia),
    data_szczepienia DATETIME
);

CREATE VIEW pacjent as
select r.id_pacjenta as pesel, s.nazwa as typ_szczepienia, r. data_szczepienia as termin
from szczepienia_zrealizowane r
join szczepionki s on r.id_szczepionki = s.id;


CREATE VIEW punkt_szczepien as select
d.imie as imie, d.nazwisko as nazwisko, d.pesel as pesel, 
d.nr_tel, s.nazwa, r.termin_szczepienia, r.id_szczepionki as id_szczepionki, r.id_szczepienia as id_szczepienia
from rejestracja_na_szczepienie r join  dane_pacjenta d 
on r.id_pacjenta = d.pesel
join szczepionki s on r.id_szczepionki = s.id
WHERE r.zaszczepiony IS FALSE;


CREATE VIEW punkt_szczepien_historia as select
d.imie as imie, d.nazwisko as nazwisko, d.pesel as pesel, 
d.nr_tel, s.nazwa, sz.data_szczepienia, sz.id_szczepionki as id_szczepionki, sz.id_szczepienia as id_szczepienia
from szczepienia_zrealizowane sz join  dane_pacjenta d 
on sz.id_pacjenta = d.pesel
join szczepionki s on sz.id_szczepionki = s.id;


 insert into szczepionki(id, nazwa) values (1, 'Błonica');
 insert into szczepionki(id, nazwa) values (2, 'Przeciw Cholerze');
 insert into szczepionki(id, nazwa) values (3, 'COVID-19');
 insert into szczepionki(id, nazwa) values (4, 'Dur brzuszny');
 insert into szczepionki(id, nazwa) values (5, 'Grypa');
 insert into szczepionki(id, nazwa) values (6, 'Gruzlica');
 insert into szczepionki(id, nazwa) values (7, 'HPV');
 insert into szczepionki(id, nazwa) values (8, 'Krztusiec');
 insert into szczepionki(id, nazwa) values (9, 'Meningokoki');
 

-- Anaa czerwińska, pesel: 93020258781, data urodzenia: 02.02.1993, nr_tel: 607832777
CREATE USER 'czerann93'@'localhost' IDENTIFIED BY 'tajnehaslo123';
GRANT SELECT ON punkt_szczepien.pacjent TO 'czerann93'@'localhost';
GRANT SELECT ON punkt_szczepien.punkt_szczepien TO 'czerann93'@'localhost';
GRANT SELECT ON punkt_szczepien.punkt_szczepien_historia TO 'czerann93'@'localhost';
GRANT SELECT ON punkt_szczepien.szczepionki TO 'czerann93'@'localhost';
GRANT SELECT ON punkt_szczepien.terminy TO 'czerann93'@'localhost';
GRANT SELECT ON punkt_szczepien.szczepienia_zrealizowane TO 'czerann93'@'localhost';
GRANT SELECT ON punkt_szczepien.dane_pacjenta TO 'czerann93'@'localhost';
GRANT INSERT ON punkt_szczepien.dane_pacjenta TO 'czerann93'@'localhost';
GRANT ALL PRIVILEGES ON punkt_szczepien.rejestracja_na_szczepienie TO 'czerann93'@'localhost';


-- Stanisław Kruk, pesel: 48082463231, data_ur: 24.08.1948, nr_tel: 508338888 
CREATE USER 'krusta48'@'localhost' IDENTIFIED BY 'tajnehaslo456';
GRANT SELECT ON punkt_szczepien.pacjent TO 'krusta48'@'localhost';
GRANT SELECT ON punkt_szczepien.punkt_szczepien TO 'krusta48'@'localhost';
GRANT SELECT ON punkt_szczepien.punkt_szczepien_historia TO 'krusta48'@'localhost';
GRANT SELECT ON punkt_szczepien.szczepionki TO 'krusta48'@'localhost';
GRANT SELECT ON punkt_szczepien.terminy TO 'krusta48'@'localhost';
GRANT SELECT ON punkt_szczepien.szczepienia_zrealizowane TO 'krusta48'@'localhost';
GRANT SELECT ON punkt_szczepien.dane_pacjenta TO 'krusta48'@'localhost';
GRANT INSERT ON punkt_szczepien.dane_pacjenta TO 'krusta48'@'localhost';
GRANT ALL PRIVILEGES ON punkt_szczepien.rejestracja_na_szczepienie TO 'krusta48'@'localhost';


-- Adrian Małecki, pesel: 06260473444, data_ur: 04.06.2006, nr_tel: 513354445
CREATE USER 'maladr06'@'localhost' IDENTIFIED BY 'tajnehaslo789';
GRANT SELECT ON punkt_szczepien.pacjent TO 'maladr06'@'localhost';
GRANT SELECT ON punkt_szczepien.punkt_szczepien TO 'maladr06'@'localhost';
GRANT SELECT ON punkt_szczepien.punkt_szczepien_historia TO 'maladr06'@'localhost';
GRANT SELECT ON punkt_szczepien.szczepionki TO 'maladr06'@'localhost';
GRANT SELECT ON punkt_szczepien.terminy TO 'maladr06'@'localhost';
GRANT SELECT ON punkt_szczepien.szczepienia_zrealizowane TO 'maladr06'@'localhost';
GRANT SELECT ON punkt_szczepien.dane_pacjenta TO 'maladr06'@'localhost';
GRANT INSERT ON punkt_szczepien.dane_pacjenta TO 'maladr06'@'localhost';
GRANT ALL PRIVILEGES ON punkt_szczepien.rejestracja_na_szczepienie TO 'maladr06'@'localhost';


CREATE USER 'pracownik'@'localhost' IDENTIFIED BY 'Luty2022';
GRANT ALL PRIVILEGES ON punkt_szczepien.punkt_szczepien TO 'pracownik'@'localhost';
GRANT ALL PRIVILEGES ON punkt_szczepien.punkt_szczepien_historia TO 'pracownik'@'localhost';
GRANT ALL PRIVILEGES ON  punkt_szczepien.szczepionki TO 'pracownik'@'localhost';
GRANT ALL PRIVILEGES ON  punkt_szczepien.terminy TO 'pracownik'@'localhost';
GRANT ALL PRIVILEGES ON  punkt_szczepien.szczepienia_zrealizowane TO 'pracownik'@'localhost';
GRANT ALL PRIVILEGES ON  punkt_szczepien.rejestracja_na_szczepienie TO 'pracownik'@'localhost';
GRANT ALL PRIVILEGES ON  punkt_szczepien.dane_pacjenta TO 'pracownik'@'localhost';

insert into terminy(termin) values ('2021-06-20 15:00');
insert into terminy(termin) values ('2022-02-03 15:30');
insert into terminy(termin) values ('2022-02-20 13:30');
insert into terminy(termin) values ('2022-02-26 15:00');
insert into terminy(termin) values ('2022-05-15 16:30');
insert into terminy(termin) values ('2022-06-28 17:15');
insert into terminy(termin) values ('2022-02-22 12:20');
insert into terminy(termin) values ('2022-02-27 10:45');
