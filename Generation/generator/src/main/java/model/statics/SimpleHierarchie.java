package model.statics;

import java.util.Objects;

/**
 * @author Maxime
 */
public class SimpleHierarchie {
    private String stateOwner;
    private String stateChild;

    public SimpleHierarchie(String stateOwner, String stateChild) {
        Objects.requireNonNull(stateOwner);
        Objects.requireNonNull(stateChild);
        this.stateOwner = stateOwner;
        this.stateChild = stateChild;
    }

    public String getStateOwner() {
        return stateOwner;
    }

    public String getStateChild() {
        return stateChild;
    }
}
