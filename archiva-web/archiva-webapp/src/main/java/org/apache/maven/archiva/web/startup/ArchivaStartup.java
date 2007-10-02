package org.apache.maven.archiva.web.startup;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.maven.archiva.common.ArchivaException;
import org.apache.maven.archiva.scheduled.ArchivaTaskScheduler;
import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;

/**
 * ArchivaStartup - the startup of all archiva features in a deterministic order. 
 *
 * @author <a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>
 * @version $Id$
 * 
 * @plexus.component 
 *              role="org.apache.maven.archiva.web.startup.ArchivaStartup"
 *              role-hint="default"
 */
public class ArchivaStartup
    extends AbstractLogEnabled
    implements Initializable
{
    /**
     * @plexus.requirement role-hint="default"
     */
    private ConfigurationSynchronization configSync;

    /**
     * @plexus.requirement role-hint="default"
     */
    private ResolverFactoryInit resolverFactory;

    /**
     * @plexus.requirement role-hint="default"
     */
    private ArchivaTaskScheduler taskScheduler;

    public void initialize()
        throws InitializationException
    {
        Banner.display( getLogger(), ArchivaVersion.determineVersion( this.getClass().getClassLoader() ) );

        try
        {
            configSync.startup();
            resolverFactory.startup();
            taskScheduler.startup();
        }
        catch ( ArchivaException e )
        {
            throw new InitializationException( "Unable to properly startup archiva: " + e.getMessage(), e );
        }
    }

}
