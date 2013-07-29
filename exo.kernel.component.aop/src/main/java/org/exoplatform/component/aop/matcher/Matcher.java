/*
 * Copyright (C) 2003-2013 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.exoplatform.component.aop.matcher;

/**
 * Returns {@code true} or {@code false} or a given input.
 * 
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 28, 2013  
 */
public interface Matcher<T> {

  /**
   * Returns {@code true} if this matches {@code t}, {@code false} otherwise.
   * @param t
   * @return
   */
  boolean matches(T t);
  
  /**
   * Returns a new matcher which return {@code true} if both an 
   * given matcher return {@code true}
   * 
   * @param other
   * @return
   */
  Matcher<T> and(Matcher<? super T> other);
  
  /**
   * Returns a new matcher returns {@code true} if either
   *  this given matcher return {@code true}.
   *  
   * @param other
   * @return
   */
  Matcher<T> or(Matcher<? super T> other);
}
