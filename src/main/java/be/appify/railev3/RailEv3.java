package be.appify.railev3;

import be.appify.component.ComponentFactory;
import be.appify.component.Components;
import be.appify.ui.Menu;

import java.util.ArrayList;
import java.util.List;

public class RailEv3 {
    private List<String> route = new ArrayList<>();
    private List<String> allDestinations = new ArrayList<>();

    private final Discovery discovery = new Discovery(allDestinations);
    private final RouteEditor routeEditor = new RouteEditor(allDestinations, route);

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String componentFactoryClassName = System.getProperty(ComponentFactory.class.getName());
        if(componentFactoryClassName != null) {
                Components.factory((ComponentFactory) Class.forName(componentFactoryClassName).newInstance());
        }
        new RailEv3();
    }

    public RailEv3() {
        try {
            mainMenu();
        } catch (Throwable e) {
            e.printStackTrace();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e1) {
                System.exit(0);
            }
        }
    }

    private void mainMenu() {
        Menu.<String>create()
                .header("Main menu:")
                .option("Discover topology", new Runnable() {
                    @Override
                    public void run() {
                        discovery.initializeDiscovery();
                    }
                })
                .option("Edit route", new Runnable() {
                    @Override
                    public void run() {
                        routeEditor.editRoute();
                    }
                })
                .option("Depart", new Runnable() {
                    @Override
                    public void run() {
                        depart();
                    }
                })
                .option("Exit", new Runnable() {
                    @Override
                    public void run() {
                        System.exit(0);
                    }
                })
                .showAt(0, 0);
    }

    private void depart() {
    }
}
