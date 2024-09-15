package ir.syrent.velocityreport.spigot.messaging;

import com.google.common.annotations.Beta;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.JsonObject;
import ir.syrent.velocityreport.spigot.Ruom;
import ir.syrent.velocityreport.utils.ruom.utils.GsonUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class BukkitMessagingChannel implements PluginMessageListener {

    private final Set<BukkitMessagingEvent> messagingEvents = new HashSet<>();
    private final String name;

    public BukkitMessagingChannel(String namespace, String name) {
        this.name = namespace + ":" + name;
        Ruom.getServer().getMessenger().registerOutgoingPluginChannel(Ruom.getPlugin(), this.name);
        Ruom.getServer().getMessenger().registerIncomingPluginChannel(Ruom.getPlugin(), this.name, this);
    }

    protected void register(BukkitMessagingEvent messagingEvent) {
        messagingEvents.add(messagingEvent);
    }

    protected void unregister(BukkitMessagingEvent messagingEvent) {
        messagingEvents.remove(messagingEvent);
    }

    @Deprecated
    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte[] bytes) {
        if (!channel.equals(name)) return;
        String rawMessage = new String(bytes, StandardCharsets.UTF_8);
        if (rawMessage.isEmpty()) return;
        try {
            JsonObject message = GsonUtils.getParser().parse(rawMessage.substring(2)).getAsJsonObject();
            messagingEvents.forEach(event -> event.onPluginMessageReceived(player, message));
        } catch (StringIndexOutOfBoundsException ignored) { }
    }

    @Beta
    public void sendMessage(Player sender, JsonObject message) {
        ByteArrayDataOutput byteArrayDataOutput = ByteStreams.newDataOutput();
        byteArrayDataOutput.writeUTF(GsonUtils.get().toJson(message));
        sender.sendPluginMessage(Ruom.getPlugin(), name, byteArrayDataOutput.toByteArray());
    }

    public String getName() {
        return name;
    }

}
