package pt.licious.nerfthis.specs

import com.pixelmonmod.pixelmon.api.pokemon.ISpecType
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec
import com.pixelmonmod.pixelmon.api.pokemon.SpecValue
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon
import net.minecraft.nbt.NBTTagCompound

class Undexable : SpecValue<Boolean>("key", true), ISpecType {

    fun register() {
        PokemonSpec.extraSpecTypes.add(this)
    }

    override fun getValueClass(): Class<Boolean> {
        return Boolean::class.java
    }

    override fun apply(p0: EntityPixelmon) {
        apply(p0.pokemonData)
    }

    override fun apply(p0: Pokemon) {
        p0.addSpecFlag("undexable")
    }

    override fun clone(): SpecValue<Boolean> {
        return parse(value.toString())
    }

    override fun matches(p0: EntityPixelmon): Boolean {
        return matches(p0.pokemonData)
    }

    override fun matches(p0: Pokemon): Boolean {
        return p0.hasSpecFlag("undexable")
    }

    override fun writeToNBT(p0: NBTTagCompound, p1: SpecValue<*>) {
        p0.setBoolean(key, value)
    }

    override fun toParameterForm(p0: SpecValue<*>): String {
        return "$key:${p0.value}"
    }

    override fun parse(p0: String?): SpecValue<Boolean> {
        val undexable = Undexable()
        if (p0.isNullOrEmpty())
            undexable.value = false
        return undexable
    }

    override fun getKeys(): MutableList<String> {
        return mutableListOf("undexable")
    }

    override fun getSpecClass(): Class<out SpecValue<*>> {
        return javaClass
    }

    override fun readFromNBT(p0: NBTTagCompound?): SpecValue<*> {
        return Undexable()
    }

}