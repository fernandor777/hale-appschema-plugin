package it.geosolutions.hale.io.appschema.writer.internal;

import it.geosolutions.hale.io.appschema.writer.internal.mapping.AppSchemaMappingContext;
import eu.esdihumboldt.cst.functions.core.Merge;
import eu.esdihumboldt.hale.common.align.model.Cell;
import it.geosolutions.hale.io.appschema.impl.internal.generated.app_schema.TypeMappingsPropertyType.FeatureTypeMapping;

/**
 * Translates a type cell specifying a {@link Merge} transformation function to
 * an app-schema feature type mapping.
 * 
 * @author Stefano Costa, GeoSolutions
 */
public class MergeHandler extends SingleSourceToTargetHandler {

	@Override
	public FeatureTypeMapping handleTypeTransformation(Cell typeCell,
			AppSchemaMappingContext context) {
		FeatureTypeMapping ftMapping = super.handleTypeTransformation(typeCell, context);

		// this is the only variation from RetypeHandler so far
		ftMapping.setIsDenormalised(true);

		return ftMapping;
	}

}
