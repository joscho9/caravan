-- Benutzer erstellen
INSERT INTO users (
    name,
    email,
    password
) VALUES (
             'admin',
             'test123@outlook.de',
             'testPasswort123'
         );

-- Erster Caravan
INSERT INTO caravans (
    id,
    wohnwagentyp,
    anzahl_schlafplaetze,
    gesamtlaenge,
    nutzlaenge,
    gesamtbreite,
    leergewicht,
    zulaessiges_gesamtgewicht,
    kupplungstyp,
    hoechstgeschwindigkeit,
    user_id,
    image_path,
    uebergabepauschale
) VALUES (
             '55183cc3-119e-49ac-8574-d0c6ddfd6cdf',
             'Tabbert P.E.A.T. 560 DM',
             '6',
             '7,85m',
             '6,57m',
             '2,50m',
             '1450Kg',
             '1900Kg',
             'Anti-Schlinger-Kupplung AL-Ko Typ ATC mit Trailer Control',
             '100km/h',
             1,
             '55183cc3-119e-49ac-8574-d0c6ddfd6cdf',
             10000
         );

-- CaravanImage für den ersten Caravan
INSERT INTO caravan_image (
    caravan_id,
    file_path,
    description
) VALUES (
             '55183cc3-119e-49ac-8574-d0c6ddfd6cdf',
             '55183cc3-119e-49ac-8574-d0c6ddfd6cdf/17473aaf-7f90-4d56-a336-d2eeede410f6.jpg',
             'Front view'
         );

-- CaravanPricing für den ersten Caravan
INSERT INTO caravan_pricing (
    caravan_id,
    start_date,
    end_date,
    price_per_day
) VALUES (
             '55183cc3-119e-49ac-8574-d0c6ddfd6cdf',
             '2000-04-01',
             '2000-09-30',
             70
         ), (
             '55183cc3-119e-49ac-8574-d0c6ddfd6cdf',
             '2000-01-01',
             '2000-03-31',
             60
         ), (
             '55183cc3-119e-49ac-8574-d0c6ddfd6cdf',
             '2000-10-01',
             '2000-12-31',
             60
         );

-- Zweiter Caravan
INSERT INTO caravans (
    id,
    wohnwagentyp,
    anzahl_schlafplaetze,
    gesamtlaenge,
    nutzlaenge,
    gesamtbreite,
    leergewicht,
    zulaessiges_gesamtgewicht,
    kupplungstyp,
    hoechstgeschwindigkeit,
    user_id,
    image_path,
    uebergabepauschale
) VALUES (
             '602ace6e-cc62-4390-b19e-cbd2a1eb17b2',
             'Hobby Prestige 545 KMF',
             '-1',
             '7,44m',
             '6,25m',
             '2,50m',
             '1462Kg',
             '1750Kg',
             'Anti-Schlinger-Kupplung mit Zulassung für Höchstgeschwindigkeit von 100Km/h vorausgesetzt das Zugfahrzeug hat eine Leermasse von mindestens 1750 Kg.',
             '100km/h',
             1,
             '602ace6e-cc62-4390-b19e-cbd2a1eb17b2',
             20000
         );

