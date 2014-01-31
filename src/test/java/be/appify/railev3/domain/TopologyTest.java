package be.appify.railev3.domain;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class TopologyTest {

    /*
     *   A
     *   |
     *   B
     */
    @Test
    public void lineWithReverse() {
        Topology topology = Topology.createBuilder()
                .add("A")
                .add("B")
                .reverse()
                .add("B")
                .add("A")
                .build();

        Location location = topology.at("A");
        assertThat(location.getName(), is("A"));
        assertThat(location.next(Rotation.COUNTERCLOCKWISE), is(nullValue()));

        location = location.next(Rotation.CLOCKWISE);
        assertThat(location, is(topology.at("B")));
        assertThat(location.next(Rotation.CLOCKWISE), is(nullValue()));

        assertThat(topology.toString(), is(
                "A\n" +
                "|\n" +
                "B\n"
        ));
    }

    /*
     *   /A
     *  | |
     *  | B
     *  | |
     *   \C
     */
    @Test
    public void triangle() {
        Topology topology = Topology.createBuilder()
                .add("A")
                .add("B")
                .add("C")
                .add("A")
                .build();

        Location a = topology.at("A");

        Location b = a.next(Rotation.CLOCKWISE);
        assertThat(b, is(topology.at("B")));

        Location c = b.next(Rotation.CLOCKWISE);
        assertThat(c, is(topology.at("C")));

        assertThat(c.next(Rotation.CLOCKWISE), is(a));
        assertThat(a.next(Rotation.COUNTERCLOCKWISE), is(c));

        assertThat(topology.toString(), is(
                " /A\n" +
                "| |\n" +
                "| B\n" +
                "| |\n" +
                " \\C\n"
        ));
    }


    /*
     *    /A C
     *   | | |
     *   | |/
     *   | |
     *    \B
     */
    @Test
    public void reversedSwitch() {
        Topology topology = Topology.createBuilder()
                .add("A")
                .add("B")
                .add("A")
                .add("B")
                .reverse()
                .add("B")
                .add("C")
                .build();

        Location a = topology.at("A");
        Location b = topology.at("B");
        Location c = topology.at("C");

        Location next = a.next(Rotation.CLOCKWISE);
        assertTrue(next.isSwitchPoint());

        SwitchPoint switchPoint = (SwitchPoint) next;
        assertThat(switchPoint.next(Rotation.CLOCKWISE), is(b));

        switchPoint.throwLeft();
        assertThat(switchPoint.next(Rotation.COUNTERCLOCKWISE), is(a));
        switchPoint.throwRight();
        assertThat(switchPoint.next(Rotation.COUNTERCLOCKWISE), is(c));

        assertThat(b.next(Rotation.COUNTERCLOCKWISE), is(next));
    }


    /*
     *    /A
     *   | |
     *   | |\
     *   | | |
     *   | B D
     *   | | |
     *   | |/
     *   | |
     *    \C
     */
    @Test
    public void branchAndJoin() {
        Topology topology = Topology.createBuilder()
                .add("A")
                .add("B")
                .add("C")
                .add("A")
                .add("D")
                .add("C")
                .build();

        Location a = topology.at("A");
        Location b = topology.at("B");
        Location c = topology.at("C");
        Location d = topology.at("D");

        Location next = a.next(Rotation.CLOCKWISE);
        assertTrue(next.isSwitchPoint());
        SwitchPoint switchPoint = (SwitchPoint) next;
        assertThat(switchPoint.next(Rotation.COUNTERCLOCKWISE), is(a));
        switchPoint.throwLeft();
        assertThat(switchPoint.next(Rotation.CLOCKWISE), is(d));
        switchPoint.throwRight();
        assertThat(switchPoint.next(Rotation.CLOCKWISE), is(b));

        next = b.next(Rotation.CLOCKWISE);
        assertTrue(next.isSwitchPoint());
        switchPoint = (SwitchPoint) next;
        assertThat(switchPoint.next(Rotation.CLOCKWISE), is(c));
        switchPoint.throwLeft();
        assertThat(switchPoint.next(Rotation.COUNTERCLOCKWISE), is(b));
        switchPoint.throwRight();
        assertThat(switchPoint.next(Rotation.COUNTERCLOCKWISE), is(d));
    }
}
