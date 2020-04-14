#!/usr/bin/env python
# -*- coding: utf-8 -*-

import flask
from flask import request, jsonify
import pymysql
import cryptography

import filter_app_functions
import add_app_functions
import delete_app_functions
import get_app_functions
import update_app_functions

cnx = pymysql.connect(
        host='localhost',
        user="root",
        password="password",
        db='pokemon_db',
        charset='utf8mb4',
        cursorclass=pymysql.cursors.DictCursor)

app = flask.Flask(__name__)
app.config["DEBUG"] = True

@app.route('/api/v1/database/home', methods=['GET'])
# TEST http://127.0.0.1:5000/api/v1/database/
def api_home():
    return jsonify("successfully connected to the database")

@app.route('/api/v1/database/f_type', methods=['GET'])
# TEST http://127.0.0.1:5000/api/v1/database/f_type?type=water
def api_f_type():
    if 'type' in request.args:
        type = request.args['type']
    else:
        return "Error: No type field provided. Please specify an type."
    try:
        results = filter_app_functions.call_filter_by_type(cnx, type)
    except ValueError:
        results =("type not in db")
    # Use the jsonify function from Flask to convert our list of
    # Python dictionaries to the JSON format.
    return jsonify(results)

@app.route('/api/v1/database/f_gen', methods=['GET'])
#TEST http://127.0.0.1:5000/api/v1/database/f_gen?gen=1
def api_f_gen():
    if 'gen' in request.args:
        if (not request.args['gen'].isdigit()):
            return "Error: non int provided"
        gen = int(request.args['gen'])
    else:
        return "Error: No generation field provided. Please specify an generation. or non int provided"
    try:
        results = filter_app_functions.call_filter_by_generation(cnx, gen)
    except ValueError:
        results =("generation not in db")
    # Use the jsonify function from Flask to convert our list of
    # Python dictionaries to the JSON format.
    return jsonify(results)

@app.route('/api/v1/database/f_trainer', methods=['GET'])
#TEST http://127.0.0.1:5000/api/v1/database/f_trainer?trainer=1
def api_f_trainer():
    if 'trainer' in request.args:
        if (not request.args['trainer'].isdigit()):
            return "Error: non int provided"
        trainer = int(request.args['trainer'])
    else:
        return "Error: No trainer field provided. Please specify an trainer."
    try:
        results = filter_app_functions.call_filter_by_trainer(cnx, trainer)
    except ValueError:
        results =("trainer not in db")
    # Use the jsonify function from Flask to convert our list of
    # Python dictionaries to the JSON format.
    return jsonify(results)

@app.route('/api/v1/database/f_game', methods=['GET'])
#TEST http://127.0.0.1:5000/api/v1/database/f_game?game=1
def api_f_game():
    if 'game' in request.args:
        if (not request.args['game'].isdigit()):
            return "Error: non int provided"
        game = int(request.args['game'])
    else:
        return "Error: No game field provided. Please specify an game."
    try:
        results = filter_app_functions.call_filter_by_game(cnx,game)
    except ValueError:
        results =("game not in db")
    # Use the jsonify function from Flask to convert our list of
    # Python dictionaries to the JSON format.
    return jsonify(results)

@app.route('/api/v1/database/f_name', methods=['GET'])
#TEST http://127.0.0.1:5000/api/v1/database/f_name?name=pikachu
def api_f_name():
    if 'name' in request.args:
        name = request.args['name']
    else:
        return "Error: No name field provided. Please specify an name."
    try:
        results = filter_app_functions.call_search_by_name(cnx,name)
    except ValueError:
        results =("pokemon name not in db")
    # Use the jsonify function from Flask to convert our list of
    # Python dictionaries to the JSON format.
    return jsonify(results)

@app.route('/api/v1/database/a_trainer', methods=['GET'])
#TEST http://127.0.0.1:5000/api/v1/database/a_trainer?name=jesus
def api_a_trainer():
    if 'name' in request.args:
        name = request.args['name']
    else:
        return "Error: No name field provided. Please specify an name."
    try:
        results = add_app_functions.call_add_trainer(cnx,name)
    except ValueError:
        results =("input name not a String")
    # Use the jsonify function from Flask to convert our list of
    # Python dictionaries to the JSON format.
    return jsonify(results)    
