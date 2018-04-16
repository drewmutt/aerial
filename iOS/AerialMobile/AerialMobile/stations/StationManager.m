//
// Created by Andrew Simmons on 6/23/15.
// Copyright (c) 2015 Aero. All rights reserved.
//

#import "StationManager.h"
#import "Station.h"


@implementation StationManager {
NSMutableArray *_stations;
}

+ (StationManager *)instance {
    static StationManager *_instance = nil;

    @synchronized (self) {
        if (_instance == nil) {
            _instance = [[self alloc] init];
        }
    }

    return _instance;
}

- (instancetype)init {
    self = [super init];
    if (self) {
        _stations = [NSMutableArray new];
    }

    return self;
}

- (void)addStation:(Station *)station
{
    [_stations addObject:station];
}

-(Station *) getStationClosestToFrequency:(NSUInteger) freq {
    NSUInteger closestDistance = 100000;
    Station *closestStation = nil; 
    for (Station *station in _stations)
    {
        int dist = abs(station.frequency.unsignedIntegerValue - freq);
        if(dist < closestDistance)
        {
            closestStation = station;
            closestDistance = (NSUInteger) dist;
        }
    }

    return closestStation;

}

- (void)clearAllStations
{
    [_stations removeAllObjects];
}
@end