-- CaravanImage für den zweiten Caravan
INSERT INTO caravan_image (caravan_id, file_path, description) VALUES
                                                                   ('602ace6e-cc62-4390-b19e-cbd2a1eb17b2', '602ace6e-cc62-4390-b19e-cbd2a1eb17b2/5c95fe7c-c3e1-5bd8-a7e4-d5aa3a3fbf25_30878063_original.webp', 'Beschreibung für 5c95fe7c...'),
                                                                   ('602ace6e-cc62-4390-b19e-cbd2a1eb17b2', '602ace6e-cc62-4390-b19e-cbd2a1eb17b2/7e4384b4-e325-5bd1-8199-11499b938e86_30878064_original.webp', 'Beschreibung für 7e4384b4...'),
                                                                   ('602ace6e-cc62-4390-b19e-cbd2a1eb17b2', '602ace6e-cc62-4390-b19e-cbd2a1eb17b2/8db0585b-c2e2-5f80-a952-326e86932881_30878067_original.webp', 'Beschreibung für 8db0585b...'),
                                                                   ('602ace6e-cc62-4390-b19e-cbd2a1eb17b2', '602ace6e-cc62-4390-b19e-cbd2a1eb17b2/63e3e6e1-7c4a-5f6d-8877-a0328b6ae90d_30878065_original.webp', 'Beschreibung für 63e3e6e1...'),
                                                                   ('602ace6e-cc62-4390-b19e-cbd2a1eb17b2', '602ace6e-cc62-4390-b19e-cbd2a1eb17b2/594d01c1-4942-5221-88f2-9fe655d87938_30878066_original.webp', 'Beschreibung für 594d01c1...'),
                                                                   ('602ace6e-cc62-4390-b19e-cbd2a1eb17b2', '602ace6e-cc62-4390-b19e-cbd2a1eb17b2/643ef590-e0c5-5650-9d33-057ce31e75ad_30878062_original.webp', 'Beschreibung für 643ef590...'),
                                                                   ('602ace6e-cc62-4390-b19e-cbd2a1eb17b2', '602ace6e-cc62-4390-b19e-cbd2a1eb17b2/c385c3bc-715e-5393-9687-76fbca018e9e_30878069_original.webp', 'Beschreibung für c385c3bc...'),
                                                                   ('602ace6e-cc62-4390-b19e-cbd2a1eb17b2', '602ace6e-cc62-4390-b19e-cbd2a1eb17b2/e99b13c6-2569-549c-b1be-90a7a25715f3_30878068_original.webp', 'Beschreibung für e99b13c6...'),
                                                                   ('602ace6e-cc62-4390-b19e-cbd2a1eb17b2', '602ace6e-cc62-4390-b19e-cbd2a1eb17b2/main.webp', 'Beschreibung für main.webp');


-- CaravanPricing für den zweiten Caravan
INSERT INTO caravan_pricing (
    caravan_id,
    start_date,
    end_date,
    price_per_day
) VALUES (
             '602ace6e-cc62-4390-b19e-cbd2a1eb17b2',
             '2000-04-01',
             '2000-09-30',
             70
         ), (
             '602ace6e-cc62-4390-b19e-cbd2a1eb17b2',
             '2000-01-01',
             '2000-03-31',
             60
         ), (
             '602ace6e-cc62-4390-b19e-cbd2a1eb17b2',
             '2000-10-01',
             '2000-12-31',
             60
         );

-- Dritter Caravan
INSERT INTO caravans (id, wohnwagentyp, anzahl_schlafplaetze, gesamtlaenge, nutzlaenge, gesamtbreite, leergewicht, zulaessiges_gesamtgewicht, kupplungstyp, hoechstgeschwindigkeit, user_id, image_path, uebergabepauschale)
VALUES (
             'e2ca8c9f-aebe-43b9-ac13-de7fa46e8e6c',
             'Wilk Vida 490 KM',
             '-1',
             '7,37m',
             '5,32m',
             '2,32m',
             '1240Kg',
             '1500Kg',
             'Anti-Schlinger-Kupplung mit Zulassung für Höchstgeschwindigkeit von 100Km/h, vorausgesetzt das Zugfahrzeug hat eine Leermasse von mindestens 1500 Kg.',
             '100km/h',
             1,
             'e2ca8c9f-aebe-43b9-ac13-de7fa46e8e6c',
             30000
         );

