-- Creación de usuarios.
INSERT INTO users(username, password, avatar, tier, description, authority, birth_date, enable)
VALUES ('alesanfe', 'patata', 'https://i.pinimg.com/736x/bd/33/43/bd3343e3e4e13c58e408c79f0e029b75.jpg', 0,
        'I want a nap', 'DOKTOL', '2002-02-01', 1),
       ('antonio', 'patata', 'https://i.pinimg.com/736x/bd/33/43/bd3343e3e4e13c58e408c79f0e029b75.jpg', 0,
        'I am a description', 'USER', '2002-02-01', 1),
       ('laurolmer', 'patata', 'https://i.pinimg.com/736x/bd/33/43/bd3343e3e4e13c58e408c79f0e029b75.jpg', 0,
        'awanabumbambam', 'USER', '2002-08-21', 1),
       ('alvhidrod', 'patata', 'https://i.pinimg.com/736x/bd/33/43/bd3343e3e4e13c58e408c79f0e029b75.jpg', 0,
        'drakorion', 'USER', '2002-02-23', 1),
       ('ismruijur', 'patata', 'https://i.pinimg.com/736x/bd/33/43/bd3343e3e4e13c58e408c79f0e029b75.jpg', 0,
        'er jefe brrr', 'USER', '2002-10-27', 1),
       ('ivasansan1', 'patata', 'https://i.pinimg.com/736x/bd/33/43/bd3343e3e4e13c58e408c79f0e029b75.jpg', 0,
        'bético encubierto', 'USER', '2002-11-12', 1),
       ('pedruiagu', 'patata', 'https://i.pinimg.com/736x/bd/33/43/bd3343e3e4e13c58e408c79f0e029b75.jpg', 0,
        'sácame del bolsillo', 'USER', '2002-10-01', 1);

-- Creación de mensajes.
INSERT INTO messages(content, time, receiver_id, sender_id)
VALUES ('Hola, soy Alesanfe', '2020-02-01 12:00', 1, 2),
       ('Hola, soy Antonio', '2020-02-01 12:00', 2, 1);

-- Creación de capacidades.
INSERT INTO capacities(state_capacity, less_damage)
VALUES ('MELEE', false),
       ('RANGE', false),
       ('MAGIC', false),
       ('EXPERTISE', false),
       ('MELEE', true),
       ('RANGE', true),
       ('MAGIC', true),
       ('EXPERTISE', true);

-- Creación de héroes.
INSERT INTO heroes(name, front_image, back_image, health, role)
VALUES ('Valèrys', 'src/main/resources/static/resources/images/heroes/valerys_hero_front.png',
        'src/main/resources/static/resources/images/heroes/lisavette_hero_back.png', 3, 'KNIGHT'),
       ('Lisavette', 'src/main/resources/static/resources/images/heroes/lisavette_hero_back.png',
        'src/main/resources/static/resources/images/heroes/valerys_hero_front.png', 3, 'KNIGHT'),
       ('Beleth-Il', 'src/main/resources/static/resources/images/heroes/belethil_hero_front.png',
        'src/main/resources/static/resources/images/heroes/idril_hero_back.png', 3, 'EXPLORER'),
       ('Idril', 'src/main/resources/static/resources/images/heroes/idril_hero_back.png',
        'src/main/resources/static/resources/images/heroes/belethil_hero_front.png', 3, 'EXPLORER'),
       ('Neddia', 'src/main/resources/static/resources/images/heroes/neddia_hero_back.png',
        'src/main/resources/static/resources/images/heroes/feldon_hero_front.png', 2, 'THIEF'),
       ('Feldon', 'src/main/resources/static/resources/images/heroes/feldon_hero_back.png',
        'src/main/resources/static/resources/images/heroes/neddia_hero_front.png', 2, 'THIEF'),
       ('Aranel', 'src/main/resources/static/resources/images/heroes/aranel_hero_back.png',
        'src/main/resources/static/resources/images/heroes/taheral_hero_front.png', 2, 'WIZARD'),
       ('Taheral', 'src/main/resources/static/resources/images/heroes/taheral_hero_back.png',
        'src/main/resources/static/resources/images/heroes/aranel_hero_front.png', 2, 'WIZARD');

