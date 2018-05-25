package me.xofu.simplechunk.claim;

import me.xofu.simplechunk.SimpleChunk;
import me.xofu.simplechunk.data.DataFile;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class ClaimManager {

    private SimpleChunk instance;
    private List<Claim> claims;

    public ClaimManager(SimpleChunk instance) {
        this.instance = instance;

        claims = new ArrayList<>();

        load();
    }

    public void addClaim(Claim claim) {
        claims.add(claim);
    }

    public void removeClaim(Claim claim) {
        claims.remove(claim);
    }

    public boolean isClaimed(Chunk chunk) {
        for(Claim claim: claims) {
            if(claim.getChunk() == chunk) {
                return true;
            }
        }
        return false;
    }

    public boolean hasClaim(UUID uuid) {
        for(Claim claim: claims) {
            if(claim.getOwner().equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    public Claim getClaimAt(Location location) {
        for(Claim claim: claims) {
            if(location.getWorld().getChunkAt(location).getX() == claim.getChunk().getX() && location.getWorld().getChunkAt(location).getZ() == claim.getChunk().getZ()) {
                return claim;
            }
        }
        return null;
    }

    public List<Claim> getClaimsByUUID(UUID uuid) {
        List<Claim> claimList = new ArrayList<>();
        for(Claim claim: claims) {
            if(claim.getOwner().equals(uuid)) {
                claimList.add(claim);
            }
        }
        return claimList;
    }

    public int getMaxClaimsByPlayer(Player player) {
        if(instance.getConfig().contains("Max_claims." + instance.getPermission().getPrimaryGroup(player))) {
           return Integer.valueOf(instance.getConfig().getString("Max_claims." + instance.getPermission().getPrimaryGroup(player)));
        }
        return instance.getConfig().getInt("Max_claims_default");
    }

    public List<Claim> getClaims() {
        if(claims == null) {
            return null;
        }
        return claims;
    }

    private void load() {
        FileConfiguration claimsConfig = instance.getFileManager().getClaimsFile().getConfig();

        for(String string: claimsConfig.getStringList("List.claims")) {
            UUID owner = UUID.fromString(claimsConfig.getString("Claim." + string + ".owner"));

            World world = Bukkit.getWorld(claimsConfig.getString("Claim." + string + ".world"));
            int x = claimsConfig.getInt("Claim." + string + ".x");
            int z = claimsConfig.getInt("Claim." + string + ".z");
            Chunk chunk = world.getChunkAt(x, z);

            boolean server = claimsConfig.getBoolean("Claim." + string + ".server");

            Set<UUID> allowedPlayers = new HashSet<>();
            for(String player: claimsConfig.getStringList("Claim." + string + ".allowedplayers")) {
                allowedPlayers.add(UUID.fromString(player));
            }

            boolean forsale = claimsConfig.getBoolean("Claim." + string + ".forsale");
            int price = claimsConfig.getInt("Claim." + string + ".price");

            Claim claim = new Claim(owner, chunk, server);
            claim.setAllowedPlayers(allowedPlayers);
            claim.setForsale(forsale);
            claim.setPrice(price);
            claims.add(claim);
        }
    }

    public void save() {
        DataFile claimsFile = instance.getFileManager().getClaimsFile();
        FileConfiguration claimsConfig = instance.getFileManager().getClaimsFile().getConfig();

        List<String> claimList = new ArrayList<>();
        for(Claim claim: claims) {
            String owner = claim.getOwner().toString();
            String world = claim.getChunk().getWorld().getName();
            int x = claim.getChunk().getX();
            int z = claim.getChunk().getZ();
            boolean server = claim.isServer();
            List<String> allowedPlayers = new ArrayList<>();
            for(UUID uuid: claim.getAllowedPlayers()) {
                allowedPlayers.add(uuid.toString());
            }
            boolean forsale = claim.isForsale();
            int price = claim.getPrice();

            claimsConfig.set("Claim." + claim.getChunk().getX() + ":" + claim.getChunk().getZ() + ".owner", owner);
            claimsConfig.set("Claim." + claim.getChunk().getX() + ":" + claim.getChunk().getZ() + ".world", world);
            claimsConfig.set("Claim." + claim.getChunk().getX() + ":" + claim.getChunk().getZ() + ".x", x);
            claimsConfig.set("Claim." + claim.getChunk().getX() + ":" + claim.getChunk().getZ() + ".z", z);
            claimsConfig.set("Claim." + claim.getChunk().getX() + ":" + claim.getChunk().getZ() + ".server", server);
            claimsConfig.set("Claim." + claim.getChunk().getX() + ":" + claim.getChunk().getZ() + ".allowedplayers", allowedPlayers);
            claimsConfig.set("Claim." + claim.getChunk().getX() + ":" + claim.getChunk().getZ() + ".forsale", forsale);
            claimsConfig.set("Claim." + claim.getChunk().getX() + ":" + claim.getChunk().getZ() + ".price", price);
            claimsFile.save();

            claimList.add(String.valueOf(x) + ":" + String.valueOf(z));
        }
        for(String list: claimsConfig.getStringList("List.claims")) {
            if(!claimList.contains(list)) {
                claimsConfig.set("Claim." + list, null);
                claimsFile.save();
            }
        }
        claimsConfig.set("List.claims", claimList);
        claimsFile.save();
    }
}
