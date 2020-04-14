from get_app_functions import call_get_all_ids,call_get_all_trainer_ids, call_get_all_trainer_pokemon_ids
from filter_app_functions import call_search_by_name_no_title, grab_generations, grab_types, is_valid

def call_add_trainer(cnx,name):
    valid_ = (name.isalpha() and len(name) < 45)
    if (not valid_):
         raise ValueError("name is not a string or longer than 45 characters")
    cursor = cnx.cursor()
    query = "call add_trainer(%s)"
    cursor.execute(query, name)
    # ... retrieve data ...
    query = "select * from trainer"
    cursor.execute(query)
    returnable = list()
    for row in cursor.fetchall():
        returnable.append(row)

    cursor.close()
    
    # Make sure data is committed to the database
    cnx.commit()
    
    return returnable
    
def pokemon_valididty(cnx,name, hp, speed, attack, generation, type1, defense, special_attack = None, special_defense = None, evolve_id = None, type2 = None):
    gen_ids = grab_generations(cnx)
    valid_types = grab_types(cnx)
    exists = call_search_by_name_no_title(cnx,name)
    return ((not(exists))
        and is_valid(type1, valid_types)
        and (isinstance(hp,int))
        and (isinstance(speed,int)) 
        and (isinstance(attack,int)) 
        and (isinstance(defense,int)) 
        and (generation in gen_ids))
        
    
def call_add_pokemon(cnx,name, hp, speed, attack, generation, type1, defense, special_attack=None, special_defense = None, evolve_id = None, type2 = None):
    valid_ = pokemon_valididty(cnx,name, hp, speed, attack, generation, type1, defense, special_attack,  special_defense, evolve_id, type2)
    if (not(valid_)):
        raise ValueError("duplicate name or one or more values not allowed, please go over the types for all inputs")
    cursor = cnx.cursor()
    query = "call add_pokemon(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)"
    args = (name, hp, speed, attack, special_attack, defense, special_defense, evolve_id, generation, type1, type2)
    cursor.execute(query, args)
    
    query = "select * from pokemon"
    cursor.execute(query)
    returnable = list()
    for row in cursor.fetchall():
        returnable.append(row)

    cursor.close()
    
    # Make sure data is committed to the database
    cnx.commit()
    return returnable

def call_add_pokemon_to_trainer(cnx,trainer_id, pokemon_id):
    t_ids = call_get_all_trainer_ids(cnx)
    p_ids = call_get_all_ids(cnx)
    combo = call_get_all_trainer_pokemon_ids(cnx)
    valid_ = ((trainer_id in t_ids) and (pokemon_id in p_ids) and ((trainer_id,pokemon_id) not in combo))
    if (not valid_):
         raise ValueError("trainer or pokemon not in db")
    cursor = cnx.cursor()
    query = "call add_pokemon_to_trainer(%s, %s)"
    args = (trainer_id, pokemon_id)
    cursor.execute(query, args)
    # ... retrieve data ...
    
    query = "select * from trainer_pokemon"
    cursor.execute(query)
    returnable = list()
    for row in cursor.fetchall():
        returnable.append(row)

    cursor.close()
    
    # Make sure data is committed to the database
    cnx.commit()
    return returnable
    
def call_add_gen(cnx, gen, date):
    gen_ids = grab_generations(cnx)
    valid_ = isinstance(gen, int) and isinstance(date, str) and (gen not in gen_ids)
    if (not valid_):
         raise ValueError("pokemon not in db")
    cursor = cnx.cursor()
    query = "call add_gen(%s, %s)"
    args = (gen, date)
    cursor.execute(query, args)
    cnx.commit()

    query = "select * from generation"
    cursor.execute(query)
    returnable = list()
    for row in cursor.fetchall():
        returnable.append(row)

    cursor.close()
    
    # Make sure data is committed to the database
    cnx.commit()
    return returnable
