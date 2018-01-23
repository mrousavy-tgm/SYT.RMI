package Server;

import Modules.Compute;

public interface Processor extends Compute {
    boolean busy();
}