-- CaravanImage für den dritten Caravan
INSERT INTO caravan_image (caravan_id, file_path, description)
VALUES (
             'e2ca8c9f-aebe-43b9-ac13-de7fa46e8e6c',
             'e2ca8c9f-aebe-43b9-ac13-de7fa46e8e6c/8bee0a15-a0d2-5877-a8fc-a055266d1ff8_25279769_original.webp',
           -- Benutzer erstellen
             INSERT INTO users (
    name,
    email,
    password
) VALUES (
             'admin',
             'test123@outlook.de',
             'testPasswort123'
         );

-- Erster Caravan
INSERT INTO caravans (
    id,
    wohnwagentyp,
    anzahl_schlafplaetze,
    gesamtlaenge,
    nutzlaenge,
    gesamtbreite,
    leergewicht,
    zulaessiges_gesamtgewicht,
    kupplungstyp,
    hoechstgeschwindigkeit,
    user_id,
    image_path,
    uebergabepauschale
) VALUES (
             '55183cc3-119e-49ac-8574-d0c6ddfd6cdf',
             'Tabbert P.E.A.T. 560 DM',
             '6',
             '7,85m',
             '6,57m',
             '2,50m',
             '1450Kg',
             '1900Kg',
             'Anti-Schlinger-Kupplung AL-Ko Typ ATC mit Trailer Control',
             '100km/h',
             1,
             '55183cc3-119e-49ac-8574-d0c6ddfd6cdf',
             10000
         );

-- CaravanImage für den ersten Caravan
INSERT INTO caravan_image (
    caravan_id,
    file_path,
    description
) VALUES (
             '55183cc3-119e-49ac-8574-d0c6ddfd6cdf',
             '55183cc3-119e-49ac-8574-d0c6ddfd6cdf/17473aaf-7f90-4d56-a336-d2eeede410f6.jpg',
             'Front view'
         );

-- CaravanPricing für den ersten Caravan
INSERT INTO caravan_pricing (
    caravan_id,
    start_date,
    end_date,
    price_per_day
) VALUES (
             '55183cc3-119e-49ac-8574-d0c6ddfd6cdf',
             '2000-04-01',
             '2000-09-30',
             70
         ), (
             '55183cc3-119e-49ac-8574-d0c6ddfd6cdf',
             '2000-01-01',
             '2000-03-31',
             60
         ), (
             '55183cc3-119e-49ac-8574-d0c6ddfd6cdf',
             '2000-10-01',
             '2000-12-31',
             60
         );

-- Zweiter Caravan
INSERT INTO caravans (
    id,
    wohnwagentyp,
    anzahl_schlafplaetze,
    gesamtlaenge,
    nutzlaenge,
    gesamtbreite,
    leergewicht,
    zulaessiges_gesamtgewicht,
    kupplungstyp,
    hoechstgeschwindigkeit,
    user_id,
    image_path,
    uebergabepauschale
) VALUES (
             '602ace6e-cc62-4390-b19e-cbd2a1eb17b2',
             'Hobby Prestige 545 KMF',
             '-1',
             '7,44m',
             '6,25m',
             '2,50m',
             '1462Kg',
             '1750Kg',
             'Anti-Schlinger-Kupplung mit Zulassung für Höchstgeschwindigkeit von 100Km/h vorausgesetzt das Zugfahrzeug hat eine Leermasse von mindestens 1750 Kg.',
             '100km/h',
             1,
             '602ace6e-cc62-4390-b19e-cbd2a1eb17b2',
             20000
         );

-- CaravanImage für den zweiten Caravan
INSERT INTO caravan_image (caravan_id, file_path, description) VALUES
                                                                   ('602ace6e-cc62-4390-b19e-cbd2a1eb17b2', '602ace6e-cc62-4390-b19e-cbd2a1eb17b2/5c95fe7c-c3e1-5bd8-a7e4-d5aa3a3fbf25_30878063_original.webp', 'Beschreibung für 5c95fe7c...'),
                                                                   ('602ace6e-cc62-4390-b19e-cbd2a1eb17b2', '602ace6e-cc62-4390-b19e-cbd2a1eb17b2/7e4384b4-e325-5bd1-8199-11499b938e86_30878064_original.webp', 'Beschreibung für 7e4384b4...'),
                                                                   ('602ace6e-cc62-4390-b19e-cbd2a1eb17b2', '602ace6e-cc62-4390-b19e-cbd2a1eb17b2/8db0585b-c2e2-5f80-a952-326e86932881_30878067_original.webp', 'Beschreibung für 8db0585b...'),
                                                                   ('602ace6e-cc62-4390-b19e-cbd2a1eb17b2', '602ace6e-cc62-4390-b19e-cbd2a1eb17b2/63e3e6e1-7c4a-5f6d-8877-a0328b6ae90d_30878065_original.webp', 'Beschreibung für 63e3e6e1...'),
                                                                   ('602ace6e-cc62-4390-b19e-cbd2a1eb17b2', '602ace6e-cc62-4390-b19e-cbd2a1eb17b2/594d01c1-4942-5221-88f2-9fe655d87938_30878066_original.webp', 'Beschreibung für 594d01c1...'),
                                                                   ('602ace6e-cc62-4390-b19e-cbd2a1eb17b2', '602ace6e-cc62-4390-b19e-cbd2a1eb17b2/643ef590-e0c5-5650-9d33-057ce31e75ad_30878062_original.webp', 'Beschreibung für 643ef590...'),
                                                                   ('602ace6e-cc62-4390-b19e-cbd2a1eb17b2', '602ace6e-cc62-4390-b19e-cbd2a1eb17b2/c385c3bc-715e-5393-9687-76fbca018e9e_30878069_original.webp', 'Beschreibung für c385c3bc...'),
                                                                   ('602ace6e-cc62-4390-b19e-cbd2a1eb17b2', '602ace6e-cc62-4390-b19e-cbd2a1eb17b2/e99b13c6-2569-549c-b1be-90a7a25715f3_30878068_original.webp', 'Beschreibung für e99b13c6...'),
                                                                   ('602ace6e-cc62-4390-b19e-cbd2a1eb17b2', '602ace6e-cc62-4390-b19e-cbd2a1eb17b2/main.webp', 'Beschreibung für main.webp');


-- CaravanPricing für den zweiten Caravan
INSERT INTO caravan_pricing (
    caravan_id,
    start_date,
    end_date,
    price_per_day
) VALUES (
             '602ace6e-cc62-4390-b19e-cbd2a1eb17b2',
             '2000-04-01',
             '2000-09-30',
             70
         ), (
             '602ace6e-cc62-4390-b19e-cbd2a1eb17b2',
             '2000-01-01',
             '2000-03-31',
             60
         ), (
             '602ace6e-cc62-4390-b19e-cbd2a1eb17b2',
             '2000-10-01',
             '2000-12-31',
             60
         );

-- Dritter Caravan
INSERT INTO caravans (id, wohnwagentyp, anzahl_schlafplaetze, gesamtlaenge, nutzlaenge, gesamtbreite, leergewicht, zulaessiges_gesamtgewicht, kupplungstyp, hoechstgeschwindigkeit, user_id, image_path, uebergabepauschale)
VALUES (
           'e2ca8c9f-aebe-43b9-ac13-de7fa46e8e6c',
           'Wilk Vida 490 KM',
           '-1',
           '7,37m',
           '5,32m',
           '2,32m',
           '1240Kg',
           '1500Kg',
           'Anti-Schlinger-Kupplung mit Zulassung für Höchstgeschwindigkeit von 100Km/h, vorausgesetzt das Zugfahrzeug hat eine Leermasse von mindestens 1500 Kg.',
           '100km/h',
           1,
           'e2ca8c9f-aebe-43b9-ac13-de7fa46e8e6c',
           30000
       );

-- CaravanImage für den dritten Caravan
INSERT INTO caravan_image (caravan_id, file_path, description)
VALUES (
           'e2ca8c9f-aebe-43b9-ac13-de7fa46e8e6c',
           'e2ca8c9f-aebe-43b9-ac13-de7fa46e8e6c/8bee0a15-a0d2-5877-a8fc-a055266d1ff8_25279769_original.webp',
           'Front view'
       );

-- CaravanPricing für den dritten Caravan

INSERT INTO caravan_pricing (caravan_id, start_date, end_date, price_per_day)
VALUES
    ('e2ca8c9f-aebe-43b9-ac13-de7fa46e8e6c', '2000-04-01', '2000-09-30', 70),
    ('e2ca8c9f-aebe-43b9-ac13-de7fa46e8e6c', '2000-01-01', '2000-03-31', 60),
    ('e2ca8c9f-aebe-43b9-ac13-de7fa46e8e6c', '2000-10-01', '2000-12-31', 60);'Front view'
         );

-- CaravanPricing für den dritten Caravan

INSERT INTO caravan_pricing (caravan_id, start_date, end_date, price_per_day)
VALUES
          ('e2ca8c9f-aebe-43b9-ac13-de7fa46e8e6c', '2000-04-01', '2000-09-30', 70),
          ('e2ca8c9f-aebe-43b9-ac13-de7fa46e8e6c', '2000-01-01', '2000-03-31', 60),
          ('e2ca8c9f-aebe-43b9-ac13-de7fa46e8e6c', '2000-10-01', '2000-12-31', 60);