//
// Created by Andrew Simmons on 6/24/15.
// Copyright (c) 2015 Aero. All rights reserved.
//

#import "SoundCloudConnector.h"
#import "SoundCloudPlaylist.h"


@implementation SoundCloudConnector 
{
    NSString *_lastUserId;
};

+ (SoundCloudConnector *)instance {
    static SoundCloudConnector *_instance = nil;

    @synchronized (self) {
        if (_instance == nil) {
            _instance = [[self alloc] init];
        }
    }

    return _instance;
}


-(NSArray *) getPlaylistsForUser:(NSString *) userName
{
    [self getUserIdFromUserName:userName];

    NSMutableArray *playlists = [NSMutableArray new];

    NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:@"https://api.soundcloud.com/users/%@/playlists.json?consumer_key=apigee", userName]];

    NSURLResponse *response = nil;
    NSError *error = nil;
    NSData *data = [NSURLConnection sendSynchronousRequest:[[NSURLRequest alloc] initWithURL:url] returningResponse:&response error:&error];

    NSArray *results = [NSJSONSerialization JSONObjectWithData:data
                                                       options:0
                                                         error:NULL];
      
    for(NSDictionary *playlistObj in results)
    {
        SoundCloudPlaylist *playlist = [SoundCloudPlaylist new];
        playlist.title = (NSString *) playlistObj[@"title"];
        playlist.id = (NSNumber *) playlistObj[@"id"];
        [playlists addObject:playlist];
    }

    return playlists;
//    _lastUserId = userId;

}

-(void) getUserIdFromUserName:(NSString *) userName
{
    NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:@"https://api.soundcloud.com/users.json?consumer_key=apigee&q=%@", userName]];

    NSURLResponse *response = nil;
    NSError *error = nil;
    NSData *data = [NSURLConnection sendSynchronousRequest:[[NSURLRequest alloc] initWithURL:url] returningResponse:&response error:&error];
//    [NSURLConnection sendAsynchronousRequest:[[NSURLRequest alloc] initWithURL:url] queue:[[NSOperationQueue alloc] init] completionHandler:^(NSURLResponse *response, NSData *data, NSError *error) {
//        if(error)
//            NSLog(error);

        NSArray *results = [NSJSONSerialization JSONObjectWithData:data
                                                                options:0
                                                                  error:NULL];
        NSString *userId = results[0][@"id"];
        _lastUserId = userId;

/*
        for (NSDictionary *stationDict in results) {
            Station *newStation = [Station new];
            NSNumber * frequency = (NSNumber *) [stationDict valueForKey:@"frequency"];
            newStation.frequency = frequency;
            newStation.stationName = [stationDict valueForKey:@"title"];
            [[StationManager instance] addStation:newStation];
        }

        NSMutableArray *stations = [[StationManager instance] stations];
        NSSortDescriptor *sortDescriptor = [[NSSortDescriptor alloc] initWithKey:@"frequency" ascending:YES];
        [stations sortUsingDescriptors:@[sortDescriptor]];

        [_delegate gotStations];
        */
//        if (error) {
//            [self.delegate fetchingGroupsFailedWithError:error];
//        } else {
//            [self.delegate receivedGroupsJSON:data];
//        }
//    }];
}
@end