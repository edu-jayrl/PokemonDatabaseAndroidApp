Delimiter //
DROP PROCEDURE IF EXISTS filter_by_type; //
-- 
CREATE PROCEDURE filter_by_type(IN type_ varchar(45))
BEGIN
	select * from pokemon join pokemon_type on pokemon_id = idNum
	WHERE type_1 = type_ or type_2 = type_;
END // 

-- Examples
CALL filter_by_type("grass"); // 
CALL filter_by_type(""); // -- returns those without a second type (not bad, but lets make sure we say thats what we want, because we do)
CALL filter_by_type("water"); //


Delimiter //
DROP PROCEDURE IF EXISTS filter_by_generation; //
-- 
CREATE PROCEDURE filter_by_generation(IN generation_ int)
BEGIN
	select * from pokemon
	WHERE generation = generation_;
END // 

-- Examples
CALL filter_by_generation(1); // 
CALL filter_by_generation(0); // 
CALL filter_by_generation(7); //
CALL filter_by_generation(8); //


Delimiter //
DROP PROCEDURE IF EXISTS filter_by_trainer; //
-- 
CREATE PROCEDURE filter_by_trainer(IN trainer_ int)
BEGIN
	select pokemon.*, trainer_id from pokemon join trainer_pokemon on idNum = pokemon_id
	WHERE trainer_id = trainer_;
END // 

-- Examples
CALL filter_by_trainer(1); // 
CALL filter_by_trainer(0); // 
CALL filter_by_trainer(2); //
CALL filter_by_trainer(8); //

Delimiter //
DROP PROCEDURE IF EXISTS filter_by_game; //
-- 
CREATE PROCEDURE filter_by_game(IN game_ int)
BEGIN
	select * from pokemon
	WHERE generation <= (select generation from game where game_id = game_);
END // 

-- Examples
CALL filter_by_game(1); // 
CALL filter_by_game(0); // 
CALL filter_by_game(2); //
CALL filter_by_game(8); //

Delimiter //
DROP PROCEDURE IF EXISTS search_by_name; //
-- 
CREATE PROCEDURE search_by_name(IN name_ varchar(45))
BEGIN
	select pokemon.*, type_1, type_2 from pokemon join pokemon_type on idNum = pokemon_id
	WHERE name = name_;
END // 

-- Examples
CALL search_by_name("Pikachu"); // 
CALL search_by_name("Pichu"); // 
CALL search_by_name(""); //
CALL search_by_name("Hoopa"); //

Delimiter //
DROP PROCEDURE IF EXISTS add_trainer; //
-- 
CREATE PROCEDURE add_trainer(IN name_ varchar(45))
BEGIN
	insert into trainer (trainer_name)
	values(name_);
END // 

-- Examples
CALL add_trainer("kathleen"); // 
CALL add_trainer("Joshua"); //

Delimiter //
DROP PROCEDURE IF EXISTS add_pokemon; //
-- 
CREATE PROCEDURE add_pokemon(IN name_ varchar(45), in HP_ int, in speed_ int, in attack_ int, in special_attack_ int, in defense_ int, in special_defense_ int, in evolve_id_ int, in generation_ int, in type1_ varchar(45), in type2_ varchar(45))
BEGIN
	insert into pokemon (name, HP, speed, attack, special_attack, defense, special_defense, evolve_id, generation)
	values(name_, HP_, speed_, attack_, special_attack_, defense_, special_defense_, evolve_id_, generation_);
    
    insert into pokemon_type(pokemon_id, type_1, type_2)
    values((select idNum from pokemon where name = name_), type1_, type2_); 
END // 

-- Examples
CALL add_pokemon("test", 1, 1, 1, 1, 1, 1, 1, 1, "grass", "flying"); // 
CALL search_by_name("joshua"); //
CALL add_pokemon("test_2", 1, 1, 1, NULL, 1, NULL, NULL, 1, "electric", NULL); //
CALL search_by_name("test_2"); //

Delimiter //
DROP PROCEDURE IF EXISTS add_pokemon_to_trainer; //
-- 
CREATE PROCEDURE add_pokemon_to_trainer(IN trainer_ int, in pokemon_ int)
BEGIN
	insert into trainer_pokemon (trainer_id, pokemon_id)
	values(trainer_, pokemon_);
END // 

-- Examples
CALL add_pokemon_to_trainer(1, 45); // 
CALL filter_by_trainer(1); //
CALL add_pokemon_to_trainer(3, 450); //
CALL filter_by_trainer(3); //

Delimiter //
DROP PROCEDURE IF EXISTS delete_trainer; //
-- 
CREATE PROCEDURE delete_trainer(IN trainer_ int)
BEGIN
	delete from trainer where trainer_id = trainer_;
END // 

