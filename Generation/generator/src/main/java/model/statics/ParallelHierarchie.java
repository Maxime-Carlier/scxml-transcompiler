package model.statics;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Maxime
 */
public class ParallelHierarchie {
    private String stateOwner;
    private ArrayList<String> childs;

    public ParallelHierarchie(String stateOwner) {
        Objects.requireNonNull(stateOwner);
        this.stateOwner = stateOwner;
        this.childs = new ArrayList<>();
    }

    public void addChild(String stateName) {
        Objects.requireNonNull(stateName);
        childs.add(stateName);
    }

    public String getStateOwner() {
        return stateOwner;
    }

    public ArrayList<String> getChilds() {
        return childs;
    }
}
