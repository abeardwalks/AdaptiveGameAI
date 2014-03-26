package tests.moves;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AbstractMoveTest.class, MovementMoveTest.class,
		PlacementMoveTest.class, RemovalMoveTest.class })
public class MoveTests {

}
