package pt.licious.nerfthis.commands

import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.server.MinecraftServer
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextFormatting
import pt.licious.nerfthis.config.Config

class Reload : CommandBase() {

    override fun getName(): String {
        return "ntreload"
    }

    override fun getUsage(sender: ICommandSender): String {
        return "${TextFormatting.RED}Usage: /ntreload"
    }

    override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<String>) {
        if (args.isEmpty()) {
            Config.load()
            sender.sendMessage(TextComponentString("${TextFormatting.WHITE}[${TextFormatting.YELLOW}NerfThis${TextFormatting.WHITE}] ${TextFormatting.GREEN}Reloaded the config!"))
        }
        else
            sender.sendMessage(TextComponentString(getUsage(sender)))
    }

    override fun checkPermission(server: MinecraftServer, sender: ICommandSender): Boolean {
        return sender.canUseCommand(4, "nerfthis.admin.reload")
    }
}