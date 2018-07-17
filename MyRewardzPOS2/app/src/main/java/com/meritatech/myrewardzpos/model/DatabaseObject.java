package com.meritatech.myrewardzpos.model;

import com.meritatech.myrewardzpos.database.QueryBuilder;
import com.meritatech.myrewardzpos.database.SugarRecord;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Created by Waithera on 12/6/2017.
 */

public abstract class DatabaseObject extends SugarRecord {

    final static String OBJECT_ID_FIELD_NAME = "OBJECT_ID";


    public static <T> void buildInClause(Class<T> tClass, List<T> sequence, String fieldName, StringBuilder builder, boolean addQuotes) {
        builder.append(fieldName);
        builder.append(" IN (");

        boolean addComma = false;
        for (T item : sequence) {
            if (addComma) {
                builder.append(", ");
            }
            addComma = true;

            if (addQuotes) {
                builder.append("'");
            }
            builder.append(item);

            if (addQuotes) {
                builder.append("'");
            }
        }

        builder.append(" ) ");
    }

    public static <T> ArrayList<T> findAllRecords(Class<T> classType) {
        ArrayList<T> result = new ArrayList<>();
        try {
            for (Iterator<T> it = SugarRecord.findAll(classType);
                 it.hasNext(); ) {
                T record = it.next();

                if (record instanceof DatabaseObject) {
                    DatabaseObject dbObject = (DatabaseObject) record;
                    dbObject.afterLoad();
                }
                result.add(record);
            }
        }
        catch (Exception e) {

           // Utilities.LogErrors(e);
        }
        return result;
    }

    public static <T> List<T> findById(Class<T> type, UUID[] objectIDS) {
        try {
            String[] ids = new String[objectIDS.length];

            for (int n = 0; n < objectIDS.length; n++) {
                ids[n] = objectIDS[n].toString();
            }
            String whereClause = OBJECT_ID_FIELD_NAME + " IN (" + QueryBuilder.generatePlaceholders(ids.length) + ")";
            List<T> recordList = find(type, whereClause, ids);

            for (T record : recordList) {
                if (record instanceof DatabaseObject) {
                    DatabaseObject dbObject = (DatabaseObject) record;
                    dbObject.afterLoad();
                }
            }

            return recordList;
        } catch (Exception e) {
           // Utilities.LogErrors(e);
            return new ArrayList<>();
        }
    }

    public static <T> T findById(Class<T> type, UUID id) {
        try {
            List<T> list = find(type, OBJECT_ID_FIELD_NAME + "=?", new String[]{String.valueOf(id)}, null, null, "1");
            if (list.isEmpty()) return null;
            T result = list.get(0);

            if (result instanceof DatabaseObject) {
                DatabaseObject db = (DatabaseObject) result;
                db.afterLoad();
            }

            return result;
        } catch (Exception e) {
           // Utilities.LogErrors(e);
            return null;
        }
    }

    public static String getObjectIdFieldName() {
        return OBJECT_ID_FIELD_NAME;
    }

    public abstract UUID getObjectID();

    public abstract void setObjectID(UUID objectID);


    public void beforeSave() {
        UUID objectID = getObjectID();

        if (objectID == null) {
            objectID = UUID.randomUUID();
            setObjectID(objectID);
        }
    }

    public void afterLoad() {
    }

    public static String uuidToString(UUID uuid) {
        if (uuid != null) {
            return uuid.toString();
        } else {
            return null;
        }
    }

    public static UUID uuidFromString(String value) {
        if (value != null && value.length() > 0) {
            return UUID.fromString(value);
        } else {
            return null;
        }
    }

    @Override
    public long save() {
        long retValue = 0;
        try {
            beforeSave();
            if (this.getId() == null || this.getId() == 0) {
                retValue = super.save();
            } else {
                retValue = this.update();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retValue;
    }

    @Override
    public long update() {
        beforeSave();
        return super.update();
    }



    public static DatabaseObject updateDatabaseRecord(Class type, Object sourceObject, UUID objectID) {
        DatabaseObject record = (DatabaseObject) DatabaseObject.findById(type, objectID);

        if (record == null) {
            try {
                record = (DatabaseObject) type.getDeclaredConstructor().newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        DatabaseObject.copyTo(sourceObject, record);

        record.save();
        return record;
    }

    public static void copyTo(Object source, DatabaseObject databaseObject) {
        Field[] targetFields = databaseObject.getClass().getFields();
        Field[] sourceFields = source.getClass().getFields();

        for (Field targetField : targetFields) {
            targetField.setAccessible(true);
            Class<?> targetFieldType = targetField.getType();

            for (Field sourceField : sourceFields) {
                Class<?> sourceFieldType = targetField.getType();

                if (targetFieldType.equals(sourceFieldType) && sourceField.getName().equals(targetField.getName())) {
                    try {
                        sourceField.setAccessible(true);
                        targetField.setAccessible(true);
                        Object value = sourceField.get(source);
                        targetField.set(databaseObject, value);
                        break;
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if (source instanceof DatabaseObject) {
            DatabaseObject ds = (DatabaseObject) source;
            databaseObject.setObjectID(ds.getObjectID());
        }
    }
}
