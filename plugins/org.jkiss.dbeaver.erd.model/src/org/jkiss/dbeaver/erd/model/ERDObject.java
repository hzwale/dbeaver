/*
 * DBeaver - Universal Database Manager
 * Copyright (C) 2010-2023 DBeaver Corp and others
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * Created on Jul 15, 2004
 */
package org.jkiss.dbeaver.erd.model;

import org.jkiss.code.NotNull;
import org.jkiss.dbeaver.model.DBPAdaptable;
import org.jkiss.dbeaver.model.DBPNamedObject;
import org.jkiss.dbeaver.model.preferences.DBPPropertySource;
import org.jkiss.dbeaver.runtime.properties.PropertyCollector;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Map;

/**
 * Provides base class support for model objects to participate in event handling framework
 *
 * @author Serge Rider
 */
public abstract class ERDObject<OBJECT> implements DBPAdaptable, DBPNamedObject {

    public static final String PROP_CHILD = "CHILD";
    public static final String PROP_REORDER = "REORDER";
    public static final String PROP_INPUT = "INPUT";
    public static final String PROP_OUTPUT = "OUTPUT";
    public static final String PROP_NAME = "NAME";
    public static final String PROP_CONTENTS = "CONTENTS";
    public static final String PROP_SIZE = "SIZE";

    private transient PropertyChangeSupport listeners = null;//new PropertyChangeSupport(this);

    protected OBJECT object;
    private Object userData;

    protected ERDObject(OBJECT object) {
        this.object = object;
    }

    public OBJECT getObject() {
        return object;
    }

    public void setObject(OBJECT object) {
        this.object = object;
    }

    public Object getUserData() {
        return userData;
    }

    public void setUserData(Object userData) {
        this.userData = userData;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        if (listeners == null) {
            listeners = new PropertyChangeSupport(this);
        }
        listeners.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        if (listeners != null) {
            listeners.removePropertyChangeListener(l);
        }
    }

    public void firePropertyChange(String prop, Object old, Object newValue) {
        if (listeners != null) {
            listeners.firePropertyChange(prop, old, newValue);
        }
    }

    @Override
    public <T> T getAdapter(Class<T> adapter) {
        if (adapter == DBPPropertySource.class) {
            PropertyCollector propertyCollector = new PropertyCollector(object, false);
            propertyCollector.collectProperties();
            return adapter.cast(propertyCollector);
        }
        return null;
    }

    public abstract void fromMap(@NotNull ERDContext context, Map<String, Object> map);

    public abstract Map<String, Object> toMap(@NotNull ERDContext context, boolean fullInfo);

}
