package aero.aerial.web;

import org.restlet.*;
import org.restlet.data.Protocol;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Router;
import org.restlet.service.CorsService;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by andrewsimmons on 6/4/15.
 */
public class RestletServer {
    public static void startServer()
    {
        final Component component = new Component();

// Add a new HTTP server listening on port 8182.
        component.getServers().add(Protocol.HTTP, 8080);

        final Router router = new Router(component.getContext().createChildContext());

        router.attach("/getplaylists", SoundCloudGetPlaylists.class);
        router.attach("/getstations", GetStations.class);
        router.attach("/getfrequency", GetCurrentFrequency.class);
        router.attach("/populateplaylist", PopulateStationWithSoundcloudPlaylist.class);
        router.attach("/getplaylistatstation", GetPlaylistAtStationRequest.class);

/*
        CorsService corsService = new CorsService();
        corsService.setAllowedOrigins(new HashSet(Arrays.asList("*")));
        corsService.setAllowedCredentials(true);

        component.getDefaultHost().attachDefault(application);
// Attach the sample application.
*/
        component.getDefaultHost().attach("/aerial", router);

// Start the component.
        try {
            component.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
