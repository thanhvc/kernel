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

import static org.exoplatform.component.aop.matcher.Matchers.any;
import static org.exoplatform.component.aop.matcher.Matchers.only;

import java.lang.reflect.InvocationTargetException;

import junit.framework.TestCase;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.exoplatform.component.aop.ConstructionProxy;
import org.exoplatform.component.aop.intercept.ProxyFactory;
import org.exoplatform.component.aop.intercept.ProxyFactoryBuilder;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Jul 28, 2013  
 */
public class ProxyFactoryTest extends TestCase {

  public void testSimpleCase() throws NoSuchMethodException, InvocationTargetException {
    //Defines the SimpleInterceptor which implements the {@code org.aopalliance.intercept.MethodInterceptor}
    SimpleInterceptor simpleInterceptor = new SimpleInterceptor();
    LogInterceptor logInterceptor = new LogInterceptor();
    
    //Defines the {@code ProxyFactoryBuilder}
    ProxyFactoryBuilder builder = new ProxyFactoryBuilder();
    
    
//    builder.intercept(any(), any(), logInterceptor, simpleInterceptor);
    builder.intercept(only(Complex.class), any(), logInterceptor, simpleInterceptor);
    
    //Creates the {@code ProxyFactory}
    ProxyFactory factory = builder.create();
    //Gets constructor of the ConstructionProxy<Simple> class
    ConstructionProxy<Simple> constructor = factory.get(Simple.class.getDeclaredConstructor());

    //newInstance the Simple
    Simple simple = constructor.newInstance();
    //invoke this method.
    simple.invoke();
    assertTrue(simple.invoked);
//    assertTrue(simpleInterceptor.invoked);
    
    ConstructionProxy<Complex> complexconstructor = factory.get(Complex.class.getDeclaredConstructor());

    //newInstance the Simple
    Complex complex = complexconstructor.newInstance();
    //invoke this method.
    complex.invoke();
    assertTrue(complex.invoked);
    assertTrue(simpleInterceptor.invoked);

  }
  
  static class Simple {
    boolean invoked = false;
    
    public void invoke() {
      invoked = true;
      System.out.println("Simple::invoke() invoked!");
    }
  }
  
  static class Complex {
    boolean invoked = false;
    
    public void invoke() {
      invoked = true;
      System.out.println("Complex::invoke() invoked!");
    }
  }
  
  static class SimpleInterceptor implements MethodInterceptor {
    boolean invoked = false;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
      invoked = true;
      return invocation.proceed();
    }
    
  }
  
  static class LogInterceptor implements MethodInterceptor {
    
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
      try {
        System.out.println("LOG::BEGIN::" + invocation.getMethod().getName());
        return invocation.proceed();
      } finally {
        System.out.println("LOG::END::" + invocation.getMethod().getName());
      }
      
    }
  }
  
  
}

