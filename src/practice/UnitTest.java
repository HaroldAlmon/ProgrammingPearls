package practice;

import org.junit.Ignore;
import org.junit.Test;

public class UnitTest {
	VeryLargeNumberSort veryLargeNumberSort = VeryLargeNumberSort.getInstance();
	
	@Test(timeout = 1_000)
	public void VeryLargeNumberSort() {
		System.out.printf( "sortFileandReturnNumber(\"oneThousand.bin\", 1_000, 100) = %d%n", veryLargeNumberSort.sortFileandReturnNumber( "oneThousand.bin", 1_000, 100 ) );
	}
	
	private final int twoHours = 2 * 3_600;
	@Test( timeout = twoHours )
	@Ignore( "THIS IS SLOW, 45 minutes on an i7 core machine!" )
	public void SortOneBillionNumbers() {
		System.out.printf( "sortFileandReturnNumber(\"oneBillion.bin\", 1_000_000_000, 1_000_000) = %d%n", veryLargeNumberSort.sortFileandReturnNumber( "oneBillion.bin", 1_000_000_000, 1_000_000 ) );
	}
}
