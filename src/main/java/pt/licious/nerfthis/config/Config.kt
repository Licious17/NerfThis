package pt.licious.nerfthis.config

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec
import info.pixelmon.repack.ninja.leaping.configurate.ConfigurationNode
import info.pixelmon.repack.ninja.leaping.configurate.ConfigurationOptions
import info.pixelmon.repack.ninja.leaping.configurate.commented.CommentedConfigurationNode
import info.pixelmon.repack.ninja.leaping.configurate.hocon.HoconConfigurationLoader
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraftforge.server.permission.PermissionAPI
import pt.licious.nerfthis.NerfThis
import java.nio.file.Files

object Config {

    private var permissionOverride = "nerfthis.admin.override"
    var playerTrade = true
    var evolution = true
    var sidemod = true
    var nonOTBreed = true
    var nonOTBreedMessage = "&2Professor Oak &f» &c%player% this %pokemon% does not seem to want to breed, it seems to miss %OT%"
    var eggGifts = true
    private var whitelist = mutableListOf<PokemonSpec>()

    lateinit var loader: HoconConfigurationLoader
    lateinit var node: CommentedConfigurationNode

    fun init() {
        val file = NerfThis.configFile
        if (!file.exists()) {
            val input = Config::class.java.classLoader.getResourceAsStream("assets/config/config.conf")
            input?.let { Files.copy(input, file.toPath()) } ?: run {
                NerfThis.LOG.warn("NerfThis default config was somehow missing from the assets? Downloading the plugin again will fix this but for now the plugin will use its hardcoded config")
                return
            }
        }
        load()
    }

    fun load() {
        loader = HoconConfigurationLoader.builder().setFile(NerfThis.configFile).build()
        node = loader.load(ConfigurationOptions.defaults().setShouldCopyDefaults(true))
        permissionOverride = node.getNode("permissionOverride").string
        playerTrade = node.getNode("playerTrade").boolean
        evolution = node.getNode("evolution").boolean
        sidemod = node.getNode("sidemod").boolean
        nonOTBreed = node.getNode("nonOTBreed").boolean
        nonOTBreedMessage = node.getNode("nonOTBreedMessage").string.replace(Regex("&([0-9a-fk-or])"),"\u00a7$1")
        eggGifts = node.getNode("eggGifts").boolean
        whitelist.clear()
        node.getNode("whitelist").childrenList.stream()
                .map(ConfigurationNode::getString)
                .forEach { whitelist.add(PokemonSpec(it)) }
    }

    fun isExempt(player: EntityPlayerMP): Boolean {
        return permissionOverride.isNotEmpty() && (PermissionAPI.hasPermission(player, permissionOverride))
    }

    fun isExempt(pokemon: Pokemon): Boolean {
        return whitelist.any { it.matches(pokemon) }
    }

}