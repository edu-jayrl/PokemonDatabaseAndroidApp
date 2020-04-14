package com.peluso.pokemondatabase

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.github.kittinunf.fuel.httpGet
import com.peluso.pokemondatabase.model.*
import kotlinx.android.synthetic.main.activity_create_pokemon.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var db: Database
    private var ip: String = ""
    private var connected = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // grabbing the locally saved ip from previous versions
        val sharedPref = this.getSharedPreferences("UserInfo",0)
        ip = sharedPref.getString("ip","localhost")!!
        // registering each button click to launch a new Activity
        fightButton.setOnClickListener {
            val intent = Intent(this,FightActivity::class.java)
            startActivity(intent)
        }
        selectButton.setOnClickListener {
            val intent = Intent(this, SelectActivity::class.java)
            startActivity(intent)
        }


        // delete buttons should lead to the dialog launch
        deletePokemon.setOnClickListener {singleInputDialog(SingleInput.POKEMON) }
        deleteTrainer.setOnClickListener { singleInputDialog(SingleInput.TRAINER) }
        // create buttons and their respective launchers
        createTrainerToPokemon.setOnClickListener { doubleInputDialog(TwoInputDialog.TRAINER_TO_POKEMON) }
        createGeneration.setOnClickListener { doubleInputDialog(TwoInputDialog.GENERATION) }
        createPokemon.setOnClickListener { createPokemonDialog() }
        createTrainer.setOnClickListener { createTrainerDialog() }
        // update button and respective launcher
        updateTrainerButton.setOnClickListener { doubleInputDialog(TwoInputDialog.UPDATE_TRAINER) }

        // allow user to enter their IP address in the app
        connectButton.setOnClickListener { singleInputDialog(SingleInput.CONNECTION) }
    }
    private fun connectToDatabase(id: String) {
        //Connect to the client and attempt a simple query
        this.ip = id
        APIClient.setIPAddress(id)
        val client = APIClient.retrofitInstance!!.create(APIService::class.java)
        db = Database(this,client)
        db.pingDatabase(this::handshake)
    }

    private fun handshake(success: Boolean) {
        if(success){
            connectionTextview.text = "Connected to Database Successfully!"
            connected = true
        }
    }

    private fun createTrainerDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.create_trainer_dialog,null)
        builder.setView(view)

        val createTrainerDialogButton: Button = view.findViewById(R.id.create_trainer_button)
        val createTrainerInput: EditText = view.findViewById(R.id.create_trainer_edittext)

        val dialog = builder.create()

        createTrainerDialogButton.setOnClickListener {
            val name = createTrainerInput.text.toString()
            db.addTrainer(name)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun createPokemonDialog() {
        val builder = android.app.AlertDialog.Builder(this)
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.activity_create_pokemon,null)
        builder.setView(view)

        // all those dang widgets necessary for making a pokemon
        val nameInput: EditText = view.findViewById(R.id.create_NAME_edittext)
        val hpInput: EditText = view.findViewById(R.id.create_HP_edittext)
        val speedInput: EditText = view.findViewById(R.id.create_SPEED_edittext)
        val attackInput: EditText = view.findViewById(R.id.create_ATTACK_edittext)
        val specialAttackInput: EditText = view.findViewById(R.id.create_SPECIAL_ATTACK_edittext)
        val defenseInput: EditText = view.findViewById(R.id.create_DEFENSE_edittext)
        val specialDefenseInput: EditText = view.findViewById(R.id.create_SPECIAL_DEFENSE_edittext)
        val generationInput: EditText = view.findViewById(R.id.create_GENERATION_edittext)
        val type1Input: EditText = view.findViewById(R.id.create_type1_edittext)
        val type2Input: EditText = view.findViewById(R.id.create_type2_edittext)
        val createPokemonButton: Button = view.findViewById(R.id.create_pokemon_dialog_button)

        // creating dialog
        val dialog = builder.create()

        // creating our lil pokemon to add on the button press
        createPokemonButton.setOnClickListener {
            val name = nameInput.text.toString()
            val hp = hpInput.text.toString().toInt()
            val speed = speedInput.text.toString().toInt()
            val attack = attackInput.text.toString().toInt()
            val specialAttack = specialAttackInput.text.toString().toInt()
            val defense = defenseInput.text.toString().toInt()
            val specialDefense = specialDefenseInput.text.toString().toInt()
            val generation = generationInput.text.toString().toInt()
            val type1 = type1Input.text.toString()
            val type2 = type2Input.text.toString()
            db.addPokemon(name,hp,speed,attack,specialAttack,defense,specialDefense,0,generation,type1,type2)
        }

        dialog.show()
    }

    // function responsible for launching delete dialog and relaying message to maintextview
    private fun singleInputDialog(singleInputDialog: SingleInput){
        val builder = android.app.AlertDialog.Builder(this)
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.delete_dialog_layout,null)
        builder.setView(view)

        val deleteDialogButton: Button = view.findViewById(R.id.deleteDialogButton)
        val deleteDialogEditText: EditText = view.findViewById(R.id.delete_id_editText)
        val singleInputMainText: TextView = view.findViewById(R.id.singleInputMainText)
        // ui changes depending on input
        if(singleInputDialog == SingleInput.CONNECTION){
            deleteDialogEditText.hint = "IP Address"
            singleInputMainText.text = "Please enter IP address of machine hosting Flask server (192.168.X.X)"
            deleteDialogButton.text = "Connect"
            deleteDialogEditText.setText(ip)
        }
        // creating dialog
        val dialog = builder.create()

        deleteDialogButton.setOnClickListener {
            val ip = deleteDialogEditText.text.toString()
            when(singleInputDialog){
                SingleInput.POKEMON -> db.deletePokemon(ip.toInt())
                SingleInput.TRAINER -> db.deleteTrainer(ip.toInt())
                SingleInput.CONNECTION -> { connectToDatabase(ip) }
            }
        }
        dialog.show()
    }

    private fun doubleInputDialog(type: TwoInputDialog){
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.create_two_input_dialog,null)
        builder.setView(view)

        val createDialogButton: Button = view.findViewById(R.id.createDialogButton)
        val editText1: EditText = view.findViewById(R.id.create_edit_text_1)
        val editText2: EditText = view.findViewById(R.id.create_edit_text_2)
        val mainTextView: TextView = view.findViewById(R.id.create_dialog_main_text)

        // slight UI changes depending on what you're uploading
        when(type){
            TwoInputDialog.TRAINER_TO_POKEMON -> {
                mainTextView.text = "Enter Trainer ID and Pokemon ID"
                editText1.hint = "T_id"
                editText2.hint = "P_id"
            }
            TwoInputDialog.GENERATION -> {
                mainTextView.text = "Enter Generation number and Date"
                editText1.hint = "Gen #"
                editText2.hint = "Date"
            }
            TwoInputDialog.UPDATE_TRAINER -> {
                mainTextView.text = "Enter Trainer ID and new name"
                editText1.hint = "Trainer ID"
                editText2.hint = "Name"
            }
        }
        val dialog = builder.create()
        createDialogButton.setOnClickListener {
            when(type){
                TwoInputDialog.TRAINER_TO_POKEMON -> {
                    // set the edit texts to IDs and call database object
                    val t_id = editText1.text.toString().toInt()
                    val p_id = editText2.text.toString().toInt()
                    db.addPokemonToTrainer(t_id,p_id)
                }
                TwoInputDialog.GENERATION -> {
                    // set edit texts to gen number and date and call the database operation
                    val gen = editText1.text.toString().toInt()
                    val date = editText2.text.toString()
                    db.add_gen(gen,date)
                }
                TwoInputDialog.UPDATE_TRAINER -> {
                    val t_id = editText1.text.toString().toInt()
                    val name = editText2.text.toString()
                    db.updateTrainer(t_id,name)
                }
            }
        }
        dialog.show()
    }

    override fun onPause() {
        // saving the ip from previous sessions for easy connecting
        val settings = getSharedPreferences("UserInfo", 0)
        with(settings.edit()){
            putString("ip",ip)
            commit()
        }
        super.onPause()
    }

}