-- Creación de habilidades.
INSERT INTO abilities(name, front_image, back_image, role, attack, quantity)
VALUES ('Compañero Lobo', 'src/main/resources/static/resources/images/juego/wolf_abilities_front.png',
        'src/main/resources/static/resources/images/juego/parte_atras_general.png', 'EXPLORER', 2, 1),
       ('Disparo Certero', 'src/main/resources/static/resources/images/juego/sharp_abilities_front.png',
        'src/main/resources/static/resources/images/juego/parte_atras_general.png', 'EXPLORER', 3, 1),
       ('Disparo Rápido', 'src/main/resources/static/resources/images/juego/rapid_abilities_front.png',
        'src/main/resources/static/resources/images/juego/parte_atras_general.png', 'EXPLORER', 1, 1),
       ('En la Diana', 'src/main/resources/static/resources/images/juego/diana_abilities_front.png',
        'src/main/resources/static/resources/images/juego/parte_atras_general.png', 'EXPLORER', 4, 1),
       ('Lluvia de flechas', 'src/main/resources/static/resources/images/juego/rain_abilities_front.png',
        'src/main/resources/static/resources/images/juego/parte_atras_general.png', 'EXPLORER', 2, 1),
       ('Recoger flechas', 'src/main/resources/static/resources/images/juego/pick_abilities_front.png',
        'src/main/resources/static/resources/images/juego/parte_atras_general.png', 'EXPLORER', 0, 1),
       ('Supervivencia', 'src/main/resources/static/resources/images/juego/survival_abilities_front.png',
        'src/main/resources/static/resources/images/juego/parte_atras_general.png', 'EXPLORER', 0, 1),
       ('Ataque Brutal', 'src/main/resources/static/resources/images/juego/brutal_abilities_front.png',
        'src/main/resources/static/resources/images/juego/parte_atras_general.png', 'KNIGHT', 3, 1),
       ('Carga con Escudo', 'src/main/resources/static/resources/images/juego/charge_abilities_front.png',
        'src/main/resources/static/resources/images/juego/parte_atras_general.png', 'KNIGHT', 2, 1),
       ('Doble Espadazo', 'src/main/resources/static/resources/images/juego/doble_abilities_front.png',
        'src/main/resources/static/resources/images/juego/parte_atras_general.png', 'KNIGHT', 2, 1),
       ('Escudo', 'src/main/resources/static/resources/images/juego/shield_abilities_front.png',
        'src/main/resources/static/resources/images/juego/parte_atras_general.png', 'KNIGHT', 0, 1),
       ('Espadazo', 'src/main/resources/static/resources/images/juego/sword_abilities_front.png',
        'src/main/resources/static/resources/images/juego/parte_atras_general.png', 'KNIGHT', 1, 1),
       ('Paso Atrás', 'src/main/resources/static/resources/images/juego/back_abilities_front.png',
        'src/main/resources/static/resources/images/juego/parte_atras_general.png', 'KNIGHT', 0, 1),
       ('Todo o Nada', 'src/main/resources/static/resources/images/juego/all_abilities_front.png',
        'src/main/resources/static/resources/images/juego/parte_atras_general.png', 'KNIGHT', 1, 1),
       ('Voz de Aliento', 'src/main/resources/static/resources/images/juego/AAAAAAAA_abilities_front.png',
        'src/main/resources/static/resources/images/juego/parte_atras_general.png', 'KNIGHT', 0, 1),
       ('Aura protectora', 'src/main/resources/static/resources/images/abilities/auraprotectora_ability_front.png',
        'src/main/resources/static/resources/images/abilities/ability_back.png', 'WIZARD', 0, 1),
       ('Bola de fuego', 'src/main/resources/static/resources/images/abilities/boladefuego_ability_front.png',
        'src/main/resources/static/resources/images/abilities/ability_back.png', 'WIZARD', 2, 1),
       ('Disparo gélido', 'src/main/resources/static/resources/images/abilities/disparogelido_ability_front.png',
        'src/main/resources/static/resources/images/abilities/ability_back.png', 'WIZARD', 1, 2),
       ('Flecha corrosiva', 'src/main/resources/static/resources/images/abilities/flechacorrosiva_ability_front.png',
        'src/main/resources/static/resources/images/abilities/ability_back.png', 'WIZARD', 1, 1),
       ('Golpe de bastón', 'src/main/resources/static/resources/images/abilities/golpebaston_ability_front.png',
        'src/main/resources/static/resources/images/abilities/ability_back.png', 'WIZARD', 1, 3),
       ('Orbe curativo', 'src/main/resources/static/resources/images/abilities/orbecurativo_ability_front.png',
        'src/main/resources/static/resources/images/abilities/ability_back.png', 'WIZARD', 0, 1),
       ('Proyectil ígneo', 'src/main/resources/static/resources/images/abilities/proyectiligneo_ability_front.png',
        'src/main/resources/static/resources/images/abilities/ability_back.png', 'WIZARD', 2, 1),
       ('Reconstitución', 'src/main/resources/static/resources/images/abilities/reconstitucion_ability_front.png',
        'src/main/resources/static/resources/images/abilities/ability_back.png', 'WIZARD', 0, 1),
       ('Torrente de luz', 'src/main/resources/static/resources/images/abilities/torrentedeluz_ability_front.png',
        'src/main/resources/static/resources/images/abilities/ability_back.png', 'WIZARD', 2, 1),
       ('Al corazón', 'src/main/resources/static/resources/images/abilities/alcorazon_ability_front.png',
        'src/main/resources/static/resources/images/abilities/ability_back.png', 'THIEF', 4, 1),
       ('Ataque furtivo', 'src/main/resources/static/resources/images/abilities/ataquefurtivo_ability_front.png',
        'src/main/resources/static/resources/images/abilities/ability_back.png', 'THIEF', 2, 1),
       ('Ballesta precisa', 'src/main/resources/static/resources/images/abilities/ballestaprecisa_ability_front.png',
        'src/main/resources/static/resources/images/abilities/ability_back.png', 'THIEF', 2, 2),
       ('En las sombras', 'src/main/resources/static/resources/images/abilities/enlassombras_ability_front.png',
        'src/main/resources/static/resources/images/abilities/ability_back.png', 'THIEF', 1, 1),
       ('Engañar', 'src/main/resources/static/resources/images/abilities/engañar_ability_front.png',
        'src/main/resources/static/resources/images/abilities/ability_back.png', 'THIEF', 0, 1),
       ('Robar bolsillo', 'src/main/resources/static/resources/images/abilities/robarbolsillos_ability_front.png',
        'src/main/resources/static/resources/images/abilities/ability_back.png', 'THIEF', 0, 1),
       ('Saqueo', 'src/main/resources/static/resources/images/abilities/saqueo_ability_front.png',
        'src/main/resources/static/resources/images/abilities/ability_back.png', 'THIEF', 0, 1),
       ('Trampa', 'src/main/resources/static/resources/images/abilities/trampa_ability_front.png',
        'src/main/resources/static/resources/images/abilities/ability_back.png', 'THIEF', 0, 1);

