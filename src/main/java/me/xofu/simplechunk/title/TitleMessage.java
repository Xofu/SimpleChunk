package me.xofu.simplechunk.title;

import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_12_R1.PlayerConnection;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class TitleMessage {

    private String message;

    public TitleMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void send(Player player, int fadein, int stay, int fadeout) {
        PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;

        IChatBaseComponent title = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + getMessage() + "\"}");
        PacketPlayOutTitle titleTime = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, title, fadein, stay, fadein);
        PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, title);
        connection.sendPacket(titleTime);
        connection.sendPacket(titlePacket);
    }
}
