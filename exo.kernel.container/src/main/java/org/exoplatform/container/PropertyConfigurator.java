/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.container;

import org.exoplatform.commons.utils.PropertiesLoader;
import org.exoplatform.commons.utils.PropertyManager;
import org.exoplatform.commons.utils.Tools;
import org.exoplatform.container.configuration.ConfigurationManager;
import org.exoplatform.container.xml.Deserializer;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.container.xml.PropertiesParam;
import org.exoplatform.container.xml.Property;
import org.exoplatform.container.xml.ValueParam;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.picocontainer.Startable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * <p>The property configurator configures a set of system properties via the {@link PropertyManager}
 * static methods. It is possible to configure properties from the init params or from an external
 * file.</p>
 *
 * <p>The constructor will inspect the {@link org.exoplatform.container.xml.InitParams} params argument
 * to find a param named <code>properties</code> with an expected type of {@link PropertiesParam}. The
 * properties contained in that argument will be sourced into the property manager. When such properties
 * are loaded from an XML configuration file, the values are evaluated and property substitution occurs.</p>
 *
 * <p>When the property {@link PropertyManager#PROPERTIES_URL} is not null and points to a valid property
 * file it will loaded and sourced. Property values will be evaluated and property substitution will
 * occur. When the file name ends with the <code>.properties</code> properties are loaded using the
 * {@link Properties#load(java.io.InputStream)} method. When the file name ends with the <code>.xml</code>
 * properties are loaded using the {@link Properties#loadFromXML(java.io.InputStream)} method. Suffix
 * checks are done ignoring the case.</p>
 *
 * <p>When properties are loaded from an URL, the order of the properties declarations in the file matters.</p>
 *
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class PropertyConfigurator implements Startable
{

   /** The logger. */
   private final Log log = ExoLogger.getExoLogger(PropertyConfigurator.class);

   public PropertyConfigurator(InitParams params, ConfigurationManager confManager)
   {
      PropertiesParam propertiesParam = params.getPropertiesParam("properties");
      if (propertiesParam != null)
      {
         log.debug("Going to initialize properties from init param");
         for (Iterator<Property> i = propertiesParam.getPropertyIterator();i.hasNext();)
         {
            Property property = i.next();
            String name = property.getName();
            String value = property.getValue();
            log.debug("Adding property from init param " + name + " = " + value);
            PropertyManager.setProperty(name, value);
         }
      }

      //
      String path = null;
      ValueParam pathParam = params.getValueParam("properties.url");
      if (pathParam != null)
      {
         log.debug("Using file path " + path + " found from configuration");
         path = pathParam.getValue();
      }

      //
      String systemPath = PropertyManager.getProperty(PropertyManager.PROPERTIES_URL);
      if (systemPath != null)
      {
         log.debug("Using file path " + path + " found from system properties");
         path = systemPath;
      }

      //
      if (path != null)
      {
         log.debug("Found property file path " + path);
         InputStream in = null;
         try
         {
            URL url = confManager.getURL(path);

            //
            if (url != null)
            {
               in = url.openStream();
            }

            //
            if (in != null)
            {
               LinkedHashMap<String, String> props = null;
               String fileName = url.getFile();
               if (Tools.endsWithIgnoreCase(path, ".properties"))
               {
                  log.debug("Attempt to load property file " + path);
                  props = PropertiesLoader.load(in);
               }
               else if (Tools.endsWithIgnoreCase(fileName, ".xml"))
               {
                  log.debug("Attempt to load property file " + path + " with XML format");
                  props = PropertiesLoader.loadFromXML(in);
               }
               else
               {
                  log.debug("Will not load property file" + path + " because its format is not recognized");
               }
               if (props != null)
               {
                  for (Map.Entry<String, String> entry : props.entrySet())
                  {
                     String propertyName = entry.getKey();
                     String propertyValue = entry.getValue();
                     propertyValue = Deserializer.resolveString(propertyValue);
                     PropertyManager.setProperty(propertyName, propertyValue);
                  }
               }
            }
            else
            {
               log.error("Could not load property file " + path);
            }
         }
         catch (Exception e)
         {
            log.error("Cannot load property file " + path, e);
         }
         finally
         {
            if (in != null)
            {
               try
               {
                  in.close();
               }
               catch (IOException ignore)
               {
               }
            }
         }
      }
   }

   public void start()
   {
   }

   public void stop()
   {
   }
}