-- Creación de orcos.
INSERT INTO orcs(name, back_image, front_image, health, glory, gold, has_cure, less_damage_wizard)
VALUES ('Honda', 'src/main/resources/static/resources/images/juego/0-0_orc_back.png',
        'src/main/resources/static/resources/images/juego/honda_orc_front.png', 2, 1, 0, FALSE, FALSE),
       ('Honda', 'src/main/resources/static/resources/images/juego/1-0_orc_back.png',
        'src/main/resources/static/resources/images/juego/honda_orc_front.png', 2, 1, 1, FALSE, FALSE),
       ('Honda', 'src/main/resources/static/resources/images/juego/1-0_orc_back.png',
        'src/main/resources/static/resources/images/juego/honda_orc_front.png', 2, 1, 1, FALSE, FALSE),
       ('Honda', 'src/main/resources/static/resources/images/juego/0-0_orc_back.png',
        'src/main/resources/static/resources/images/juego/honda_orc_front.png', 2, 1, 0, FALSE, FALSE),
       ('Honda', 'src/main/resources/static/resources/images/juego/1-0_orc_back.png',
        'src/main/resources/static/resources/images/juego/honda_orc_front.png', 2, 1, 1, FALSE, FALSE),
       ('Lanza', 'src/main/resources/static/resources/images/juego/0-0_orc_back.png',
        'src/main/resources/static/resources/images/juego/lanza_orc_front.png', 3, 2, 0, TRUE, FALSE),
       ('Lanza', 'src/main/resources/static/resources/images/juego/0-0_orc_back.png',
        'src/main/resources/static/resources/images/juego/lanza_orc_front.png', 3, 2, 0, TRUE, FALSE),
       ('Lanza', 'src/main/resources/static/resources/images/juego/0-0_orc_back.png',
        'src/main/resources/static/resources/images/juego/lanza_orc_front.png', 3, 2, 0, TRUE, FALSE),
       ('Lanza', 'src/main/resources/static/resources/images/juego/1-0_orc_back.png',
        'src/main/resources/static/resources/images/juego/lanza_orc_front.png', 3, 2, 1, TRUE, FALSE),
       ('Lanza', 'src/main/resources/static/resources/images/juego/1-1_orc_back.png',
        'src/main/resources/static/resources/images/juego/chaman_orc_front.png', 3, 3, 1, TRUE, FALSE),
       ('Chaman', 'src/main/resources/static/resources/images/juego/2-0_orc_back.png',
        'src/main/resources/static/resources/images/juego/chaman_orc_front.png', 3, 1, 2, FALSE, TRUE),
       ('Chaman', 'src/main/resources/static/resources/images/juego/0-0_orc_back.png',
        'src/main/resources/static/resources/images/juego/chaman_orc_front.png', 3, 1, 0, FALSE, TRUE),
       ('Sword', 'src/main/resources/static/resources/images/juego/0-0_orc_back.png',
        'src/main/resources/static/resources/images/juego/sword_orc_front.png', 4, 2, 0, FALSE, FALSE),
       ('Sword', 'src/main/resources/static/resources/images/juego/0-0_orc_back.png',
        'src/main/resources/static/resources/images/juego/sword_orc_front.png', 4, 2, 0, FALSE, FALSE),
       ('Sword', 'src/main/resources/static/resources/images/juego/1-0_orc_back.png',
        'src/main/resources/static/resources/images/juego/sword_orc_front.png', 4, 2, 1, FALSE, FALSE),
       ('Sword', 'src/main/resources/static/resources/images/juego/1-0_orc_back.png',
        'src/main/resources/static/resources/images/juego/bounty_orc_front.png', 4, 2, 1, FALSE, FALSE),
       ('Sword', 'src/main/resources/static/resources/images/juego/1-0_orc_back.png',
        'src/main/resources/static/resources/images/juego/bounty_orc_front.png', 4, 2, 1, FALSE, FALSE),
       ('Sword', 'src/main/resources/static/resources/images/juego/2-0_orc_back.png',
        'src/main/resources/static/resources/images/juego/bounty_orc_front.png', 4, 2, 2, FALSE, FALSE),
       ('Sword', 'src/main/resources/static/resources/images/juego/2-1_orc_back.png',
        'src/main/resources/static/resources/images/juego/bounty_orc_front.png', 4, 3, 2, FALSE, FALSE),
       ('Sword', 'src/main/resources/static/resources/images/juego/2-1_orc_back.png',
        'src/main/resources/static/resources/images/juego/bounty_orc_front.png', 4, 3, 2, FALSE, FALSE),
       ('Gran Chaman', 'src/main/resources/static/resources/images/juego/0-0_orc_back.png',
        'src/main/resources/static/resources/images/juego/greatchaman_orc_front.png', 5, 3, 0, FALSE, TRUE),
       ('Gran Chaman', 'src/main/resources/static/resources/images/juego/2-0_orc_back.png',
        'src/main/resources/static/resources/images/juego/greatchaman_orc_front.png', 5, 3, 2, FALSE, TRUE),
       ('Gran Chaman', 'src/main/resources/static/resources/images/juego/2-0_orc_back.png',
        'src/main/resources/static/resources/images/juego/greatchaman_orc_front.png', 5, 3, 2, FALSE, TRUE),
       ('Gran Chaman', 'src/main/resources/static/resources/images/juego/2-1_orc_back.png',
        'src/main/resources/static/resources/images/juego/greatchaman_orc_front.png', 5, 4, 2, FALSE, TRUE),
       ('Terminator', 'src/main/resources/static/resources/images/juego/1-0_orc_back.png',
        'src/main/resources/static/resources/images/juego/axe_orc_front.png', 6, 4, 0, FALSE, FALSE),
       ('Terminator', 'src/main/resources/static/resources/images/juego/1-0_orc_back.png',
        'src/main/resources/static/resources/images/juego/axe_orc_front.png', 6, 4, 0, FALSE, FALSE),
       ('Terminator', 'src/main/resources/static/resources/images/juego/0-0_orc_back.png',
        'src/main/resources/static/resources/images/juego/axe_orc_front.png', 6, 4, 1, FALSE, FALSE);


