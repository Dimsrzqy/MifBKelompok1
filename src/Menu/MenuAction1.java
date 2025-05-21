package Menu;

/**
 *
 * @author Raven
 */
public class MenuAction1 {

    protected boolean isCancel() {
        return cancel;
    }

    public void cancel() {
        this.cancel = true;
    }

    private boolean cancel = false;
}
