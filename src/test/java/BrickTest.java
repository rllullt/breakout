import logic.brick.GlassBrick;
import logic.brick.MetalBrick;
import logic.brick.WoodenBrick;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BrickTest {
	private GlassBrick glassBrick;
	private MetalBrick metalBrick;
	private WoodenBrick woodenBrick;

	@Before
	public void setUp() {
		glassBrick= new GlassBrick();
		metalBrick= new MetalBrick();
		woodenBrick= new WoodenBrick();
	}

	@Test
	public void hitsTest() {
		int life;
		life= glassBrick.remainingHits();
		assertEquals(1, life);
		glassBrick.hit();
		assertEquals(0, glassBrick.remainingHits());
		assertTrue(glassBrick.isDestroyed());

		life= metalBrick.remainingHits();
		assertEquals(10, life);
		for (int i= 0; i< 12; i++)
			metalBrick.hit();
		assertEquals(0, metalBrick.remainingHits());
		assertTrue(metalBrick.isDestroyed());

		life= woodenBrick.remainingHits();
		assertEquals(3, life);
		for (int i= 0; i< 9; i++)
			woodenBrick.hit();
		assertEquals(0, woodenBrick.remainingHits());
		assertTrue(woodenBrick.isDestroyed());
	}

	@Test
	public void scoreTest() {
		assertEquals(50, glassBrick.getScore());
		assertEquals(0, metalBrick.getScore());
		assertEquals(200, woodenBrick.getScore());
	}
}
