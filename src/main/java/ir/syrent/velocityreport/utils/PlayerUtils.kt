package ir.syrent.velocityreport.utils

import ir.syrent.velocityreport.spigot.adventure.AdventureApi
import ir.syrent.velocityreport.spigot.storage.Message
import ir.syrent.velocityreport.spigot.storage.Settings
import net.kyori.adventure.inventory.Book
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

fun CommandSender.sendMessage(message: Message, vararg replacements: TextReplacement) {
    AdventureApi.get().sender(this).sendMessage(Settings.formatMessage(message, *replacements).component())
}

fun Player.sendMessage(message: Message, vararg replacements: TextReplacement) {
    val formattedMessage = Settings.formatMessage(this, message, *replacements)
    if (formattedMessage.isBlank()) return

    Settings.commandSound.let {
        if (it != null) {
            this.playSound(this.location, it, 1f, 1f)
        }
    }

    AdventureApi.get().player(this).sendMessage(Settings.formatMessage(this, message, *replacements).component())
}

fun Player.sendMessageOnly(message: Message, vararg replacements: TextReplacement) {
    AdventureApi.get().player(this).sendMessage(Settings.formatMessage(this, message, *replacements).component())
}

fun Player.sendActionbar(message: Message, vararg replacements: TextReplacement) {
    AdventureApi.get().player(this).sendActionBar(Settings.formatMessage(this, message, *replacements).component())
}

fun Player.openBook(book: Book) {
    Settings.bookSound.let {
        if (it != null) {
            this.playSound(this.location, it, 1f, 1f)
        }
    }

    AdventureApi.get().player(this).openBook(book)
}