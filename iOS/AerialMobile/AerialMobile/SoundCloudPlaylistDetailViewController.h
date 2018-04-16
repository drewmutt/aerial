//
//  SoundCloudPlaylistDetailViewController.h
//  AerialMobile
//
//  Created by Andrew Simmons on 6/24/15.
//  Copyright (c) 2015 Aero. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "IRadioConnectorDelegate.h"

@class SoundCloudPlaylist;
@class Station;

@interface SoundCloudPlaylistDetailViewController : UIViewController <IRadioConnectorDelegate>

@property(nonatomic, strong) SoundCloudPlaylist *playlist;
@property(nonatomic, strong) Station *station;

- (void)gotStations;
@end
