package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.HolderConverters;

/**
 * @author taedium
 * 
 */
@HolderConverters({ BranchConverter.class, PrimaryKeyConverter.class, VersionNoConverter.class })
public class HolderConvertersProvider {
}
