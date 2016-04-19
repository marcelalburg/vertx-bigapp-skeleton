package com.weeaar.vertxwebconfig.server;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.weeaar.vertxwebconfig.annotation.VertxWebConfig;
import com.weeaar.vertxwebconfig.service.ServiceLoader;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Launcher;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class VertxApplication
    extends AbstractVerticle
{
    static Logger logger = LoggerFactory.getLogger( VertxApplication.class );

    public static void main( String[] args )
    {
        Launcher.main( new String[] { "run", "service:" + VertxApplication.class.getName() } );
    }

    @Override
    public void start()
        throws Exception
    {
        logger.debug( "starting" );

        ServiceLoader serviceLoader = new ServiceLoader();
        List<String> verticals = serviceLoader.findVerticles();

        @SuppressWarnings( "rawtypes" )
        List<Future> allVerticalFuture = new ArrayList<Future>();

        JsonObject portWithRoutes = new JsonObject();
        portWithRoutes.put( "portWithRoutes", new JsonObject() );

        // List<JsonObject> routes = new ArrayList<JsonObject>();

        if ( null != verticals )
        {
            for ( String name : verticals )
            {
                /*
                 * Figure out routes
                 */
                if ( !name.contains( ".js" ) )
                {
                    Method[] methods = Class.forName( name ).getMethods();
                    for ( Method method : methods )
                    {
                        if ( method.isAnnotationPresent( VertxWebConfig.class ) )
                        {
                            VertxWebConfig vertxWebConfig = method.getAnnotation( VertxWebConfig.class );

                            JsonObject route = new JsonObject();
                            if ( null != vertxWebConfig.channelName() )
                            {
                                route.put( "channelName", vertxWebConfig.channelName() );
                            }

                            if ( null != vertxWebConfig.path() )
                            {
                                if ( vertxWebConfig.pathIsRegex() )
                                {
                                    route.put( "pathRegex", vertxWebConfig.path() );
                                }
                                else
                                {
                                    route.put( "path", vertxWebConfig.path() );
                                }
                            }

                            if ( !route.isEmpty() )
                            {
                                String port = "DEFAULT";

                                if ( !vertxWebConfig.port().isEmpty() )
                                {
                                    port = vertxWebConfig.port();
                                }

                                if ( !portWithRoutes.getJsonObject( "portWithRoutes" ).containsKey( port ) )
                                {

                                    portWithRoutes.getJsonObject( "portWithRoutes" ).put( port,
                                                                                          new JsonObject().put( "routes",
                                                                                                                new JsonArray() ) );
                                }

                                portWithRoutes.getJsonObject( "portWithRoutes" ).getJsonObject( port ).getJsonArray( "routes" ).add( route );
                            }
                        }
                    }
                }

                /*
                 * Starting verticle
                 */
                Future<String> verticleFuture = Future.future();

                logger.info( "Deploy verticle " + name );

                vertx.deployVerticle( name, verticleFuture.completer() );
                allVerticalFuture.add( verticleFuture );
            }
        }

        if ( allVerticalFuture.size() > 0 )
        {
            CompositeFuture.all( allVerticalFuture ).setHandler( ar -> {
                if ( ar.succeeded() )
                {
                    logger.info( "All verticals successfull deployed" );

                    startServerVerticle( portWithRoutes );
                }
                else
                {
                    logger.error( "Deploying verticals failed", ar.cause() );
                }
            } );
        }
        else
        {
            startServerVerticle( portWithRoutes );
        }
    }

    void startServerVerticle( JsonObject portWithRoutes )
    {
        if ( config().containsKey( "serverService" ) )
        {
            JsonObject serverServiceOptions = config().getJsonObject( "serverService" );

            String serverVerticleName = serverServiceOptions.getString( "name" );

            /*
             * JsonObject deployConfig = new JsonObject(); deployConfig.put( "routes", portWithRoutes );
             */

            DeploymentOptions options = new DeploymentOptions();

            if ( serverServiceOptions.containsKey( "config" ) )
            {
                JsonObject serverConfig = serverServiceOptions.getJsonObject( "config" );

                for ( Map.Entry<String, Object> configObject : serverConfig.getMap().entrySet() )
                {
                    portWithRoutes.put( configObject.getKey(), configObject.getValue() );
                }

                options.setConfig( portWithRoutes );
            }

            vertx.deployVerticle( serverVerticleName, options );
        }
    }
}