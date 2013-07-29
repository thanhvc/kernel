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
package org.exoplatform.component.aop;


/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 28, 2013  
 */
public interface Provider<T> extends javax.inject.Provider<T> {

  /**
   * Provides an instance of {@code T}. Must never return {@code null}.
   *
   * @throws OutOfScopeException when an attempt is made to access a scoped object while the scope
   *     in question is not currently active
   * @throws ProvisionException if an instance cannot be provided. Such exceptions include messages
   *     and throwables to describe why provision failed.
   */
  T get();
}
