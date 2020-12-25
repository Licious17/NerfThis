package pt.licious.nerfthis.events

import com.pixelmonmod.pixelmon.api.events.BreedEvent
import com.pixelmonmod.pixelmon.api.events.PokedexEvent
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.util.text.TextComponentString
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import pt.licious.nerfthis.config.Config


class Events {

    companion object {

        private const val BREEDER_TAG = "BreederUUID"

    }

    @SubscribeEvent
    fun onDex(e: PokedexEvent) {
        FMLCommonHandler.instance().minecraftServerInstance.playerList.getPlayerByUUID(e.uuid)?.let { player ->
            val pokemon = e.pokemon
            if (e.isCanceled || pokemon == null || Config.isExempt(player) || Config.isExempt(pokemon) || e.isCausedByCapture || pokemon.isOriginalTrainer(player)) return
            e.isCanceled = if (e.isCausedByStorageMovement)
                true
            else if (pokemon.hasSpecFlag("undexable"))
                true
            else if (e.isCausedByEggHatching && Config.eggGifts && notBreeder(player, pokemon))
                true
            else if (e.isCausedByPlayerTrade && Config.playerTrade)
                true
            else if (e.isCausedByEvolution && Config.evolution)
                true
            else e.isCausedBySidemod && Config.sidemod
        }
    }

    private fun notBreeder(player: EntityPlayerMP, pokemon: Pokemon): Boolean {
        return pokemon.persistentData.hasUniqueId(BREEDER_TAG) && player.uniqueID == pokemon.persistentData.getUniqueId(BREEDER_TAG)
    }

    @SubscribeEvent
    fun onEggCollect(e: BreedEvent.CollectEgg) {
        FMLCommonHandler.instance().minecraftServerInstance.playerList.getPlayerByUUID(e.owner)?.let { player ->
            if (e.isCanceled || !Config.nonOTBreed || Config.isExempt(player) || Config.isExempt(e.egg)) return
            e.egg.persistentData.setUniqueId(BREEDER_TAG, e.owner)
        }
    }

    @SubscribeEvent
    fun onRanchDeposit(e: BreedEvent.AddPokemon) {
        FMLCommonHandler.instance().minecraftServerInstance.playerList.getPlayerByUUID(e.owner)?.let { player ->
            val pokemon = e.pokemon
            if (e.isCanceled || pokemon.isOriginalTrainer(player) || !Config.nonOTBreed || Config.isExempt(player) || Config.isExempt(pokemon)) return
            e.isCanceled = true
            Config.sendNonOTBreedMessage(player, pokemon)
        }
    }

}