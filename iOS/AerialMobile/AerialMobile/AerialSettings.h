//
// Created by Andrew Simmons on 6/29/15.
// Copyright (c) 2015 Aero. All rights reserved.
//

#import <Foundation/Foundation.h>

@class Range;


@interface AerialSettings : NSObject
@property(readonly, nonatomic) NSTimeInterval tunerFrequencyUpdateInSec;
@property(readonly, nonatomic) unsigned int tunerRunningAverageSampleSize;

- (int)getMinStationFrequency;

- (int)getMaxStationFrequency;

- (Range *)getStationFrequencyRange;

+ (AerialSettings *)instance;

@end