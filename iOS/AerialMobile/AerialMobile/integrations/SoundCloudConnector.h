//
// Created by Andrew Simmons on 6/24/15.
// Copyright (c) 2015 Aero. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface SoundCloudConnector : NSObject
+ (SoundCloudConnector *)instance;

- (NSArray *)getPlaylistsForUser:(NSString *)userName;
@end