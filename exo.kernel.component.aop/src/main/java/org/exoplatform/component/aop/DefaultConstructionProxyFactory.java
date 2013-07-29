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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastConstructor;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 28, 2013  
 */
/**
 * Default {@link ConstructionProxyFactory} implementation. Simply invokes the
 * constructor. Can be reused by other @ ConstructionProxyFactory}
 * implementations. 
 */
public class DefaultConstructionProxyFactory implements ConstructionProxyFactory {
  @Override
  public <T> ConstructionProxy<T> get(Constructor<T> constructor) {

    FastClass fastClass = FastClass.create(constructor.getDeclaringClass());
    final FastConstructor fastConstructor = fastClass.getConstructor(constructor);

    return new ConstructionProxy<T>() {

      @Override
      @SuppressWarnings({ "unchecked" })
      public T newInstance(Object... arguments) throws InvocationTargetException {
        return (T) fastConstructor.newInstance(arguments);
      }

    };
  }

}
