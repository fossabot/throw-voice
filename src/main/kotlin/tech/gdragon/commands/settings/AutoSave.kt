package tech.gdragon.commands.settings

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent
import org.jetbrains.exposed.sql.transactions.transaction
import tech.gdragon.BotUtils
import tech.gdragon.commands.Command
import tech.gdragon.commands.InvalidCommand
import tech.gdragon.db.dao.Guild

class AutoSave : Command {
  override fun action(args: Array<String>, event: GuildMessageReceivedEvent) {

      require(args.isEmpty()) {
        throw InvalidCommand(::usage, "Incorrect number of arguments: ${args.size}")
      }

    transaction {
      val guild = Guild.findById(event.guild.idLong)

      val message =
        guild?.settings?.let {
          it.autoSave = !it.autoSave

          if (it.autoSave)
            "Now saving at the end of each session!"
          else
            "No longer saving at the end of each session!"
        } ?: "Could not toggle autosave option."

      BotUtils.sendMessage(event.channel, message)
    }
  }

  override fun usage(prefix: String): String = "${prefix}autosave"

  override fun description(): String = "Toggles the option to automatically save and send all files at the end of each session - not just saved or clipped files."
}
