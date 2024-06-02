package spring.main;

import java.util.ServiceLoader;

public class ModuleFacadeLoader {
    static <T> T moduleFacadeLoader(Class<T> clazz, String environment) {
        //TODO: set prod environment for modules
        return ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Module facade class " + clazz.getName() + " could not be loaded"));
    }
}
