def call_get_effectiveness(cnx, attack, defense):
    valid_ = call_get_all_ids(cnx) 
    if (attack not in valid_ or defense not in valid_):
        raise ValueError("id not a valid pokemon")
    cursor = cnx.cursor()
    query = "call get_effectiveness(%s, %s)"
    args = (attack, defense)
    cursor.execute(query, args)
    # ... retrieve data ...
    returnable = list()
    for row in cursor.fetchall():
        returnable.append(row)

    cursor.close()

    return returnable

def call_get_evolution(cnx,evolution):
    valid_ = call_get_all_ids(cnx) 
    if (evolution not in valid_):
         raise ValueError("id not a valid pokemon")
    cursor = cnx.cursor()
    query = "call get_evolution(%s)"
    cursor.execute(query, evolution)
    # ... retrieve data ...
    returnable = list()
    for row in cursor.fetchall():
        returnable.append(row)

    cursor.close()

    return returnable
    
def call_get_by_id(cnx,id):
    valid_ = call_get_all_ids(cnx) 
    if (id not in valid_):
         raise ValueError("id not a valid pokemon")
    cursor = cnx.cursor()
    query = "call get_by_id(%s)"
    cursor.execute(query, id)
    # ... retrieve data ...
    
    returnable = list()
    for row in cursor.fetchall():
        returnable.append(row)

    cursor.close()

    return returnable
    
def call_get_winner(cnx,pokemon1, pokemon2):
    valid_ = call_get_all_ids(cnx) 
    if (pokemon1 not in valid_ and pokemon2 not in valid_):
         raise ValueError("id not a valid pokemon")
    effectiveness = list()
    effectiveness.append(call_get_effectiveness(cnx,pokemon1, pokemon2))
    effectiveness.append(call_get_effectiveness(cnx,pokemon2, pokemon1))
    
    val = list()
    for i in range(len(effectiveness)):
        for j in range(len(effectiveness[i])):
            tmp = dict(effectiveness[i][j])
            val.append(tmp['effectiveness'])
    returnable = list()
    if val.index(max(val)) < (.5 * len(val)): #if the greatest is from the first pokemon given
        returnable.append(call_get_by_id(cnx, pokemon1))
    else:
        returnable.append(call_get_by_id(cnx, pokemon2))
    return returnable
    
def call_get_all(cnx):
    cursor = cnx.cursor()
    query = "select * from pokemon"
    cursor.execute(query)
    # ... retrieve data ...
    returnable = list()
    for row in cursor.fetchall():
        returnable.append(row)

    cursor.close()

    return returnable

def call_get_all_ids(cnx):
    cursor = cnx.cursor()
    query = "select distinct idNum from pokemon"
    cursor.execute(query)
    # ... retrieve data ...
    returnable = list()
    for row in cursor.fetchall():
        returnable.append(row['idNum'])

    cursor.close()

    return returnable
    
def call_get_all_trainer_ids(cnx):
    cursor = cnx.cursor()
    query = "select distinct trainer_id from trainer"
    cursor.execute(query)
    # ... retrieve data ...
    returnable = list()
    for row in cursor.fetchall():
        returnable.append(row['trainer_id'])

    cursor.close()

    return returnable
    
def call_get_all_trainer_pokemon_ids(cnx):
    cursor = cnx.cursor()
    query = "select * from trainer_pokemon"
    cursor.execute(query)
    # ... retrieve data ...
    returnable = list()
    for row in cursor.fetchall():
        returnable.append((row['trainer_id'], row['pokemon_id']))

    cursor.close()

    return returnable