package to.networld.android.divedroid.graphs;

import android.content.Context;
import android.content.Intent;

/**
 * Defines the demo charts.
 */
public interface IChart {
  /**
   * Executes the chart.
   * 
   * @param context The execution context (parent window).
   * @return The created Intent.
   */
  Intent execute(Context _context);

}
