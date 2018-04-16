//
// Created by Andrew Simmons on 6/23/15.
// Copyright (c) 2015 Aero. All rights reserved.
//

#import "RadioConnector.h"
#import "GCDAsyncUdpSocket.h"
#import "Station.h"
#import "StationManager.h"
#import "IRadioConnectorDelegate.h"
#import "SoundAsset.h"
#import "RadioSocketClient.h"


NSString * const NOTIFICATION_GOT_PLAYLISTS_FOR_STATION = @"NOTIFICATION_GOT_PLAYLISTS_FOR_STATION";
NSString * const NOTIFICATION_CONNECTED_TO_RADIO = @"NOTIFICATION_CONNECTED_TO_RADIO";
NSString * const NOTIFICATION_GOT_STATIONS = @"NOTIFICATION_GOT_STATIONS";
NSString * const NOTIFICATION_GOT_CURRENT_FREQUENCY = @"NOTIFICATION_GOT_CURRENT_FREQUENCY";

@implementation RadioConnector {
    GCDAsyncUdpSocket *udpSocket;
    NSString *radioURL;
    NSObject <IRadioConnectorDelegate> *_delegate;
	NSOperationQueue *_operationsQueue;
}

@synthesize radioURL;


+ (RadioConnector *)instance {
    static RadioConnector *_instance = nil;

    @synchronized (self) {
        if (_instance == nil) {
            _instance = [[self alloc] init];
        }
    }

    return _instance;
}

- (instancetype)init
{
	self = [super init];
	if (self)
	{
		_operationsQueue = [NSOperationQueue new];
        _socketClient = [RadioSocketClient new];
	}

	return self;
}


-(void) listenForBroadcastedIP
{
    udpSocket = [[GCDAsyncUdpSocket alloc] initWithDelegate:self delegateQueue:dispatch_get_main_queue()];
    [udpSocket enableBroadcast:YES error:nil];
    NSError *error = nil;

    if (![udpSocket bindToPort:2666 error:&error]) {
        NSLog(@"Error starting server (bind): %@", error.description );
        return;
    }

    if(![udpSocket joinMulticastGroup:@"239.0.0.0" error:&error] ) { //]onInterface:@"en0" error:&error]) {
        NSLog(@"Error joining multicast group: %@",error.description);
        return;
    }

    if (![udpSocket receiveOnce:&error]) {
        [udpSocket close];
        NSLog(@"Error starting server (recv): %@", error.description);
        return;
    }

    NSLog(@"Udp server started on port %@:%hu", [udpSocket localHost_IPv4], [udpSocket localPort]);

}

- (void)udpSocket:(GCDAsyncUdpSocket *)sock didReceiveData:(NSData *)data fromAddress:(NSData *)address withFilterContext:(id)filterContext {
    NSString *msg = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
    NSLog(@"message rec'd: %@:%hu %@\n", [udpSocket localHost_IPv4], [udpSocket localPort],msg);
    radioURL = [msg stringByReplacingOccurrencesOfString:@"\n" withString:@""];
	[_delegate connectedToRadio];
	[udpSocket close];
	[[NSNotificationCenter defaultCenter] postNotificationName:NOTIFICATION_CONNECTED_TO_RADIO object:self userInfo:nil];
}

-(NSURL *) radioURL:(NSString *) function
{
    NSString *urlAsString = [NSString stringWithFormat:@"http://%@/aerial/%@", radioURL, function];
    NSURL *url = [[NSURL alloc] initWithString:urlAsString];
    return url;
}


-(void) populateStationWithSoundcloudPlaylistWithFreq:(int) freq soundCloudPlaylistId:(int) playlistId
{
    NSString *urlAsString = [NSString stringWithFormat:@"http://%@/aerial/populateplaylist?frequency=%d&playlistId=%d", radioURL, freq, playlistId];

    NSURL *url = [[NSURL alloc] initWithString:urlAsString];

    NSURLResponse *response = nil;
    NSError *error = nil;
    NSData *data = [NSURLConnection sendSynchronousRequest:[[NSURLRequest alloc] initWithURL:url] returningResponse:&response error:&error];

    NSString *string = [[NSString alloc] initWithData:data encoding:NSASCIIStringEncoding];
}