-- Creación de señores de la noche.
INSERT INTO night_lords(name, back_image, front_image, health)
VALUES ('Gurdrug', 'src/main/resources/static/resources/images/juego/nightlord_back.png',
        'src/main/resources/static/resources/images/juego/gur_nightlord_front.png', 8),
       ('Roghkiller', 'src/main/resources/static/resources/images/juego/nightlord_back.png',
        'src/main/resources/static/resources/images/juego/rogh_nightlord_front.png', 9),
       ('Shriekknifer', 'src/main/resources/static/resources/images/juego/nightlord_back.png',
        'src/main/resources/static/resources/images/juego/shriek_nightlord_front.png', 10);



-- Productos
INSERT INTO products(name, front_image, back_image, price, attack, quantity)
VALUES ('Daga élfica', 'src/main/resources/static/resources/images/products/dagaelfica_product_front.png',
        'src/main/resources/static/resources/images/products/product_back.png', 3, 2, 2),
       ('Poción curativa', 'src/main/resources/static/resources/images/products/pocioncurativa_product_front.png',
        'src/main/resources/static/resources/images/products/product_back.png', 8, 0, 3),
       ('Piedra de amolar', 'src/main/resources/static/resources/images/products/piedraamolar_product_front.png',
        'src/main/resources/static/resources/images/products/product_back.png', 4, 0, 1),
       ('Vial de conjuración',
        'src/main/resources/static/resources/images/products/vialconjuracion_product_front.png',
        'src/main/resources/static/resources/images/products/product_back.png', 5, 0, 2),
       ('Elixir de concentración',
        'src/main/resources/static/resources/images/products/pocioncurativa_product_front.png',
        'src/main/resources/static/resources/images/products/product_back.png', 3, 0, 2),
       ('Capa élfica', 'src/main/resources/static/resources/images/products/capaelfica_product_front.png',
        'src/main/resources/static/resources/images/products/product_back.png', 3, 0, 1),
       ('Armadura de placas',
        'src/main/resources/static/resources/images/products/armaduraplacas_product_front.png',
        'src/main/resources/static/resources/images/products/product_back.png', 4, 0, 1),
       ('Alabarda orca', 'src/main/resources/static/resources/images/products/alabardaorca_product_front.png',
        'src/main/resources/static/resources/images/products/product_back.png', 5, 4, 1),
       ('Arco compuesto', 'src/main/resources/static/resources/images/products/arcocompuesto_product_front.png',
        'src/main/resources/static/resources/images/products/product_back.png', 5, 4, 1);