@app.route('/api/v1/database/a_pokemon', methods=['GET'])
#TEST http://127.0.0.1:5000/api/v1/database/a_pokemon?name=joshua&hp=100&speed=100&attack=100&special_attack=100&defense=100&special_defense=100&evolve_id=100&generation=100&type1=water&type2=flying
def api_a_pokemon():
    special_attack,  special_defense, evolve_id, type2 = None, None, None, None #set defaults for those fields that can remain null
    if 'name' in request.args:
        name = request.args['name']
 
    if 'hp' in request.args:
        hp = int(request.args['hp'])
        
    if 'speed' in request.args:
        speed = int(request.args['speed'])
        
    if 'attack' in request.args:
        attack = int(request.args['attack'])
    
    if 'special_attack' in request.args:
        special_attack = int(request.args['special_attack'])
    
    if 'defense' in request.args:
        defense = int(request.args['defense'])
    
    if 'special_defense' in request.args:
        special_defense = int(request.args['special_defense'])
    
    if 'evolve_id' in request.args:
        evolve_id = int(request.args['evolve_id'])
    
    if 'generation' in request.args:
        generation = int(request.args['generation'])
    
    if 'type1' in request.args:
        type1 = request.args['type1']
    
    if 'type2' in request.args:
        type2 = request.args['type2']    
    
    # Loop through the data and match results that fit the requested ID.
    # IDs are unique, but other fields might return many results
    try:
        results = add_app_functions.call_add_pokemon(cnx,name, hp, speed, attack, generation, type1, defense, special_attack,  special_defense, evolve_id, type2)
    except ValueError:
        results =("duplicate name or one or more values not allowed, please go over the types for all inputs")
    # Use the jsonify function from Flask to convert our list of
    # Python dictionaries to the JSON format.
    return jsonify(results)

@app.route('/api/v1/database/a_pt', methods=['GET'])
#TEST http://127.0.0.1:5000/api/v1/database/a_pt?t_id=1&p_id=200
def api_a_pt():
    if 't_id' in request.args:
        t_id = int(request.args['t_id'])
    if 'p_id' in request.args:
        p_id = int(request.args['p_id'])
    else:
        return "Error:pokemon id field provided."
    try:
        results = add_app_functions.call_add_pokemon_to_trainer(cnx,t_id, p_id)
    except ValueError:
        results =("one or more inputs are not in the database or entry is duplicate")
    # Use the jsonify function from Flask to convert our list of
    # Python dictionaries to the JSON format.
    return jsonify(results)

@app.route('/api/v1/database/a_gen', methods=['GET'])
#TEST http://127.0.0.1:5000/api/v1/database/a_gen?gen=100&date=2021-09-01
def api_a_gen():
    if 'gen' in request.args:
        gen = int(request.args['gen'])
    if 'date' in request.args:
        date = request.args['date']
    else:
        return "Error: missing field provided."

    # Loop through the data and match results that fit the requested ID.
    # IDs are unique, but other fields might return many results
    try:
        returnable = add_app_functions.call_add_gen(cnx,gen, date)
    except ValueError:
        returnable =("input is not an int or entry is duplicate")
    # Use the jsonify function from Flask to convert our list of
    # Python dictionaries to the JSON format.
    return jsonify(returnable)

@app.route('/api/v1/database/d_trainer', methods=['GET'])

def api_d_trainer():
#TEST http://127.0.0.1:5000/api/v1/database/d_trainer?t_id=2
    if 't_id' in request.args:
        t_id = int(request.args['t_id'])
    else:
        return "Error: No trainer id field provided."
    try:
        results = delete_app_functions.call_delete_trainer(cnx, t_id)
    except ValueError:
        results =("input is not an int or entry is not in db")
    # Use the jsonify function from Flask to convert our list of
    # Python dictionaries to the JSON format.
    return jsonify(results)
    
@app.route('/api/v1/database/d_pokemon', methods=['GET'])
#TEST http://127.0.0.1:5000/api/v1/database/d_pokemon?p_id=1
def api_d_pokemon():
    if 'p_id' in request.args:
        p_id = int(request.args['p_id'])
    else:
        return "Error: No pokemon id field provided."
    try:
        results = delete_app_functions.call_delete_pokemon(cnx, p_id)
    except ValueError:
        results =("input is not an int or entry is not in db")
    # Use the jsonify function from Flask to convert our list of
    # Python dictionaries to the JSON format.
    return jsonify(results)
    
