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

    val breederTag = "BreederUUID"

    @SubscribeEvent
    fun onDex(e: PokedexEvent) {
        val player = FMLCommonHandler.instance().minecraftServerInstance.playerList.getPlayerByUUID(e.uuid)
        val pokemon = e.pokemon
        if (e.isCanceled || player == null || pokemon == null || Config.isExempt(player) || Config.isExempt(pokemon)) return
        if (pokemon.hasSpecFlag("undexable"))
            e.isCanceled = true
        else if (!pokemon.isOriginalTrainer(player)) {
            if (Config.eggGifts && e.isCausedByEggHatching && notBreeder(player, pokemon))
                e.isCanceled = true
            else if (Config.playerTrade && e.isCausedByPlayerTrade)
                e.isCanceled = true
            else if (Config.sidemod && e.isCausedBySidemod)
                e.isCanceled = true
            else if (Config.evolution && e.isCausedByEvolution)
                e.isCanceled = true
            else if (!e.isCausedByCapture && e.isCausedByStorageMovement)
                e.isCanceled = true
        }
    }

    private fun notBreeder(player: EntityPlayerMP, pokemon: Pokemon): Boolean {
        return pokemon.persistentData.hasUniqueId(breederTag) && player.uniqueID == pokemon.persistentData.getUniqueId(breederTag)
    }

    @SubscribeEvent
    fun onEggCollect(e: BreedEvent.CollectEgg) {
        val player = FMLCommonHandler.instance().minecraftServerInstance.playerList.getPlayerByUUID(e.owner)
        if (e.isCanceled || player == null || !Config.nonOTBreed || Config.isExempt(player) || Config.isExempt(e.egg)) return
        e.egg.persistentData.setUniqueId(breederTag, e.owner)
    }

    @SubscribeEvent
    fun onRanchDeposit(e: BreedEvent.AddPokemon) {
        val player = FMLCommonHandler.instance().minecraftServerInstance.playerList.getPlayerByUUID(e.owner)
        if (e.isCanceled || player == null || !Config.nonOTBreed || Config.isExempt(player) || Config.isExempt(e.pokemon)) return
        e.isCanceled = true
        player.sendMessage(TextComponentString(Config.nonOTBreedMessage))
    }

}