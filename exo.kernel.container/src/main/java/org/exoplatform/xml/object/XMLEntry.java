/*
 * Copyright (C) 2009 eXo Platform SAS.
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
package org.exoplatform.xml.object;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Apr 11, 2005
 * @version $Id: XMLEntry.java 5799 2006-05-28 17:55:42Z geaz $
 */
public class XMLEntry
{

   private XMLBaseObject key_;

   private XMLBaseObject value_;

   public XMLEntry()
   {
   }

   public XMLEntry(Object key, Object val) throws Exception
   {
      key_ = new XMLKey(null, key);
      value_ = new XMLValue(null, val);
   }

   public XMLBaseObject getKey()
   {
      return key_;
   }

   public void setKey(XMLBaseObject key)
   {
      key_ = key;
   }

   public XMLBaseObject getValue()
   {
      return value_;
   }

   public void setValue(XMLBaseObject value)
   {
      value_ = value;
   }
}
