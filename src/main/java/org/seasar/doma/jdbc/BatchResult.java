package org.seasar.doma.jdbc;

import java.util.Arrays;
import java.util.List;

/**
 * A batch processing result for immutable entities.
 * 
 * @param <ENTITY>
 *            the entity type
 */
public class BatchResult<ENTITY> {

    private final int[] counts;

    private final List<ENTITY> entities;

    /**
     * Creates an instance.
     * 
     * @param counts
     *            the array of the affected row count
     * @param entities
     *            the entity list
     */
    public BatchResult(int[] counts, List<ENTITY> entities) {
        this.counts = counts;
        this.entities = entities;
    }

    /**
     * Returns the array of the affected row count.
     * 
     * @return the array of the affected row count
     */
    public int[] getCounts() {
        return counts;
    }

    /**
     * Returns the entity list.
     * 
     * @return the entity list
     */
    public List<ENTITY> getEntities() {
        return entities;
    }

    /**
     * Returns the entity list.
     * 
     * @return the entity list
     * @see <a href=
     *      "https://kotlinlang.org/docs/reference/multi-declarations.html">Destructuring
     *      Declarations</a>
     */
    public List<ENTITY> component1() {
        return entities;
    }

    /**
     * Returns the array of the affected row count.
     * 
     * @return the array of the affected row count
     * @see <a href=
     *      "https://kotlinlang.org/docs/reference/multi-declarations.html">Destructuring
     *      Declarations</a>
     */
    public int[] component2() {
        return counts;
    }

    @Override
    public String toString() {
        return "BatchResult(entities=" + entities + ", counts=" + Arrays.toString(counts) + ")";
    }

}
