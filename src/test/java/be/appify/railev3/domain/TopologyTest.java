package be.appify.railev3.domain;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class TopologyTest {

    /*
     *   A--B
     */
    @Test
    public void simpleLoop() {
        Topology topology = Topology.createBuilder()
                .add("A")
                .add("B")
                .add("A")
                .build();

        Destination destination = topology.at("A");
        assertThat(destination.getName(), equalTo("A"));

        destination = destination.next(Direction.CLOCKWISE);
        assertThat(destination.getName(), equalTo("B"));

        destination = destination.next(Direction.CLOCKWISE);
        assertThat(destination.getName(), equalTo("A"));

        destination = destination.next(Direction.COUNTERCLOCKWISE);
        assertThat(destination.getName(), equalTo("B"));
    }


    /*
     *     A
     *    / \
     *   C--B
     */
    @Test
    public void triangle() {
        Topology topology = Topology.createBuilder()
                .add("A")
                .add("B")
                .add("C")
                .add("A")
                .build();

        Destination destination = topology.at("A");
        assertThat(destination.getName(), equalTo("A"));

        destination = destination.next(Direction.CLOCKWISE);
        assertThat(destination.getName(), equalTo("B"));

        destination = destination.next(Direction.CLOCKWISE);
        assertThat(destination.getName(), equalTo("C"));

        destination = destination.next(Direction.CLOCKWISE);
        assertThat(destination.getName(), equalTo("A"));

        destination = destination.next(Direction.COUNTERCLOCKWISE);
        assertThat(destination.getName(), equalTo("C"));
    }
}
