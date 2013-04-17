package org.exoplatform.container.multitenancy.bridge;

import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.jmx.MX4JComponentAdapter;
import org.exoplatform.container.xml.InitParams;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.defaults.InstanceComponentAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * {@link TenantContainerContext} implementation for tests.
 * 
 * @author <a href="mailto:pnedonosko@exoplatform.org">Peter Nedonosko</a>
 * @version $Id: DummyTenantContainerContext.java 000000 Mar 6, 2013 4:32:48 PM pnedonosko $
 *
 */
public class DummyTenantContainerContext implements TenantContainerContext
{

   public Object lastGetKey;

   public Object lastGetListKey;

   public Object lastRegisteredKey;

   public Object lastUnregisteredKey;

   private Set<Object> registeredKeys = new HashSet<Object>();

   public DummyTenantContainerContext(ExoContainerContext ctx, InitParams config)
   {
   }

   public List getComponentAdaptersOfType(Class componentType)
   {
      lastGetListKey = componentType;
      return null;
   }

   public List getComponentInstancesOfType(Class componentType)
   {
      lastGetListKey = componentType;
      return null;
   }

   public ComponentAdapter getComponentAdapterOfType(Class key)
   {
      lastGetKey = key;
      return null;
   }

   public Object getComponentInstance(Object componentKey)
   {
      lastGetKey = componentKey;
      //return container.getComponentInstance(componentKey);
      return null;
   }

   public Object getComponentInstanceOfType(Class<?> componentType)
   {
      lastGetKey = componentType;
      return null;
   }

   public boolean accept(ComponentAdapter adapter)
   {
      return !(adapter instanceof InstanceComponentAdapter);
   }

   public boolean accept(Object key)
   {
      boolean res = registeredKeys.contains(key);
      if (res)
      {
         return true;
      }
      else if (key instanceof Class<?>)
      {
         List<Class<?>> types = getRegisteredTypes((Class<?>)key);
         return types.size() > 0;
      }

      return false;
   }

   public List<Class<?>> getRegisteredTypes(Class<?> keyType)
   {
      List<Class<?>> subclasses = new ArrayList<Class<?>>();
      for (Object k : registeredKeys)
      {
         if (k instanceof Class)
         {
            Class<?> componentType = (Class<?>)k;
            if (keyType == null || keyType.isAssignableFrom(componentType))
            {
               subclasses.add(componentType);
            }
         }
      }
      return subclasses;
   }

   public ComponentAdapter registerComponent(ComponentAdapter component) throws TenantComponentRegistrationException
   {
      if (!TenantContainerContext.class.equals(component.getComponentKey()))
      {
         lastRegisteredKey = component.getComponentKey();
         registeredKeys.add(component.getComponentKey());
         return component;
      }
      return new MX4JComponentAdapter(component.getComponentKey(), component.getComponentImplementation()); // dummy stuff to return not null
   }

   public ComponentAdapter unregisterComponent(Object componentKey) throws TenantComponentRegistrationException
   {
      if (!TenantContainerContext.class.equals(componentKey))
      {
         lastUnregisteredKey = componentKey;
         registeredKeys.remove(componentKey);
         return new MX4JComponentAdapter(componentKey, this.getClass()); // dummy stuff to return not null
      }
      return null;
   }
}
