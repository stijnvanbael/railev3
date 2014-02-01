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
                .add("A", 3)
                .add("B", 15)
                .reverse()
                .add("B", 1)
                .add("A", 16)
                .build();

        Location location = topology.at("A");
        assertThat(location.getName(), is("A"));
        assertThat(location.next(Rotation.COUNTERCLOCKWISE), is(nullValue()));
        assertThat(location.distanceToNext(Rotation.CLOCKWISE), is(15));

        location = location.next(Rotation.CLOCKWISE);
        assertThat(location, is(topology.at("B")));
        assertThat(location.next(Rotation.CLOCKWISE), is(nullValue()));
        assertThat(location.distanceToNext(Rotation.COUNTERCLOCKWISE), is(15));
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
                .add("A", 2)
                .add("B", 9)
                .add("C", 7)
                .add("A", 12)
                .build();

        Location a = topology.at("A");

        Location b = a.next(Rotation.CLOCKWISE);
        assertThat(b, is(topology.at("B")));

        Location c = b.next(Rotation.CLOCKWISE);
        assertThat(c, is(topology.at("C")));

        assertThat(c.next(Rotation.CLOCKWISE), is(a));
        assertThat(a.next(Rotation.COUNTERCLOCKWISE), is(c));
    }


    /*
     *    /A C
     *   | |/
     *    \B
     */
    @Test
    public void reversedSwitch() {
        Topology topology = Topology.createBuilder()
                .add("A", 2)
                .add("B", 12)
                .add("A", 9)
                .add("B", 12)
                .reverse()
                .add("B", 1)
                .add("C", 9)
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

        assertThat(a.distanceToNext(Rotation.CLOCKWISE), is(8));
        assertThat(switchPoint.distanceToNext(Rotation.CLOCKWISE), is(4));

        switchPoint.throwLeft();
        assertThat(switchPoint.distanceToNext(Rotation.COUNTERCLOCKWISE), is(8));
        switchPoint.throwRight();
        assertThat(switchPoint.distanceToNext(Rotation.COUNTERCLOCKWISE), is(5));

        assertThat(b.distanceToNext(Rotation.COUNTERCLOCKWISE), is(4));
        assertThat(c.distanceToNext(Rotation.CLOCKWISE), is(5));
    }


    /*
     *    /A
     *   | |\
     *   | B D
     *   | |/
     *    \C
     */
    @Test
    public void branchAndJoin() {
        Topology topology = Topology.createBuilder()
                .add("A", 3)
                .add("B", 8)
                .add("C", 9)
                .add("A", 15)
                .add("D", 9)
                .add("C", 10)
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
