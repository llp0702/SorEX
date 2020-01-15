package com.sorex.sorex.entities.blockchain;

import com.sorex.sorex.entities.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Bloc implements Serializable {
    private int id;
    private int index;
    private long timestamp;
    private String hash;
    private String previousHash;
    private Transaction data;
    private int nonce; // number of tries before resolving proof of work

    public Bloc(int id, int index, long timestamp, String previousHash, Transaction data) {
        this.index = index;
        this.timestamp = timestamp;
        this.previousHash = previousHash;
        this.data = data;
        nonce = 0;
        hash = null;
    }

    // String representation of our block (except for hash)
    public String str() {
        return index + timestamp + previousHash + data + nonce;
    }

    @Override
    public String toString() {
        return "Block " +
                "index=" + index +
                ", timestamp=" + new Date(timestamp) +
                ", hash='" + hash + '\'' +
                ", previousHash='" + previousHash + '\'' +
                ", data='" + data + '\'' +
                ", nonce=" + nonce +
                '}';
    }



}
