package com.sorex.sorex.entities.blockchain;

import com.sorex.sorex.entities.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Blockchain  implements Serializable{

    private int id;
    private int difficulty;
    private List<Bloc> blocs;

    /**public Blockchain(int difficulty)
    {
        this.difficulty = difficulty;
        blocs = new ArrayList<>();
        // create first/genesis block
        Bloc b = new Bloc(0, System.currentTimeMillis(),
                null, null);
        b.mineBloc(difficulty);
        blocs.add(b);
    }*/

    public Bloc latestBlock() {
        return blocs.get(blocs.size() - 1);
    }


    public Bloc newBlock(Transaction data) {
        Bloc latestBloc = latestBlock();
        Bloc newBloc = new Bloc(latestBloc.getId()+1,latestBloc.getIndex() + 1,
                System.currentTimeMillis(), latestBloc.getHash(), data);
        return newBloc;
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();
        for (Bloc bloc : blocs) {
            builder.append(bloc).append("\n");
        }

        return builder.toString();
    }
}
