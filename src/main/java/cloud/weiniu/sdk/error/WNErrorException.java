package cloud.weiniu.sdk.error;

/**
 * @author tiger (tiger@microsoul.com) 6/30/21
 */
public class WNErrorException extends Exception {
    private static final long serialVersionUID = -6357149550353160810L;

    private WNError error;

    public WNErrorException(WNError error) {
        super(error.toString());
        this.error = error;
    }

    public WNErrorException(WNError error, Throwable cause) {
        super(error.toString(), cause);
        this.error = error;
    }

    public WNError getError() {
        return this.error;
    }


}