-- Nuevo

-- Creación de la relación entre capacidades y productos.
INSERT INTO products_capacity(product_id, capacity_id) VALUES
(3, 1), (3, 2), (3,4), (6, 3), (6, 4),
(7, 2), (8, 2), (9, 4);

-- Creación de logros.
INSERT INTO achievements(name, description, image, threshold) VALUES
('Primera partida', 'Se le otorga un logro al usuario cuando completa su primera partida. ', 'awanakimkum', '1'),
('Primera victoria', 'Se le otorga un logro al usuario cuando gana su primera partida. ', 'awanakimkum', '1'),
('Última Sangre', 'Se le otorga un logro al usuario cuando gana su primera partida.', 'awanakimkum', '1'),
('Aficionado', 'Se le otorga un logro al usuario cuando mata a 10 orcos. ', 'awanakimkum', '10'),
('Veterano', 'Se le otorga un logro al usuario cuando mata a 50 orcos. ', 'awanakimkum', '50'),
('Maestro', 'Se le otorga un logro al usuario cuando mata a 100 orcos. ', 'awanakimkum', '100'),
('Exterminador', 'Se le otorga un logro al usuario cuando mata a 200 orcos. ', 'awanakimkum', '200'),
('Asesino', 'Se le otorga un logro al usuario cuando mata a 500 orcos. ', 'awanakimkum', '500'),
('Mataorcos', 'Se le otorga un logro al usuario cuando mata a 1000 orcos. ', 'awanakimkum', '1000'),
('Primeros pasos', 'Se le otorga un logro al usuario cuando compra su primer producto. ', 'awanakimkum', '1'),
('Comprador', 'Se le otorga un logro al usuario cuando compra 10 productos. ', 'awanakimkum', '10'),
('Comprador experto', 'Se le otorga un logro al usuario cuando compra 50 productos. ', 'awanakimkum', '50'),
('Comprador maestro', 'Se le otorga un logro al usuario cuando compra 100 productos. ', 'awanakimkum', '100'),
('Touch Some Grass', 'Se le otorga un logro al usuario cuando juega 20 partidas.', 'awanakimkum', '20'),
('Un poco de todo', 'Se le otorga un logro al usuario cuando completa su primera partida en modo multiclase.',
 'awanakimkum', '1');

