package com.example.pokemonapp

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pokemonapp.databinding.ActivityPokemonBinding
import com.squareup.picasso.Picasso

class PokemonActivity : AppCompatActivity() {

    companion object {
        const val POKEMON_TAG = "Pokemon"
        fun start(pokemon: Pokemon, context: Context) {
            val intent = Intent(context, PokemonActivity::class.java)
            intent.putExtra(POKEMON_TAG, pokemon.toJson())
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityPokemonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pokemonJson = intent.getStringExtra(POKEMON_TAG)
        if (pokemonJson != null) {
            val pokemon = Pokemon.fromJson(pokemonJson)
            Picasso.get().load(pokemon.sprites.frontDefault).into(binding.ivPokemon)
            pokemon.iniciarVidas()
            binding.tvPokemonNombre.text ="Nombre: " +pokemon.nameCapitalized()
            binding.tvPokemonAltura.text = "Altura: "+pokemon.height.toString()
            binding.tvPokemonPeso.text = "Peso: "+pokemon.weight.toString()
            binding.progressBar.max = pokemon.vidaMaxima
            binding.progressBar.progress = pokemon.vidaActual
            binding.psVida.text ="PS: " +pokemon.vidaActual.toString()+"/"+pokemon.vidaMaxima.toString()

            binding.progressBar.apply {
                max = pokemon.vidaMaxima
                progress = pokemon.vidaActual
                progressTintList = ColorStateList.valueOf(
                    when{
                        pokemon.vidaActual < pokemon.vidaMaxima*0.15 -> Color.RED
                        pokemon.vidaActual < pokemon.vidaMaxima*0.5 -> Color.YELLOW
                        else -> Color.GREEN
                    }
                )
            }
            val image1 = pokemon.obtenerImagenTipo1()
            if (image1 != null)
                binding.ivTipo1.setImageResource(image1)
            else
                binding.ivTipo1.setImageDrawable(null)

            val image2 = pokemon.obtenerImagenTipo2()
            if (image2 != null)
                binding.ivTipo2.setImageResource(image2)
            else
                binding.ivTipo2.setImageDrawable(null)
        } else {
            Toast.makeText(this, "No se ha recibido ningún Pokémon", Toast.LENGTH_LONG).show()
            finish()
        }
    }
}