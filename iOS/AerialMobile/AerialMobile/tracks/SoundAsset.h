//
// Created by Andrew Simmons on 6/26/15.
// Copyright (c) 2015 Aero. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ITrack.h"


@interface SoundAsset : NSObject <ITrack>
@property(nonatomic, copy) NSString *title;
@property(nonatomic, copy) NSString *artist;
@end