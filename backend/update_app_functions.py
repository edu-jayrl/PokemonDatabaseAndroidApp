from get_app_functions import call_get_all_trainer_ids 
        
def call_update_trainer(cnx, trainer_id, new_name):
    trainer_ids = call_get_all_trainer_ids(cnx)
    valid_ = isinstance(new_name, str) and  len(new_name) < 45 and (trainer_id in trainer_ids)
    if (not valid_):
         raise ValueError("trainer not in db")
    cursor = cnx.cursor()
    query = "call update_trainers(%s, %s)"
    args = (trainer_id, new_name)
    cursor.execute(query, args)
    cnx.commit()
    query = "select * from trainer"
    cursor.execute(query)
    returnable = list()
    for row in cursor.fetchall():
        returnable.append(row)

    cursor.close()
    
    # Make sure data is committed to the database
    return returnable