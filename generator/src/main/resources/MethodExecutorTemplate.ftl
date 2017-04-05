package generated;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class MethodExecutor {
    private Method targetmethod;
    private Object targetInstance;

    public MethodExecutor(Method targetmethod, Object targetInstance) {
    Objects.requireNonNull(targetmethod, "targetmethod cannot be null");
    Objects.requireNonNull(targetInstance, "targetInstance cannot be null");
        this.targetmethod = targetmethod;
        this.targetInstance = targetInstance;
    }

    public void execute() {
        try {
            targetmethod.invoke(targetInstance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}