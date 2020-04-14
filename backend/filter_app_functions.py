from get_app_functions import call_get_all_ids, call_get_all_trainer_ids 
 
#type 
def grab_types(cnx):
    valid = list()
    cur = cnx.cursor()
    stmt_select = "select distinct types from types"
    cur.execute(stmt_select)
    for row in cur.fetchall():
        valid.append(row["types"])
    cur.close()
    return valid

def is_valid(item, l):
    str_names = ', '.join(l)
    return item in l

def call_filter_by_type(cnx, type):
    valid_types = grab_types(cnx)
    if (not(is_valid(type, valid_types))):
         raise ValueError("type not in db") 
    cursor = cnx.cursor()
    query = "call filter_by_type(%s)"
    cursor.execute(query, type)
    # ... retrieve data ...
    
    filtered = list()
    for row in cursor.fetchall():
        filtered.append(row)
    cursor.close()
    return filtered

# generation 
def grab_generations(cnx):
    valid = list()
    cur = cnx.cursor()
    stmt_select = "select distinct generation from generation"
    cur.execute(stmt_select)
    for row in cur.fetchall():
        valid.append(row["generation"])
    cur.close()
    return valid

def call_filter_by_generation(cnx, generation):
    valid_ = grab_generations(cnx)
    if (generation not in valid_):
         raise ValueError("generation not in db")
    cursor = cnx.cursor()
    query = "call filter_by_generation(%s)"
    cursor.execute(query, generation)
    # ... retrieve data ...

    filtered = list()
    for row in cursor.fetchall():
        filtered.append(row)
    cursor.close()
    return filtered

#trainer

def call_filter_by_trainer(cnx, trainer):
    valid_ = call_get_all_trainer_ids(cnx)
    if (trainer not in valid_):
         raise ValueError("trainer id not in db")
    cursor = cnx.cursor()
    query = "call filter_by_trainer(%s)"
    cursor.execute(query, trainer)
    # ... retrieve data ...
    filtered = list()
    for row in cursor.fetchall():
        filtered.append(row)
    cursor.close()
    return filtered
#game
def grab_games(cnx):
    valid = list()
    cur = cnx.cursor()
    stmt_select = "select distinct game_id from game"
    cur.execute(stmt_select)
    for row in cur.fetchall():
        valid.append(row["game_id"])
    cur.close()
    return valid

def call_filter_by_game(cnx, game):
    valid_ = grab_games(cnx)
    if (game not in valid_):
         raise ValueError("game id not in db")
    cursor = cnx.cursor()
    query = "call filter_by_game(%s)"
    cursor.execute(query, game)
    # ... retrieve data ...
    filtered = list()
    for row in cursor.fetchall():
        filtered.append(row)
    cursor.close()
    return filtered
    
#name
def grab_names(cnx):
    valid = list()
    cur = cnx.cursor()
    stmt_select = "select distinct name from pokemon"
    cur.execute(stmt_select)
    for row in cur.fetchall():
        valid.append(row["name"])
    cur.close()
    return valid

def is_valid(item, l):
    str_names = ', '.join(l)
    return item in l

def call_search_by_name(cnx,name):
    valid_ = grab_names(cnx)
    if (name.title() not in valid_):
         raise ValueError("name not in db")
    cursor = cnx.cursor()
    query = "call search_by_name(%s)"
    cursor.execute(query, name)
    # ... retrieve data ...
    returnable = list()
    
    for row in cursor.fetchall():
        returnable.append(row)
    cursor.close()
    return returnable

def call_search_by_name_no_title(cnx,name):
    valid_ = grab_names(cnx)
    if (name in valid_):
         return True
    return False
