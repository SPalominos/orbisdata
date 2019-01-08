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
 * DataManager API  is distributed under GPL 3 license.
 *
 * Copyright (C) 2018 CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * DataManager API  is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * DataManager API  is distributed in the hope that it will be useful, but WITHOUT ANY
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
package org.orbisgis.datamanagerapi.dsl;

import org.orbisgis.datamanagerapi.dataset.ITable;

import java.util.Map;

/**
 * Interface defining methods for the SQL request building (select, from ...).
 *
 * @author Erwan Bocher (CNRS)
 * @author Sylvain PALOMINOS (UBS 2019)
 */
public interface ISqlBuilder {

    enum Order{ASC, DESC}

    /**
     * Start point of the SQL request.
     *
     * @param fields Array of the fields to select.
     *
     * @return ISqlBuilder instance to continue building.
     */
    ISqlBuilder select(String... fields);

    /**
     * Indicates the table use for the selection.
     *
     * @param tables Array of the table use for the selection.
     *
     * @return ISqlBuilder instance to continue building.
     */
    ISqlBuilder from(String... tables);

    /**
     * Indicates the condition for the selection.
     *
     * @param condition Condition to use for for the selection.
     *
     * @return ISqlBuilder instance to continue building.
     */
    ISqlBuilder where(String condition);

    /**
     * Add a 'and' condition for the selection.
     *
     * @param condition Condition to add for for the selection.
     *
     * @return ISqlBuilder instance to continue building.
     */
    ISqlBuilder and(String condition);

    /**
     * Add a 'or' condition for the selection.
     *
     * @param condition Condition to add for for the selection.
     *
     * @return ISqlBuilder instance to continue building.
     */
    ISqlBuilder or(String condition);

    /**
     * Set the group by fields.
     *
     * @param fields Array of fields to use for the group by.
     *
     * @return ISqlBuilder instance to continue building.
     */
    ISqlBuilder groupBy(String... fields);

    /**
     * Set the order by fields.
     *
     * @param orderByMap Map with the field as key and the Order as value.
     *
     * @return ISqlBuilder instance to continue building.
     */
    ISqlBuilder orderBy(Map<String, Order> orderByMap);

    /**
     * Set the order by unique field.
     *
     * @param field Field to use.
     * @param order Order of the field.
     *
     * @return ISqlBuilder instance to continue building.
     */
    ISqlBuilder orderBy(String field, Order order);

    /**
     * Set the ASC order by unique field.
     *
     * @param field Field to use.
     *
     * @return ISqlBuilder instance to continue building.
     */
    ISqlBuilder orderBy(String field);

    /**
     * Execute the query.
     */
    ITable execute();
}
