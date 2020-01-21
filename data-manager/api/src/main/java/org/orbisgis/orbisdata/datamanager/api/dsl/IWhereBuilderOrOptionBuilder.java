/*
 * Bundle DataManager API is part of the OrbisGIS platform
 *
 * OrbisGIS is a java GIS application dedicated to research in GIScience.
 * OrbisGIS is developed by the GIS group of the DECIDE team of the
 * Lab-STICC CNRS laboratory, see <http://www.lab-sticc.fr/>.
 *
 * The GIS group of the DECIDE team is located at :
 *
 * Laboratoire Lab-STICC – CNRS UMR 6285
 * Equipe DECIDE
 * UNIVERSITÉ DE BRETAGNE-SUD
 * Institut Universitaire de Technologie de Vannes
 * 8, Rue Montaigne - BP 561 56017 Vannes Cedex
 *
 * DataManager API is distributed under LGPL 3 license.
 *
 * Copyright (C) 2018-2020 CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * DataManager API is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * DataManager API is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * DataManager API. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly:
 * info_at_ orbisgis.org
 */
package org.orbisgis.orbisdata.datamanager.api.dsl;

import org.orbisgis.orbisdata.datamanager.api.dataset.ITable;

/**
 * Interface defining methods for the SQL 'from' building. The request construction can be continued thanks to the
 * {@link IConditionOrOptionBuilder} or the {@link IOptionBuilder} or its result can be get calling 'eachRow' to
 * iterate on the resultSet or 'as ITable' to get the {@link ITable} object.
 * As the {@link IConditionOrOptionBuilder} extends {@link IOptionBuilder} the result of the where method ca be
 * used to add condition (using AND or OR) or to set options (Like LIMIT, GROUP BY, ...).
 *
 * @author Erwan Bocher (CNRS)
 * @author Sylvain PALOMINOS (Lab-STICC UBS 2019)
 */
public interface IWhereBuilderOrOptionBuilder extends IOptionBuilder {

    /**
     * Indicates the condition for the selection.
     *
     * @param condition Condition to use for for the selection.
     * @return {@link IConditionOrOptionBuilder} instance to continue building.
     */
    IConditionOrOptionBuilder where(String condition);
}
