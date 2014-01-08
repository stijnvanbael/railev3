package be.appify.railev3;

import be.appify.ui.Menu;
import be.appify.util.Lists;
import be.appify.util.Procedure;

import java.util.List;

public class RouteEditor {
    private final List<String> allDestinations;
    private final List<String> route;

    public RouteEditor(List<String> allDestinations, List<String> route) {
        this.allDestinations = allDestinations;
        this.route = route;
    }

    void editRoute() {
        final Menu<String> menu = Menu.<String>create()
                .header("Route:");
        menu.onRefresh(new Runnable() {
            @Override
            public void run() {
                menu.clear();
                Lists.forEach(route)
                        .apply(new Procedure<String>() {
                            @Override
                            public void apply(final String destination) {
                                menu.option(destination, new Runnable() {
                                    @Override
                                    public void run() {
                                        editDestination(destination);
                                    }
                                });
                            }
                        });
                if (route.isEmpty())
                    menu.separator();
                if (route.size() < 5)
                    menu.option("Add destination", new Runnable() {
                        @Override
                        public void run() {
                            addDestination();
                        }
                    });
                menu.exitOption("Back");
            }
        });
        menu.showAt(0, 0);
    }

    void editDestination(final String destination) {
        Menu.<String>create()
                .exitOption("Move up", new Runnable() {
                    @Override
                    public void run() {
                        moveDestinationUp(destination);
                    }
                })
                .exitOption("Move down", new Runnable() {
                    @Override
                    public void run() {
                        moveDestinationDown(destination);
                    }
                })
                .exitOption("Delete", new Runnable() {
                    @Override
                    public void run() {
                        deleteDestination(destination);
                    }
                })
                .exitOption("Cancel")
                .showAt(0, 0);
    }

    void deleteDestination(String destination) {
        route.remove(destination);
    }

    void moveDestinationDown(String destination) {
        int index = route.indexOf(destination);
        if (index < route.size()) {
            route.remove(destination);
            route.add(index + 1, destination);
        }
    }

    void moveDestinationUp(String destination) {
        int index = route.indexOf(destination);
        if (index > 0) {
            route.remove(destination);
            route.add(index - 1, destination);
        }
    }

    void addDestination() {
        Menu<String> menu = Menu.<String>create()
                .header("Add destination:");
        for (final String destination : allDestinations) {
            menu.exitOption(destination, new Runnable() {
                @Override
                public void run() {
                    addDestination(destination);
                }
            });
        }
        menu.showAt(0, 0);
    }

    void addDestination(String destination) {
        route.add(destination);
    }
}