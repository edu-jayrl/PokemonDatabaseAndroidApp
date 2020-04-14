from get_app_functions import call_get_all_ids, call_get_all_trainer_ids 

def call_delete_trainer(cnx, trainer_id):
    valid_ = call_get_all_trainer_ids(cnx)
    if (trainer_id not in valid_):
         raise ValueError("trainer not in db")
    cursor = cnx.cursor()
    query = "call delete_trainer(%s)"
    cursor.execute(query, trainer_id)
    
    query = "select * from trainer"
    cursor.execute(query)
    returnable = list()
    for row in cursor.fetchall():
        returnable.append(row)

    cursor.close()
    # Make sure data is committed to the database
    cnx.commit()
    return returnable

def call_delete_pokemon(cnx, pokemon_id):
    valid_ = call_get_all_ids(cnx)
    if (pokemon_id not in valid_):
         raise ValueError("pokemon not in db")
    cursor = cnx.cursor()
    query = "call delete_pokemon(%s)"
    cursor.execute(query, pokemon_id)   

    query = "select * from pokemon"
    cursor.execute(query)
    returnable = list()
    for row in cursor.fetchall():
        returnable.append(row)

    cursor.close()
    # Make sure data is committed to the database
    cnx.commit()
    return returnable