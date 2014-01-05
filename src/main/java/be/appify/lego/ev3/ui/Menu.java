package be.appify.lego.ev3.ui;

import be.appify.lego.ev3.component.Components;
import be.appify.lego.ev3.component.Key;
import be.appify.lego.ev3.component.LightColor;
import be.appify.util.Function;
import be.appify.util.Lists;

import java.util.*;

public class Menu<T> {
    private static final Option<?> SEPARATOR = new StaticOption<>("----------");

    private final List<Option<T>> options;
    private Option<T> selected;
    private boolean exit;
    private String header;
    private Runnable refreshAction;

    private Menu() {
        this.options = new ArrayList<>();
    }

    public static <T> Menu<T> create() {
        return new Menu<>();
    }

    public Menu<T> option(T option, Runnable action) {
        options.add(new ExecutableOption<T>(option, action));
        return this;
    }

    public Menu exitOption(T option) {
        return option(option, new Runnable() {
            @Override
            public void run() {
                exit();
            }
        });
    }

    private void exit() {
        exit = true;
    }

    public T showAt(int x, int y) {
        while (!exit) {
            T returnValue = renderMenu(x, y);
            if (exit) {
                return returnValue;
            }
        }
        return null;
    }

    private T renderMenu(int x, int y) {
        if (refreshAction != null)
            refreshAction.run();
        if (header != null) {
            Components.display().text(header).showAt(x, y);
            y++;
        }
        for (Option<T> option : options) {
            if (option.isSelectable()) {
                selected = option;
                break;
            }
        }
        final Set<T> returnValue = new HashSet<>();
        while (!exit) {
            renderOptions(x, y);
            Components.keypad()
                    .onKeyDo(Key.UP, new Runnable() {
                        @Override
                        public void run() {
                            selectPrevious();
                        }
                    })
                    .onKeyDo(Key.DOWN, new Runnable() {
                        @Override
                        public void run() {
                            selectNext();
                        }
                    })
                    .onKeyDo(Key.ENTER, new Runnable() {
                        @Override
                        public void run() {
                            returnValue.add(executeActionAndReturnValue());
                        }
                    })
                    .onKeyDo(Key.ESCAPE, new Runnable() {
                        @Override
                        public void run() {
                            returnValue.add(abort());
                        }
                    })
                    .awaitKey();
            if (!returnValue.isEmpty()) {
                return returnValue.iterator().next();
            }
        }
        return null;
    }

    private void renderOptions(final int x, final int y) {
        final int[] index = new int[]{0};
        Lists.forEach(options)
                .apply(new Function<Option<T>, Void>() {
                    @Override
                    public Void apply(Option<T> option) {
                        index[0] = option.render(x, y, index[0]);
                        return null;
                    }
                });
    }

    private T abort() {
        Components.light().color(LightColor.RED).blink();
        Components.display().clear();
        exit = true;
        return null;
    }

    private T executeActionAndReturnValue() {
        Components.light().color(LightColor.GREEN).blink();
        Components.display().clear();
        selected.execute();
        return selected.getValue();
    }

    private void selectNext() {
        int selectedIndex = options.indexOf(selected) + 1;
        if (selectedIndex >= options.size()) {
            selectedIndex = 0;
        }
        selected = options.get(selectedIndex);
        if (!selected.isSelectable()) {
            selectNext();
        }
    }

    private void selectPrevious() {
        int selectedIndex = options.indexOf(selected) - 1;
        if (selectedIndex < 0) {
            selectedIndex = options.size() - 1;
        }
        selected = options.get(selectedIndex);
        if (!selected.isSelectable()) {
            selectPrevious();
        }
    }

    public Menu<T> header(String header) {
        this.header = header;
        return this;
    }

    public Menu<T> exitOption(T option, final Runnable runnable) {
        return option(option, new Runnable() {
            @Override
            public void run() {
                runnable.run();
                exit();
            }
        });
    }

    public void separator() {
        options.add((Option<T>) SEPARATOR);
    }

    public void clear() {
        options.clear();
    }

    public void onRefresh(Runnable action) {
        this.refreshAction = action;
    }

    private static interface Option<T> {
        T getValue();

        boolean isSelectable();

        void execute();

        int render(int x, int y, int index);
    }

    private class ExecutableOption<T> implements Option<T> {
        private T value;
        private Runnable action;

        private ExecutableOption(T value, Runnable action) {
            this.value = value;
            this.action = action;
        }

        @Override
        public T getValue() {
            return value;
        }

        @Override
        public boolean isSelectable() {
            return true;
        }

        @Override
        public void execute() {
            if (action != null)
                action.run();
        }

        @Override
        public int render(int x, int y, int index) {
            Text text = Components.display().text(value.toString());
            if (selected == this) {
                text.invert();
            }
            text.showAt(x, y + index);
            index++;
            return index;
        }
    }

    private static class StaticOption<T> implements Option<T> {

        private T value;

        private StaticOption(T value) {
            this.value = value;
        }

        @Override
        public T getValue() {
            return value;
        }

        @Override
        public boolean isSelectable() {
            return false;
        }

        @Override
        public void execute() {
        }

        @Override
        public int render(int x, int y, int index) {
            Text text = Components.display().text(value.toString());
            text.showAt(x, y + index);
            index++;
            return index;
        }
    }

}
