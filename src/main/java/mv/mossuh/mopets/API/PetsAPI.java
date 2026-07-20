package mv.mossuh.mopets.API;

import mv.mossuh.mopets.MANAGERS.PetsManager;

public class PetsAPI {
    private static final PetsManager petsManager = new PetsManager();

    public static PetsManager getManager() { return petsManager; }
}
