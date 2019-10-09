/*
 * Copyright (c) 2015 Data Harmonisation Panel
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution. If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Data Harmonisation Panel <http://www.dhpanel.eu>
 */

package it.geosolutions.hale.io.appschema.writer.internal;

import com.google.common.collect.ListMultimap;

import eu.esdihumboldt.hale.common.align.model.Cell;
import eu.esdihumboldt.hale.common.align.model.Entity;
import eu.esdihumboldt.hale.common.schema.model.TypeDefinition;
import it.geosolutions.hale.io.appschema.impl.internal.generated.app_schema.TypeMappingsPropertyType.FeatureTypeMapping;
import it.geosolutions.hale.io.appschema.writer.internal.mapping.AppSchemaMappingContext;

/**
 * Base class for type transformation handlers converting a single source entity
 * to a single target entity.
 * 
 * @author Stefano Costa, GeoSolutions
 */
public abstract class SingleSourceToTargetHandler implements TypeTransformationHandler {

	private static final TypeTransformationHandler DEFAULT_HANDLER = new DefaultHandlerDelegate();
	private TypeTransformationHandler handlerDelegate = DEFAULT_HANDLER;

	/**
	 * @see it.geosolutions.hale.io.appschema.writer.internal.TypeTransformationHandler#handleTypeTransformation(eu.esdihumboldt.hale.common.align.model.Cell,
	 *      it.geosolutions.hale.io.appschema.writer.internal.mapping.AppSchemaMappingContext)
	 */
	@Override
	public FeatureTypeMapping handleTypeTransformation(Cell typeCell,
			AppSchemaMappingContext context) {
		return handlerDelegate.handleTypeTransformation(typeCell, context);
	}

	/**
	 * @param handlerDelegate the handlerDelegate to set.
	 */
	public void setHandlerDelegate(TypeTransformationHandler handlerDelegate) {
		if (handlerDelegate == null)
			throw new IllegalArgumentException("handlerDelegate is not nullable.");
		this.handlerDelegate = handlerDelegate;
	}

	private static class DefaultHandlerDelegate implements TypeTransformationHandler {

		/**
		 * @see it.geosolutions.hale.io.appschema.writer.internal.TypeTransformationHandler#handleTypeTransformation(eu.esdihumboldt.hale.common.align.model.Cell,
		 *      it.geosolutions.hale.io.appschema.writer.internal.mapping.AppSchemaMappingContext)
		 */
		@Override
		public FeatureTypeMapping handleTypeTransformation(Cell typeCell,
				AppSchemaMappingContext context) {
			ListMultimap<String, ? extends Entity> sourceEntities = typeCell.getSource();
			if (sourceEntities == null || sourceEntities.size() == 0) {
				throw new IllegalStateException("No source type has been specified.");
			}
			ListMultimap<String, ? extends Entity> targetEntities = typeCell.getTarget();
			if (targetEntities == null || targetEntities.size() == 0) {
				throw new IllegalStateException("No target type has been specified.");
			}

			// Maps 1 source to 1 target, so it is safe to pick the first entity
			// in
			// the list
			Entity sourceType = sourceEntities.values().iterator().next();
			Entity targetType = targetEntities.values().iterator().next();
			TypeDefinition targetTypeDef = targetType.getDefinition().getType();

			final FeatureTypeMapping ftMapping = context
					.getOrCreateFeatureTypeMapping(targetTypeDef);
			ftMapping.setSourceType(sourceType.getDefinition().getType().getName().getLocalPart());

			return ftMapping;
		}

	}

}
