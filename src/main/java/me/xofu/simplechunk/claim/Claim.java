package me.xofu.simplechunk.claim;

import org.bukkit.Chunk;

import java.util.*;

public class Claim {

    private UUID owner;
    private Chunk chunk;
    private boolean server;

    private Set<UUID> allowedPlayers;
    private boolean forsale;
    private int price;

    public Claim(UUID owner, Chunk chunk, boolean server) {
        this.owner = owner;
        this.chunk = chunk;
        this.server = server;

        this.allowedPlayers = new HashSet<>();
        this.forsale = false;
        this.price = 0;
    }


    public UUID getOwner() {
        return owner;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public boolean isServer() { return server; }

    public Set<UUID> getAllowedPlayers() {
        return allowedPlayers;
    }

    public boolean isForsale() { return forsale; }

    public int getPrice() { return price; }

    public void allowPlayer(UUID player) {
        allowedPlayers.add(player);
    }

    public void disallowPlayer(UUID player) {
        allowedPlayers.remove(player);
    }

    public void setOwner(UUID owner) { this.owner = owner; }

    public void setAllowedPlayers(Set<UUID> allowedPlayers) {
        this.allowedPlayers = allowedPlayers;
    }

    public void setForsale(boolean forsale) { this.forsale = forsale; }

    public void setPrice(int price) { this.price = price; }
}
