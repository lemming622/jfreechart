/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2017, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Oracle and Java are registered trademarks of Oracle and/or its affiliates. 
 * Other names may be trademarks of their respective owners.]
 *
 */

package org.jfree.chart.util;

import java.awt.Paint;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * A collection of useful static utility methods for handling classes and object
 * instantiation.
 */
public final class ObjectUtils {

    /**
     * Default constructor - private.
     */
    private ObjectUtils() {
    }

    /**
     * Returns {@code true} if the two objects are equal OR both
     * {@code null}.
     *
     * @param o1 object 1 ({@code null} permitted).
     * @param o2 object 2 ({@code null} permitted).
     * @return {@code true} or {@code false}.
     */
    public static boolean equal(Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        }

        //check to see if the objects are arrayLists
        if ((o1 instanceof ArrayList) && (o2 instanceof ArrayList)){
            try{
                //cast to object lists since it is unknow what type is being used
                ArrayList<Object> o1Cast = (ArrayList<Object>)o1;
                ArrayList<Object> o2Cast = (ArrayList<Object>)o2;
                
                //check for Paint
                if(o1Cast.get(0).getClass().equals(o2Cast.get(0).getClass()) &&
                   o1Cast.get(0) instanceof Paint){
                    if(o1Cast.size() == o2Cast.size()){
                        for (int i = 0; i < o1Cast.size(); i++) {
                            if(!PaintUtils.equalObject(o1Cast.get(i), 
                                                       o2Cast.get(i))){
                                return false;
                            }
                        }
                        return true;
                    }
                    else{
                        return false;
                    }
                }
                //if not a paint let arraylist handle the equals
                else{
                    return o1.equals(o2);
                }
            }
            catch(Exception e){
                
            }
        }
        
        if (o1 != null) {
            return o1.equals(o2);
        }
        else{
            return false;
        }
    }

    /**
     * Returns a hash code for an object, or zero if the object is
     * {@code null}.
     *
     * @param object the object ({@code null} permitted).
     * @return The object's hash code (or zero if the object is
     *         {@code null}).
     */
    public static int hashCode(Object object) {
        int result = 0;
        if (object != null) {
            result = object.hashCode();
        }
        return result;
    }

    /**
     * Returns a clone of the specified object, if it can be cloned, otherwise
     * throws a CloneNotSupportedException.
     *
     * @param object the object to clone ({@code null} not permitted).
     * @return A clone of the specified object.
     * @throws CloneNotSupportedException if the object cannot be cloned.
     */
    public static Object clone(Object object)
        throws CloneNotSupportedException {
        if (object == null) {
            throw new IllegalArgumentException("Null 'object' argument.");
        }
        if (object instanceof PublicCloneable) {
            PublicCloneable pc = (PublicCloneable) object;
            return pc.clone();
        }
        else {
            try {
                Method method = object.getClass().getMethod("clone",
                        (Class[]) null);
                if (Modifier.isPublic(method.getModifiers())) {
                    return method.invoke(object, (Object[]) null);
                }
            }
            catch (NoSuchMethodException e) {
                throw new CloneNotSupportedException("Object without clone() method is impossible.");
            }
            catch (IllegalAccessException e) {
                throw new CloneNotSupportedException("Object.clone(): unable to call method.");
            }
            catch (InvocationTargetException e) {
                throw new CloneNotSupportedException("Object without clone() method is impossible.");
            }
        }
        throw new CloneNotSupportedException("Failed to clone.");
    }

    /**
     * Returns a new collection containing clones of all the items in the
     * specified collection.
     *
     * @param collection the collection ({@code null} not permitted).
     * @return A new collection containing clones of all the items in the
     *         specified collection.
     * @throws CloneNotSupportedException if any of the items in the collection
     *                                    cannot be cloned.
     */
    public static Collection deepClone(Collection collection)
            throws CloneNotSupportedException {

        if (collection == null) {
            throw new IllegalArgumentException("Null 'collection' argument.");
        }
        // all JDK-Collections are cloneable ...
        // and if the collection is not clonable, then we should throw
        // a CloneNotSupportedException anyway ...
        Collection result = (Collection) ObjectUtils.clone(collection);
        result.clear();
        Iterator iterator = collection.iterator();
        while (iterator.hasNext()) {
            Object item = iterator.next();
            if (item != null) {
                result.add(clone(item));
            }
            else {
                result.add(null);
            }
        }
        return result;
    }

}
