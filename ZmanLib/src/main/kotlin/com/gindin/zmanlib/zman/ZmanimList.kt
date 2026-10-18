/*
 * Copyright (c) 2021. Jay R. Gindin
 */

package com.gindin.zmanlib.zman;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Encapsulate a list of Zmanim.
 */
public class ZmanimList implements List<Zmanim> {

  private final List<Zmanim>                                list = new ArrayList<>();

  @Nullable
  public Zmanim findType( Zmanim.Type type ) {
    for ( Zmanim z : list ) {
      if ( z.type == type ) {
        return z;
      }
    }

    return null;
  }


  @Override
  public int size() {
    return list.size();
  }


  @Override
  public boolean isEmpty() {
    return list.isEmpty();
  }


  @Override
  public boolean contains( Object o ) {
    return list.contains( o );
  }


  @NotNull
  @Override
  public Iterator<Zmanim> iterator() {
    return list.iterator();
  }


  @NotNull
  @Override
  public Object[] toArray() {
    return list.toArray();
  }


  @NotNull
  @Override
  public <T> T[] toArray( @NotNull T[] a ) {
    return list.toArray( a );
  }


  @Override
  public boolean add( Zmanim zmanim ) {
    return list.add( zmanim );
  }


  @Override
  public boolean remove( Object o ) {
    return list.remove( o );
  }


  @Override
  public boolean containsAll( @NotNull Collection<?> c ) {
    return list.containsAll( c );
  }


  @Override
  public boolean addAll( @NotNull Collection<? extends Zmanim> c ) {
    return list.addAll( c );
  }


  @Override
  public boolean addAll(
    int index,
    @NotNull Collection<? extends Zmanim> c
  ) {
    return list.addAll( index, c );
  }


  @Override
  public boolean removeAll( @NotNull Collection<?> c ) {
    return list.removeAll( c );
  }


  @Override
  public boolean retainAll( @NotNull Collection<?> c ) {
    return list.retainAll( c );
  }


  @Override
  public void clear() {
    list.clear();
  }


  @SuppressWarnings( "EqualsWhichDoesntCheckParameterClass" )
  @Override
  public boolean equals( Object o ) {
    return list.equals( o );
  }


  @Override
  public int hashCode() {
    return list.hashCode();
  }


  @Override
  public Zmanim get( int index ) {
    return list.get( index );
  }


  @Override
  public Zmanim set(
    int index,
    Zmanim element
  ) {
    return list.set( index, element );
  }


  @Override
  public void add(
    int index,
    Zmanim element
  ) {
    list.add( index, element );
  }


  @Override
  public Zmanim remove( int index ) {
    return list.remove( index );
  }


  @Override
  public int indexOf( Object o ) {
    return list.indexOf( o );
  }


  @Override
  public int lastIndexOf( Object o ) {
    return list.lastIndexOf( o );
  }


  @NotNull
  @Override
  public ListIterator<Zmanim> listIterator() {
    return list.listIterator();
  }


  @NotNull
  @Override
  public ListIterator<Zmanim> listIterator( int index ) {
    return list.listIterator( index );
  }


  @NotNull
  @Override
  public List<Zmanim> subList(
    int fromIndex,
    int toIndex
  ) {
    return list.subList( fromIndex, toIndex );
  }

} // End of ZmanimList class