-(void) getStations
{
    [[StationManager instance] clearAllStations];

    NSURL *url = [self radioURL:@"getstations"];

    [NSURLConnection sendAsynchronousRequest:[[NSURLRequest alloc] initWithURL:url] queue:_operationsQueue completionHandler:^(NSURLResponse *response, NSData *data, NSError *error) {
//        if(error)
//            NSLog(error);

        NSDictionary *results = [NSJSONSerialization JSONObjectWithData:data
                                                                 options:0
                                                                   error:NULL];

//        NSLog(results);

        for (NSDictionary *stationDict in results)
        {
            Station *newStation = [Station new];
            NSNumber * frequency = (NSNumber *) [stationDict valueForKey:@"frequency"];
            newStation.frequency = frequency;
            newStation.stationName = [stationDict valueForKey:@"title"];
			newStation.stationTypeString = (NSString *) [stationDict valueForKey:@"type"];
            [[StationManager instance] addStation:newStation];
        }

        NSMutableArray *stations = [[StationManager instance] stations];
        NSSortDescriptor *sortDescriptor = [[NSSortDescriptor alloc] initWithKey:@"frequency" ascending:YES];
        [stations sortUsingDescriptors:@[sortDescriptor]];

        NSObject <IRadioConnectorDelegate> *delegate = [RadioConnector instance].delegate;
        [delegate gotStations];
		dispatch_async(dispatch_get_main_queue(), ^
		{
			[[NSNotificationCenter defaultCenter] postNotificationName:NOTIFICATION_GOT_STATIONS object:self userInfo:nil];
		});
//        if (error) {
//            [self.delegate fetchingGroupsFailedWithError:error];
//        } else {
//            [self.delegate receivedGroupsJSON:data];
//        }
    }];
}
// Do any additional setup after loading the view,


-(void)getTrackPlaylistAtFrequency:(int) freq
{
	NSString *urlAsString = [NSString stringWithFormat:@"http://%@/aerial/getplaylistatstation?frequency=%d", radioURL, freq];
	NSURL *url = [[NSURL alloc] initWithString:urlAsString];

    [NSURLConnection sendAsynchronousRequest:[[NSURLRequest alloc] initWithURL:url] queue:_operationsQueue completionHandler:^(NSURLResponse *response, NSData *data, NSError *error) {
//        if(error)
//            NSLog(error);

        NSDictionary *results = [NSJSONSerialization JSONObjectWithData:data
                                                                options:0
                                                                  error:NULL];

//        NSLog(results);

        NSMutableArray *playlist = [NSMutableArray new];
        for (NSDictionary *soundAssetDict in results) {
            SoundAsset *soundAsset = [SoundAsset new];
            soundAsset.title = (NSString *) [soundAssetDict valueForKey:@"title"];
            soundAsset.artist = (NSString *) [soundAssetDict valueForKey:@"artist"];
            [playlist addObject:soundAsset];
        }
		dispatch_async(dispatch_get_main_queue(), ^
		{
			[[NSNotificationCenter defaultCenter] postNotificationName:NOTIFICATION_GOT_PLAYLISTS_FOR_STATION object:self userInfo:@{@"playlist" : playlist}];
		});
//        if (error) {
//            [self.delegate fetchingGroupsFailedWithError:error];
//        } else {
//            [self.delegate receivedGroupsJSON:data];
//        }
    }];

}
-(void) getCurrentFrequency
{
    NSURL *url = [self radioURL:@"getfrequency"];

    [NSURLConnection sendAsynchronousRequest:[[NSURLRequest alloc] initWithURL:url] queue:_operationsQueue completionHandler:^(NSURLResponse *response, NSData *data, NSError *error) 
    {
        NSString *string = [[NSString alloc] initWithData:data encoding:NSASCIIStringEncoding];
        [StationManager instance].currentFrequency = string.integerValue;
		[_delegate performSelectorOnMainThread:@selector(gotFrequency:) withObject:@(string.integerValue) waitUntilDone:YES];
		dispatch_async(dispatch_get_main_queue(), ^
		{
			[[NSNotificationCenter defaultCenter] postNotificationName:NOTIFICATION_GOT_CURRENT_FREQUENCY object:self userInfo:@{@"frequency" : @(string.integerValue)}];
		});

    }];
}


@end