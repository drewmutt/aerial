//
// Created by Andrew Simmons on 6/23/15.
// Copyright (c) 2015 Aero. All rights reserved.
//

#import <Foundation/Foundation.h>

@class Station;


@interface StationManager : NSObject


@property(nonatomic, strong, readonly) NSMutableArray *stations;

@property(nonatomic) NSInteger currentFrequency;

+ (StationManager *)instance;


- (void)addStation:(Station *)station;

- (Station *)getStationClosestToFrequency:(NSUInteger)freq;

- (void)clearAllStations;
@end