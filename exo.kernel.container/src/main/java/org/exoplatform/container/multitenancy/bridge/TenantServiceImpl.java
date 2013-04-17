/*
 * Copyright (C) 2013 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.exoplatform.container.multitenancy.bridge;

import org.exoplatform.container.multitenancy.CurrentTenantNotSetException;
import org.exoplatform.container.multitenancy.Tenant;
import org.exoplatform.container.multitenancy.TenantService;
import org.exoplatform.container.multitenancy.TenantStateListener;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Implementation of {@link TenantService} based on plugins. Following kinds of plugin supported: <ul>
 * <li>
 * CurrentTenantLookup
 * </li>
 * <li>
 * TenantStateObserver
 * </li>
 * </ul>
 * 
 * NOTE: TenantServiceImpl can be consumed before the container start (e.g. to add a listener or check current tenant). 
 * 
 * @author <a href="mailto:pnedonosko@exoplatform.com">Peter Nedonosko</a>
 * 
 */
public class TenantServiceImpl implements TenantService
{

   protected static final Log LOG = ExoLogger.getLogger("exo.kernel.container.TenantServiceImpl");

   /**
    * List of registered {@link CurrentTenantLookup} implementations.
    */
   protected final List<CurrentTenantLookup> lookups = new CopyOnWriteArrayList<CurrentTenantLookup>();

   /**
    * List of registered {@link TenantStateObserver} implementations.
    */
   protected final List<TenantStateObserver> observers = new CopyOnWriteArrayList<TenantStateObserver>();

   /**
    * Register component plugin. Used by container during the service instantiation. Not recommended for use in runtime.
    * 
    * @param plugin {@link CurrentTenantLookup}
    */
   public void addPlugin(CurrentTenantLookup plugin)
   {
      lookups.add(plugin);
      LOG.info("CurrentTenantLookup instance registered: " + plugin.toString());
   }

   /**
    * Register component plugin. Used by container during the service instantiation. Not recommended for use in runtime.
    * 
    * @param plugin {@link TenantStateObserver}
    */
   public void addPlugin(TenantStateObserver plugin)
   {
      observers.add(plugin);
      LOG.info("TenantStateObserver instance registered: " + plugin.toString());
   }

   /**
    * {@inheritDoc}
    */
   public void addListener(TenantStateListener listener)
   {
      for (TenantStateObserver o : observers)
      {
         o.addListener(listener);
      }
   }

   /**
    * {@inheritDoc}
    */
   public void removeListener(TenantStateListener listener)
   {
      for (TenantStateObserver o : observers)
      {
         o.removeListener(listener);
      }
   }

   /**
    * {@inheritDoc}
    */
   public Tenant getCurrentTenant() throws CurrentTenantNotSetException
   {
      for (CurrentTenantLookup l : lookups)
      {
         if (l.hasCurrentTenant())
         {
            return l.getCurrentTenant();
         }
      }

      throw new CurrentTenantNotSetException("Current Tenant not set.");
   }

   /**
    * {@inheritDoc}
    */
   public boolean hasCurrentTenant()
   {
      for (CurrentTenantLookup l : lookups)
      {
         if (l.hasCurrentTenant())
         {
            return true;
         }
      }

      return false;
   }
}
