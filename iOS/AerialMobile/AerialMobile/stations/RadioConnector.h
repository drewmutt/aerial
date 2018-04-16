//
// Created by Andrew Simmons on 6/23/15.
// Copyright (c) 2015 Aero. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol IRadioConnectorDelegate;
@class RadioSocketClient;


@interface RadioConnector : NSObject
@property(nonatomic, strong) NSObject <IRadioConnectorDelegate> *delegate;

@property(nonatomic, readonly) NSString *radioURL;

@property(nonatomic, readonly) RadioSocketClient *socketClient;

+ (RadioConnector *)instance;

- (void)listenForBroadcastedIP;

- (void)populateStationWithSoundcloudPlaylistWithFreq:(int)freq soundCloudPlaylistId:(int)playlistId;

- (void)getStations;

- (void)getTrackPlaylistAtFrequency:(int)freq;

- (void)getCurrentFrequency;

@end

extern NSString * const NOTIFICATION_GOT_PLAYLISTS_FOR_STATION;
extern NSString * const NOTIFICATION_CONNECTED_TO_RADIO;
extern NSString * const NOTIFICATION_GOT_STATIONS;
extern NSString * const NOTIFICATION_GOT_CURRENT_FREQUENCY;