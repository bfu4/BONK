/**
   * MIT License
   * <p>
   * Copyright (c) 2021 bfu4
   * <p>
   * Permission is hereby granted, free of charge, to any person obtaining a copy
   * of this software and associated documentation files (the "Software"), to deal
   * in the Software without restriction, including without limitation the rights
   * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
   * copies of the Software, and to permit persons to whom the Software is
   * furnished to do so, subject to the following conditions:
   * <p>
   * The above copyright notice and this permission notice shall be included in all
   * copies or substantial portions of the Software.
   * <p>
   * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
   * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
   * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
   * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
   * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
   * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
   * SOFTWARE.
   */
package com.github.bfu4.bonk.utility;

import java.util.Arrays;
import java.util.List;

/**
 * Rotating {@link java.util.ArrayList } with size limitations.
 * @param <E> Element Type
 */
public class LimitedList<E> {

   private int maxSize;
   private Object[] elements;

   private final Object lock;

   /**
    * Create a new list
    *
    * @param maxSize max size of the list
    */
   public LimitedList(int maxSize) {
      this.maxSize = maxSize;
      this.lock = new Object();

      elements = new Object[0];
   }

   /**
    * Set the item at the specified index
    *
    * @param index index to set the element at
    * @param element element to set
    * @throws IndexOutOfBoundsException if the index is larger than the dynamic {@link LimitedList#size()}
    */
   public void set(int index, E element) throws IndexOutOfBoundsException {
      if (elements.length == 0) {
         elements = new Object[1];
         elements[0] = element;
      } else {
         if (index != elements.length && index < elements.length) {
            elements[index] = element;
            return;
         }
         throw new IndexOutOfBoundsException("Index is larger than the current length of the dynamic LimitedList!");
      }
   }

   public void setMaxSize(int newSize) {
      synchronized (lock) {
         this.maxSize = newSize;
      }
   }

   /**
    * Add an element to the list.
    *
    * @param element element to add
    */
   public void add(E element) {
      synchronized (lock) {
         if (elements.length == maxSize) {
            // Scoot objects over
            for (int i = 1; i < elements.length; i++) {
               elements[i - 1] = elements[i];
            }
            elements[maxSize - 1] = element;
         } else {
            int maxRealloc = (elements.length >= 1) ? elements.length + 1 : 1;
            Object[] tempArray = new Object[maxRealloc];

            for (int i = 0; i < elements.length; i ++) {
               tempArray[i] = elements[i];
            }

            tempArray[maxRealloc - 1] = element;
            elements = tempArray;
         }
      }
   }

   /**
    * Remove element from list.
    *
    * @param element element to remove
    */
   public void remove(E element) {
      synchronized (lock) {
         int index = indexOf(element);
         if (index == -1) return;

         elements[index] = null;
         shiftOverNull();
      }
   }

   /**
    * Get element from list.
    *
    * @param index index of element
    * @return element
    */
   @SuppressWarnings("unchecked")
   public E get(int index) {
      return (E) elements[index];
   }

   /**
    * Get the index of the given element
    *
    * @param element element to get the index of
    * @return index of the element, or -1 if not found.
    */
   public int indexOf(E element) {
      int index = -1;
      for (int i = 0; i < elements.length; i ++) {
         if (elements[i] == null) continue;
         if (elements[i].equals(element)) {
            index = i;
            break;
         }
      }
      return index;
   }

   /**
    * Turn this list into an array.
    *
    * @deprecated using this alone can throw a {@link ClassCastException }
    * @return an array of the elements stored in this list.
    */
   @SuppressWarnings("unchecked")
   @Deprecated
   @Unsafe
   public E[] toArray() {
      return (E[]) elements;
   }

   /**
    * Shift over null values.
    */
   private void shiftOverNull() {
      Object[] newArray = new Object[elements.length - nullElementAmount()];
      int currentIndex = 0;
      for (Object element: elements) {
         if (element != null) {
            newArray[currentIndex] = element;
            currentIndex ++;
         }
      }
      elements = newArray;
   }

   /**
    * Get the amount of null elements
    *
    * @return amount of elements null
    */
   private int nullElementAmount() {
      int amount = 0;
      for (Object element : elements) {
         if (element == null) amount++;
      }
      return amount;
   }

   /**
    * Get the amount of items stored in this list.
    *
    * @return amount of items stored in the list
    */
   public int size() { return elements.length; }

   /**
    * Clear the entire list.
    */
   public void clear() {
      elements = new Object[0];
   }

   @Override
   public String toString() {
      return Arrays.toString(elements);
   }

   /**
    * Turn this list into {@link java.util.List }
    *
    * @return Limited list as a list
    */
   public List<E> asList() {
      return Arrays.asList(toArray());
   }

}