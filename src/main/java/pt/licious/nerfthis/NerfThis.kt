package pt.licious.nerfthis

import com.pixelmonmod.pixelmon.Pixelmon
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.event.FMLServerStartingEvent
import org.apache.logging.log4j.LogManager
import pt.licious.nerfthis.commands.Reload
import pt.licious.nerfthis.config.Config
import pt.licious.nerfthis.events.Events
import pt.licious.nerfthis.specs.Undexable
import java.io.File

@Mod(
        modid = NerfThis.MOD_ID,
        name = NerfThis.MOD_NAME,
        version = NerfThis.VERSION,
        modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter",
        acceptableRemoteVersions = "*",
        dependencies = "after:" + Pixelmon.MODID,
        acceptedMinecraftVersions = "[1.12.2]"
)

object NerfThis {

    const val MOD_ID = "nerfthis"
    const val MOD_NAME = "NerfThis"
    const val VERSION = "1.6"
    val LOG = LogManager.getLogger(MOD_NAME)
    lateinit var configFile: File

    @Mod.EventHandler
    fun onPreInit(e: FMLPreInitializationEvent) {
        val configDir = File(e.modConfigurationDirectory.toString() + "/" + MOD_ID)
        configDir.mkdir()
        configFile = File(configDir, "config.conf")
        Config.init()
        Undexable().register()
        Pixelmon.EVENT_BUS.register(Events())
    }

    @Mod.EventHandler
    fun onServerStarting(e: FMLServerStartingEvent) {
        e.registerServerCommand(Reload())
    }

}