@app.route('/api/v1/database/g_effect', methods=['GET'])
#TEST http://127.0.0.1:5000/api/v1/database/g_effect?attack=10&defense=2
def api_g_effect():
    if 'attack' in request.args:
        if (not request.args['attack'].isdigit()):
            return jsonify("not an int")
        else:
            attack = int(request.args['attack'])
    if 'defense' in request.args:
        if (not request.args['defense'].isdigit()):
            return jsonify("not an int")
        else:
            defense = int(request.args['defense'])
    else:
        return "Error: one or more fields not provided"
    try:
        results = get_app_functions.call_get_effectiveness(cnx, attack, defense)
    except ValueError:
        results =("inputs are not ints or pokemon are not in db")
    # Use the jsonify function from Flask to convert our list of
    # Python dictionaries to the JSON format.
    return jsonify(results)

@app.route('/api/v1/database/g_evolution', methods=['GET'])
#TEST http://127.0.0.1:5000/api/v1/database/g_evolution?evolution=2
def api_g_evolution():
    if 'evolution' in request.args:
        if (not request.args['evolution'].isdigit()):
            return jsonify("not an int")
        evolution = int(request.args['evolution'])
    else:
        return "Error: No trainer field provided. Please specify an trainer."
    try:
        results = get_app_functions.call_get_evolution(cnx,evolution)
    except ValueError:
        results = ("input is not an int or pokemon is not in db")
    # Use the jsonify function from Flask to convert our list of
    # Python dictionaries to the JSON format.
    return jsonify(results)
    
@app.route('/api/v1/database/g_id', methods=['GET'])
#TEST http://127.0.0.1:5000/api/v1/database/g_id?id=9
def api_g_id():
    if 'id' in request.args:
        if (not request.args['id'].isdigit()):
            return jsonify("not an int")
        id = int(request.args['id'])
    else:
        return "Error: one or more fields missing."
    try:
        results = get_app_functions.call_get_by_id(cnx,id)
    except ValueError:
        results = ("input not an int or pokemon is not in db")
    # Use the jsonify function from Flask to convert our list of
    # Python dictionaries to the JSON format.
    return jsonify(results)
    

@app.route('/api/v1/database/g_winner', methods=['GET'])
#TEST http://127.0.0.1:5000/api/v1/database/g_winner?attack=1&defense=10
def api_g_winner():
    if 'attack' in request.args:
        if (not request.args['attack'].isdigit()):
            return jsonify("not an int")
        else:
            attack = int(request.args['attack'])
    if 'defense' in request.args:
        if (not request.args['defense'].isdigit()):
            return jsonify("not an int")
        else:
            defense = int(request.args['defense'])
    else:
        return "Error: one or more fields not provided"
    try:
        results = get_app_functions.call_get_winner(cnx,attack, defense)
    except ValueError:
        results = ("inputs are not ints or pokemon are not in db")

    # Use the jsonify function from Flask to convert our list of
    # Python dictionaries to the JSON format.
    return jsonify(results)

@app.route('/api/v1/database/g_all', methods=['GET'])
# TEST http://127.0.0.1:5000/api/v1/database/g_all
def api_g_all():
    results = get_app_functions.call_get_all(cnx)
    
    # Use the jsonify function from Flask to convert our list of
    # Python dictionaries to the JSON 
    return jsonify(results)


@app.route('/api/v1/database/u_trainer', methods=['GET'])
# Make sure the gen is larger than the previous largest and the date is past the previous latest
#TEST http://127.0.0.1:5000/api/v1/database/u_trainer?t_id=2&name=newname
def api_u_trainer():
    if 't_id' in request.args:
        t_id = int(request.args['t_id'])
    if 'name' in request.args:
        name = request.args['name']
    else:
        return "Error: missing field provided."
    try:
        returnable = update_app_functions.call_update_trainer(cnx,t_id, name)
    except ValueError:
        returnable =("input is not an int or doesn't exist")
    # Use the jsonify function from Flask to convert our list of
    # Python dictionaries to the JSON format.
    return jsonify(returnable)

if __name__ == "__main__":
    app.run(host='0.0.0.0',port=5000)


