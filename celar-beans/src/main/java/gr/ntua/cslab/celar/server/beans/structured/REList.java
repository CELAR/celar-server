package gr.ntua.cslab.celar.server.beans.structured;

import gr.ntua.cslab.celar.server.beans.IDEntity;
import gr.ntua.cslab.celar.server.beans.MetricValue;
import gr.ntua.cslab.celar.server.beans.ReflectiveEntity;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Wrapper class for a ReflectiveEnity LinkedList
 * @author cmantas
 * @param <E>
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class  REList <E extends ReflectiveEntity> extends ReflectiveEntity implements List  {
     public List<E> values;

    public REList() {
        values = new java.util.LinkedList();
    }

    public REList(List<E> values) {
        this.values = values;
    }

    @XmlAnyElement(lax=true)
    public List<E> getValues() {
        return values;
    }

    public void setValues(List<E> values) {
        this.values = values;
    }

    @Override
    public int size() {
        return values.size();
    }

    @Override
    public boolean isEmpty() {
        return values.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return values.contains(o);
    }

    @Override
    public Iterator iterator() {
       return values.iterator();
    }

    @Override
    public Object[] toArray() {
        return values.toArray();
    }

    @Override
    public Object[] toArray(Object[] a) {
        return values.toArray(a);
    }


    @Override
    public boolean remove(Object o) {
        return values.remove(o);
    }

    @Override
    public boolean containsAll(Collection c) {
        return values.containsAll(c);
    }

    @Override
    public boolean addAll(Collection c) {
        return values.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection c) {
        return values.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection c) {
       return values.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection c) {
        return values.retainAll(c);
    }

    @Override
    public void clear() {
       values.clear();
    }

    @Override
    public Object get(int index) {
        return values.get(index);
    }

    @Override
    public Object set(int index, Object element) {
        return values.set(index, (E) element);
    }

    @Override
    public void add(int index, Object element) {
        values.add(index, (E) element);
    }

    @Override
    public Object remove(int index) {
       return values.remove(index);
    }

    @Override
    public int indexOf(Object o) {
       return values.indexOf((E)o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return values.lastIndexOf((E)o);
    }

    @Override
    public ListIterator listIterator() {
        return values.listIterator();
    }

    @Override
    public ListIterator listIterator(int index) {
        return values.listIterator(index);
    }

    @Override
    public List subList(int fromIndex, int toIndex) {
        return values.subList(fromIndex, toIndex);
    }

    @Override
    public boolean add(Object e) {
        return values.add((E) e); 
    }
}