-- Habilidades de los heroes
INSERT INTO heroes_abilities(hero_id, abilities_id)
VALUES (1, 8),
       (1, 9),
       (1, 10),
       (1, 11),
       (1, 12),
       (1, 13),
       (1, 14),
       (1, 15),
       (2, 8),
       (2, 9),
       (2, 10),
       (2, 11),
       (2, 12),
       (2, 13),
       (2, 14),
       (2, 15),
       (3, 1),
       (3, 2),
       (3, 3),
       (3, 4),
       (3, 5),
       (3, 6),
       (3, 7),
       (4, 1),
       (4, 2),
       (4, 3),
       (4, 4),
       (4, 5),
       (4, 6),
       (4, 7),
       (5, 16),
       (5, 17),
       (5, 18),
       (5, 19),
       (5, 20),
       (5, 21),
       (5, 22),
       (5, 23),
       (5, 24),
       (6, 16),
       (6, 17),
       (6, 18),
       (6, 19),
       (6, 20),
       (6, 21),
       (6, 22),
       (6, 23),
       (6, 24),
       (7, 25),
       (7, 26),
       (7, 27),
       (7, 28),
       (7, 29),
       (7, 30),
       (7, 31),
       (7, 32),
       (8, 25),
       (8, 26),
       (8, 27),
       (8, 28),
       (8, 29),
       (8, 30),
       (8, 31),
       (8, 32);

-- Capacidades de los heroes
INSERT INTO heroes_capacities(hero_id, capacities_id)
VALUES (1, 1),
       (2, 1),
       (3, 2),
       (3, 5),
       (4, 2),
       (4, 5),
       (5, 4),
       (5, 6),
       (6, 4),
       (6, 6),
       (7, 3),
       (8, 3);
