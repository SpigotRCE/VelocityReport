package ir.sayandevelopment.sayanreport;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DiscordManager extends ListenerAdapter {

    private static DiscordManager instance;
    public static DiscordManager getInstance() {
        return instance;
    }

    private final TextChannel reportChannel = Main.JDA.getTextChannelById(901752768149225492L);
    private final TextChannel staffLogChannel = Main.JDA.getTextChannelById(910915767195811851L);

    public DiscordManager() {
        instance = this;
    }

    public void sendReportMessage(String reporter, String reported, String reason, String server) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(String.format("**%s** Reported **%s**", reporter, reported), null);

        eb.setColor(Color.red);
        eb.setColor(new Color(0xF40C0C));

        eb.addField("Reporter:", reporter, true);
        eb.addField("Reported:", reported, true);
        eb.addField("Reason:", reason, true);
        eb.addField("Server:", server, true);

        eb.setAuthor("New Report!", null,
                "https://cdn.discordapp.com/attachments/587612394768039947/901758237475487774/IMG_20211024_123512_497.jpg");

        eb.setThumbnail(String.format("http://cravatar.eu/avatar/%s/64.png", reported));

        assert reportChannel != null;
        reportChannel.sendMessageEmbeds(eb.build())
                .append("<@&703491862740336712>")
                .setActionRow(Button.success("check", "Checked"))
                .queue();
    }

    public void sendStaffLogMessage(Staff staff) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(String.format("**%s** Left the server", staff.getUsername()), null);

        eb.setColor(new Color(0x8E48CA));

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

        eb.addField("Join:", timeFormatter.format(staff.getJoinDate()), false);
        eb.addField("Leave:", timeFormatter.format(staff.getLeaveDate()), false);

        eb.setAuthor("Log System!", null,
                "https://cdn.discordapp.com/attachments/587612394768039947/901758237475487774/IMG_20211024_123512_497.jpg");

        eb.setThumbnail(String.format("http://cravatar.eu/avatar/%s/64.png", staff.getUsername()));

        assert staffLogChannel != null;
        staffLogChannel.sendMessageEmbeds(eb.build()).queue();
    }

    @Override
    public void onButtonClick(ButtonClickEvent event) {
        if (event.getComponentId().equals("check")) {
            Message message = event.getMessage();
            message.delete().queue();

            User user = event.getUser();
            String checkerName = user.getName();

            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle(String.format("Report checked by %s", checkerName), null);

            eb.setColor(Color.red);
            eb.setColor(new Color(0x00FF4D));

            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime currentTime = LocalDateTime.now();

            eb.addField("Time:", timeFormatter.format(currentTime), true);
            eb.addBlankField(false);
            message.getEmbeds().forEach(embed -> embed.getFields().forEach(eb::addField));

            eb.setAuthor("Report Checked!", null,
                    "https://cdn.discordapp.com/attachments/587612394768039947/901758237475487774/IMG_20211024_123512_497.jpg");

            eb.setThumbnail(user.getAvatarUrl());

            assert reportChannel != null;
            reportChannel.sendMessageEmbeds(eb.build()).queue();
        }
    }
}