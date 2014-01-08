package be.appify.ui;

import be.appify.component.Components;
import be.appify.component.Key;
import be.appify.component.LightColor;
import be.appify.util.Function;
import be.appify.util.Lists;
import be.appify.util.Procedure;

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
        options.add(new ExecutableOption<>(option, action));
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

    private T renderMenu(final int x, final int y) {
        if (refreshAction != null)
            refreshAction.run();
        renderHeader(x, y);
        selectFirst();
        final Set<T> returnValue = new HashSet<>();
        renderOptions(x, y);
        while (!exit) {
            Components.keypad()
                    .onKeyDo(Key.UP, new Runnable() {
                        @Override
                        public void run() {
                            selectPrevious(x, y);
                        }
                    })
                    .onKeyDo(Key.DOWN, new Runnable() {
                        @Override
                        public void run() {
                            selectNext(x, y);
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

    private void selectFirst() {
        for (Option<T> option : options) {
            if (option.isSelectable()) {
                selected = option;
                break;
            }
        }
    }

    private int renderHeader(int x, int y) {
        if (header != null) {
            Components.display().text(header).showAt(x, y);
            y++;
        }
        return y;
    }

    private void renderOptions(final int x, final int y) {
        final int[] index = new int[]{0};
        Lists.forEach(options)
                .apply(new Procedure<Option<T>>() {
                    @Override
                    public void apply(Option<T> option) {
                        index[0] = option.render(x, y, index[0]);
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

    private void selectNext(int x, int y) {
        select(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer previouslySelectedIndex) {
                return previouslySelectedIndex + 1;
            }
        }, x, y);
    }

    private void selectPrevious(int x, int y) {
        select(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer previouslySelectedIndex) {
                return previouslySelectedIndex - 1;
            }
        }, x, y);
    }

    private void select(Function<Integer, Integer> indexOperation, int x, int y) {
        int previouslySelectedIndex = options.indexOf(selected);
        select(previouslySelectedIndex, indexOperation);
        renderSelection(previouslySelectedIndex, x, y);
    }

    private void renderSelection(int previouslySelectedIndex, int x, int y) {
        Option<T> previousSelection = options.get(previouslySelectedIndex);
        previousSelection.render(x, y, previouslySelectedIndex);
        selected.render(x, y, options.indexOf(selected));
    }

    private void select(int previouslySelectedIndex, Function<Integer, Integer> indexOperation) {
        int selectedIndex = indexOperation.apply(previouslySelectedIndex);
        if (selectedIndex >= options.size()) {
            selectedIndex = 0;
        } else if (selectedIndex < 0) {
            selectedIndex = options.size() - 1;
        }
        selected = options.get(selectedIndex);
        if (!selected.isSelectable()) {
            select(previouslySelectedIndex, indexOperation);
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
