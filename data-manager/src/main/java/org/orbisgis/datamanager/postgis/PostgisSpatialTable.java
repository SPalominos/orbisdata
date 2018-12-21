/*
 * Bundle DataManager is part of the OrbisGIS platform
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
 * DataManager is distributed under GPL 3 license.
 *
 * Copyright (C) 2018 CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * DataManager is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * DataManager is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * DataManager. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly:
 * info_at_ orbisgis.org
 */
package org.orbisgis.datamanager.postgis;

import groovy.lang.MetaClass;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.h2gis.utilities.SFSUtilities;
import org.h2gis.utilities.SpatialResultSetMetaData;
import org.h2gis.utilities.TableLocation;
import org.locationtech.jts.geom.Geometry;
import org.orbisgis.datamanagerapi.dataset.Database;
import org.orbisgis.datamanagerapi.dataset.IJdbcTable;
import org.orbisgis.datamanagerapi.dataset.ISpatialTable;
import org.orbisgis.postgis_jts.ResultSetMetaDataWrapper;
import org.orbisgis.postgis_jts.StatementWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class PostgisSpatialTable extends SpatialResultSetWrapper implements ISpatialTable, IJdbcTable {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostgisSpatialTable.class);

    /** Type of the database */
    private Database dataBase;
    /** Table location */
    private TableLocation tableLocation;
    /** MetaClass use for groovy methods/properties binding */
    private MetaClass metaClass;
    /** Map of the properties */
    private Map<String, Object> propertyMap;

    /**
     * Main constructor.
     *
     * @param tableLocation TableLocation that identify the represented table.
     * @param resultSet ResultSet containing the data of the table.
     * @param statement Statement used to request the database.
     */
    public PostgisSpatialTable(TableLocation tableLocation, ResultSet resultSet, StatementWrapper statement) {
        super(resultSet, statement);
        try {
            resultSet.beforeFirst();
        } catch (SQLException e) {
            LOGGER.error("Unable to go before the first ResultSet row.\n" + e.getLocalizedMessage());
        }
        this.dataBase = Database.POSTGIS;
        this.tableLocation = tableLocation;
        this.metaClass = InvokerHelper.getMetaClass(getClass());
        this.propertyMap = new HashMap<>();
    }

    @Override
    public Geometry getGeometry(int columnIndex){
        try {
            return super.getGeometry(columnIndex);
        } catch (SQLException e) {
            LOGGER.error("Unable to get the geometry at '" + columnIndex + "'.\n" + e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    public Geometry getGeometry(String columnLabel){
        try {
            return super.getGeometry(columnLabel);
        } catch (SQLException e) {
            LOGGER.error("Unable to get the geometry of '" + columnLabel + "'.\n" + e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    public Geometry getGeometry(){
        try {
            return super.getGeometry();
        } catch (SQLException e) {
            LOGGER.error("Unable to get the geometry.\n" + e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    public void setProperty(String propertyName, Object newValue) {
        propertyMap.put(propertyName, newValue);
    }

    @Override
    public MetaClass getMetaClass() {
        return metaClass;
    }

    @Override
    public void setMetaClass(MetaClass metaClass) {
        this.metaClass = metaClass;
    }

    @Override
    public SpatialResultSetMetaData getMetadata(){
        try {
            return super.getMetaData().unwrap(SpatialResultSetMetaData.class);
        } catch (SQLException e) {
            LOGGER.error("Unable to get the metadata.\n" + e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public TableLocation getTableLocation() {
        return tableLocation;
    }

    @Override
    public Database getDataBase() {
        return dataBase;
    }

    @Override
    public Map<String, Object> getPropertyMap() {
        return propertyMap;
    }

    /**
     * SpatialResultSetMetadata implementation compatible with postgis database.
     */
    //TODO move to the postgis-jts project.
    private class SpatialResultSetMetaDataImpl extends ResultSetMetaDataWrapper implements SpatialResultSetMetaData {
        private int firstGeometryFieldIndex = -1;
        private StatementWrapper statement;

        public SpatialResultSetMetaDataImpl(ResultSetMetaData resultSetMetaData, StatementWrapper statement) {
            super(resultSetMetaData);
            this.statement = statement;
        }

        public int getFirstGeometryFieldIndex() throws SQLException {
            if (this.firstGeometryFieldIndex == -1) {
                for(int idColumn = 1; idColumn <= this.getColumnCount(); ++idColumn) {
                    if (this.getColumnTypeName(idColumn).equalsIgnoreCase("geometry")) {
                        this.firstGeometryFieldIndex = idColumn;
                        break;
                    }
                }
            }

            return this.firstGeometryFieldIndex;
        }

        public int getGeometryType() throws SQLException {
            return this.getGeometryType(this.getFirstGeometryFieldIndex());
        }

        public int getGeometryType(int column) throws SQLException {
            return SFSUtilities.getGeometryType(this.statement.getConnection(),
                    new TableLocation(this.getCatalogName(column), this.getSchemaName(column),
                            this.getTableName(column)), this.getColumnName(column));
        }

        public <T> T unwrap(Class<T> iface) throws SQLException {
            if (iface.isInstance(this)) {
                try {
                    return iface.cast(this);
                } catch (ClassCastException var3) {
                    throw new SQLException(var3);
                }
            } else {
                return super.unwrap(iface);
            }
        }
    }
}