-- Examples
CALL add_trainer("kathleen"); // 
CALL add_pokemon_to_trainer((select trainer_id from trainer where trainer_name = "kathleen"), 45); // 
CALL filter_by_trainer((select trainer_id from trainer where trainer_name = "kathleen")); //
CALL add_pokemon_to_trainer((select trainer_id from trainer where trainer_name = "kathleen"), 450); //
CALL filter_by_trainer((select trainer_id from trainer where trainer_name = "kathleen")); //
CALL delete_trainer((select trainer_id from trainer where trainer_name = "kathleen")); //
CALL filter_by_trainer((select trainer_id from trainer where trainer_name = "kathleen")); //

Delimiter //
DROP PROCEDURE IF EXISTS delete_pokemon; //
-- 
CREATE PROCEDURE delete_pokemon(IN pokemon_ int)
BEGIN
	delete from pokemon where idNum = pokemon_;
    delete from pokemon_type where pokemon_id = pokemon_;
END // 

CALL add_pokemon("test", 1, 1, 1, 1, 1, 1, 1, 1, "grass", "flying"); // 
CALL search_by_name("test"); //
CALL delete_pokemon((select idNum from pokemon where name  = "test")) //
CALL search_by_name("test"); //


DELIMITER //
DROP PROCEDURE IF EXISTS get_effectiveness; 
//
CREATE PROCEDURE get_effectiveness(IN attack_ int, IN defense_ int)
BEGIN
SELECT *
FROM effectiveness
WHERE effectiveness.attack_type IN (SELECT type_
										FROM (SELECT * 
												FROM (SELECT pokemon_id, type_1 AS type_
														FROM pokemon_type) AS t1pokemon
														UNION ALL
														(SELECT pokemon_id, type_2 AS type_
														FROM pokemon_type)) AS p_t
										WHERE p_t.pokemon_id = attack_)
	AND 
    effectiveness.defense_type IN (SELECT type_
										FROM (SELECT * 
												FROM (SELECT pokemon_id, type_1 AS type_
														FROM pokemon_type) AS t1
														UNION ALL
														(SELECT pokemon_id, type_2 AS type_
														FROM pokemon_type)) AS p_t
										WHERE p_t.pokemon_id = defense_)
ORDER BY effectiveness.effectiveness DESC;
END; 
//

CALL get_effectiveness(15, 16); //
CALL get_effectiveness(23, 28); // 
CALL get_effectiveness(28, 23); //
CALL get_effectiveness(32, 35); //


DELIMITER //
DROP PROCEDURE IF EXISTS add_gen;
//

DELIMITER //
CREATE PROCEDURE add_gen(IN gen_ INT, IN intro_ DATE)
BEGIN
DECLARE max_gen int;
DECLARE max_date DATE;
SELECT MAX(date_introduced) into max_date
FROM generation;
SELECT MAX(generation) into max_gen
FROM generation;
IF(gen_ > max_gen AND intro_ > max_date)
THEN
INSERT INTO generation(generation, date_introduced)
VALUES (gen_, intro_);
END IF;
END; //

CALL add_gen(2, '2021-09-01'); //
CALL add_gen(200, '2002-10-10'); //
CALL add_gen(8, '2020-04-06'); //
//

DROP PROCEDURE IF EXISTS get_evolution; 
//
DELIMITER //
CREATE PROCEDURE get_evolution(IN pokemon_ INT)
BEGIN
DECLARE ev INT;
SELECT evolve_id into ev FROM pokemon WHERE idNum = pokemon_;
IF(ev IS NOT NULL)
THEN
SELECT *
FROM
	(SELECT idNum, name, evolve_id
	FROM pokemon WHERE idNum = pokemon_) AS e;
END IF;
END; 
//

CALL get_evolution(1);//

//
DROP PROCEDURE IF EXISTS get_by_id; 
//
DELIMITER //
CREATE PROCEDURE get_by_id(IN id int)
BEGIN
	select pokemon.*, type_1, type_2 from pokemon join pokemon_type on idNum = pokemon_id
	WHERE idNum = id;
END // 
//
Call get_by_id(1);//

//
DROP PROCEDURE IF EXISTS get_by_id_new; 
//
DELIMITER //
CREATE PROCEDURE get_by_id_new(IN p_id int)
BEGIN
	select pokemon.*, type_1, type_2 from pokemon join pokemon_type on idNum = pokemon_id
	WHERE idNum = p_id;
END // 
//
Call get_by_id_new(1);//

DROP PROCEDURE IF EXISTS update_trainers; 
//
DELIMITER //
CREATE PROCEDURE update_trainers(IN t_id int, IN name_ varchar(45))
BEGIN
UPDATE trainer
	SET trainer_name=name_
	WHERE trainer_id=t_id;
END // 
//
call add_trainer("joshuaA");//
call add_pokemon_to_trainer(32,100);//
Call update_trainers(32,"newJosh");//

