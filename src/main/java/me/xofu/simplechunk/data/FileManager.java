package me.xofu.simplechunk.data;

import me.xofu.simplechunk.SimpleChunk;

public class FileManager {

    private SimpleChunk instance;

    private DataFile claims;

    public FileManager(SimpleChunk instance) {
        this.instance = instance;

        claims = new DataFile("claims.yml", instance);
    }

    public DataFile getClaimsFile() {
        return claims;
    }
}
